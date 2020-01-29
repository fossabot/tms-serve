package com.odakota.tms.business.files.service;

import com.odakota.tms.business.files.resource.UploadResource;
import com.odakota.tms.constant.FilePath;
import com.odakota.tms.constant.MessageCode;
import com.odakota.tms.enums.file.ImageType;
import com.odakota.tms.enums.file.StorageType;
import com.odakota.tms.system.config.UserSession;
import com.odakota.tms.system.config.exception.CustomException;
import com.odakota.tms.system.service.storage.S3StorageService;
import com.odakota.tms.system.service.storage.StorageItemMetadata;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author haidv
 * @version 1.0
 */
@Service
public class UploadService {

    private static final String[] EXTENSIONS = {"jpg", "jpeg", "png", "gif"};

    private static int MAX_FILE_SIZE = 1048576;

    private final S3StorageService storageService;

    private final UserSession userSession;

    @Value("${storage-service.cdn-url}")
    private String urlBase;

    @Autowired
    public UploadService(S3StorageService storageService, UserSession userSession) {
        this.storageService = storageService;
        this.userSession = userSession;
    }

    private static void maxImageFileSize(MultipartFile file) {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new CustomException(MessageCode.MSG_IMAGE_MAX_SIZE, HttpStatus.BAD_REQUEST);
        }
    }

    private static void imageFileExtension(MultipartFile file) {
        String name = file.getOriginalFilename();
        assert name != null;
        String[] fileName = name.split("\\.");
        String extension = fileName[fileName.length - 1];
        if (!Arrays.asList(EXTENSIONS).contains(extension.toLowerCase())) {
            throw new CustomException(MessageCode.MSG_VALIDATION, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Upload an image
     *
     * @param file    image file
     * @param type    image type
     * @return resources after upload
     */
    public UploadResource uploadFileToS3(MultipartFile file, ImageType type) {
        String imagePath;
        String imageName;
        switch (type) {
            case BRAND_IMAGE:
                imagePath = FilePath.BRAND_IMAGE_PATH;
                break;
            case AVATAR:
                imagePath = FilePath.AVATAR_IMAGE;
                imagePath = imagePath.replace("{userId}", userSession.getUserId().toString());
                break;
            default:
                throw new CustomException(MessageCode.MSG_INVALID_IMAGE_TYPE, HttpStatus.BAD_REQUEST);
        }

        if (file != null && !file.isEmpty() && !imagePath.isEmpty()) {
            maxImageFileSize(file);
            imageFileExtension(file);

            imageName = file.getOriginalFilename();

            StorageItemMetadata meta = StorageItemMetadata.builder().build();
            try {
                meta = S3StorageService.extractMetadata(file);
            } catch (IOException e) {
                throw new CustomException(MessageCode.MSG_RUNTIME_EXCEPTION, HttpStatus.BAD_REQUEST);
            }

            try {
                storageService.put(StorageType.CDN, imagePath + imageName, file.getInputStream(), meta);
            } catch (IOException e) {
                throw new CustomException(MessageCode.MSG_RUNTIME_EXCEPTION, HttpStatus.BAD_REQUEST);
            }

            String fullUrl = urlBase + imagePath + imageName;
            return new UploadResource(fullUrl, imageName);
        } else {
            return new UploadResource(null, null);
        }
    }

    /**
     * Confirm the path before deleting the image.
     *
     * @param originalPath image path before update
     * @param newPath      image path to be updated
     */
    public void handleImage(String originalPath, String newPath) {
        if (StringUtils.isEmpty(newPath) || (!originalPath.equals(newPath))) {
            removeFileFromS3(originalPath);
        }
    }

    /**
     * delete image
     *
     * @param path image path to be deleted
     */
    public void removeFileFromS3(String path) {
        if (!StringUtils.isEmpty(path)) {
            storageService.remove(StorageType.CDN, path);
        }
    }
}
