package com.odakota.tms.system.service.storage;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.odakota.tms.constant.MessageCode;
import com.odakota.tms.enums.file.StorageItemType;
import com.odakota.tms.enums.file.StorageType;
import com.odakota.tms.system.config.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author haidv
 * @version 1.0
 */
@Service
public class S3StorageService {

    private static final String ERROR_CODE_NO_SUCH_KEY = "NoSuchKey";
    private final AmazonS3 amazonS3;
    @Value("${storage-service.cdn-storage-name}")
    private String cdnStorageName;
    @Value("${storage-service.exports-storage-name}")
    private String exportStorageName;

    @Autowired
    public S3StorageService(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    private static ObjectMetadata convertToObjectMetadata(StorageItemMetadata metadata) {
        ObjectMetadata meta = new ObjectMetadata();
        metadata.getLastModified().ifPresent(meta::setLastModified);
        metadata.getContentLength().ifPresent(meta::setContentLength);
        metadata.getContentType().ifPresent(meta::setContentType);
        metadata.getContentLanguage().ifPresent(meta::setContentLanguage);
        metadata.getContentEncoding().ifPresent(meta::setContentEncoding);
        metadata.getContentMD5().ifPresent(meta::setContentMD5);
        metadata.getContentDisposition().ifPresent(meta::setContentDisposition);
        metadata.getCacheControl().ifPresent(meta::setCacheControl);
        return meta;
    }

    private static StorageItemMetadata convertToContetnsMetadata(ObjectMetadata metadata) {
        return StorageItemMetadata.builder()
                                  .lastModified(metadata.getLastModified())
                                  .contentLength(metadata.getContentLength())
                                  .contentType(metadata.getContentType())
                                  .contentLanguage(metadata.getContentLanguage())
                                  .contentEncoding(metadata.getContentEncoding())
                                  .contentMD5(metadata.getContentMD5())
                                  .contentDisposition(metadata.getContentDisposition())
                                  .cacheControl(metadata.getCacheControl())
                                  .build();
    }

    /**
     * get metadata for upload
     *
     * @param file upload target file
     * @return metadata
     * @throws IOException iOException
     */
    public static StorageItemMetadata extractMetadata(MultipartFile file) throws IOException {
        Resource fileResource = file.getResource();
        long contentLength = fileResource.contentLength();
        String contentType = file.getContentType();
        return StorageItemMetadata.builder()
                                  //.lastModified(lastModified)
                                  .contentLength(contentLength)
                                  .contentType(contentType)
                                  //.contentLanguage(null)
                                  //.contentEncoding(null)
                                  //.contentMD5(contentMD5)
                                  //.contentDisposition(null)
                                  //.cacheControl(null)
                                  .build();

    }

    /**
     * Acquires a list of files and directories directly under the specified directory. Please note that the obtained
     * {@link StorageItem} does not contain the contents of the file.
     *
     * @param type storage type
     * @param path directory path
     * @return list of files and directories
     */
    public List<StorageItem> list(final StorageType type, final String path) {
        final String s3Prefix = path + (path.endsWith("/") ? "" : "/");
        ListObjectsV2Request req = new ListObjectsV2Request();
        req.setBucketName(getBucket(type));
        req.setDelimiter("/");
        req.setPrefix(s3Prefix);
        ListObjectsV2Result result = amazonS3.listObjectsV2(req);
        Stream<StorageItem> directories = result.getCommonPrefixes().stream()
                                                .map(tmp -> new StorageItem(StorageItemType.FOLDER, tmp, null));
        Stream<StorageItem> files = result.getObjectSummaries().stream()
                                          .filter(tmp -> !tmp.getKey().equals(s3Prefix))
                                          .map(tmp -> new StorageItem(StorageItemType.FILE, tmp.getKey(),
                                                                      StorageItemMetadata.builder()
                                                                                         .contentLength(
                                                                                                 tmp.getSize())
                                                                                         .lastModified(
                                                                                                 tmp.getLastModified())
                                                                                         .build()));
        return Stream.concat(directories, files).collect(Collectors.toList());
    }

    /**
     * Acquires a list of files directly under the specified directory. Retrieved {@link StorageItem} Note that does not
     * include the contents of the file.
     *
     * @param type storage type
     * @param path directory path
     * @return directory list
     */
    public List<StorageItem> listFiles(final StorageType type, final String path) {
        return this.list(type, path).stream()
                   .filter(tmp -> tmp.getType().equals(StorageItemType.FILE))
                   .collect(Collectors.toList());
    }

    public List<StorageItem> listDirectories(final StorageType type, final String path) {
        return this.list(type, path).stream()
                   .filter(tmp -> tmp.getType().equals(StorageItemType.FOLDER))
                   .collect(Collectors.toList());
    }

    public Optional<StorageItem> getFile(final StorageType type, final String path) {
        try {
            S3Object object = amazonS3.getObject(getBucket(type), path);
            StorageItemMetadata metadata = convertToContetnsMetadata(object.getObjectMetadata());
            byte[] bytes;
            InputStream is = object.getObjectContent();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] readBuf = new byte[1024];
            int readLen;
            while ((readLen = is.read(readBuf)) > 0) {
                baos.write(readBuf, 0, readLen);
            }
            bytes = baos.toByteArray();

            StorageItem storageItem = new StorageItem(StorageItemType.FILE, object.getKey(), metadata, bytes);
            return Optional.of(storageItem);
        } catch (IOException e) {
            throw new CustomException(MessageCode.MSG_RUNTIME_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (AmazonS3Exception e) {
            if (e.getErrorCode().equals(ERROR_CODE_NO_SUCH_KEY)) {
                return Optional.empty();
            } else {
                throw new CustomException(MessageCode.MSG_DOWNLOAD_S3_FAIL, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (SdkClientException e) {
            throw new CustomException(MessageCode.MSG_DOWNLOAD_S3_FAIL, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get the file that matches the pattern from the specified directory.
     *
     * @param type    storage type
     * @param path    directory path
     * @param pattern pattern
     * @return file list
     */
    public List<StorageItem> find(final StorageType type, final String path, final String pattern) {
        return this.list(type, path);
    }

    /**
     * gets the file with the specified path
     *
     * @param type   storage type
     * @param path   file path
     * @param output the output stream to write the file data to
     */
    public void get(final StorageType type, final String path, final OutputStream output) {
        try {
            S3Object object = amazonS3.getObject(getBucket(type), path);
            S3ObjectInputStream input = object.getObjectContent();
            byte[] readBuf = new byte[1024];
            int readLen;
            while ((readLen = input.read(readBuf)) > 0) {
                output.write(readBuf, 0, readLen);
            }

        } catch (AmazonS3Exception e) {
            if (e.getErrorCode().equals(ERROR_CODE_NO_SUCH_KEY)) {
                throw new CustomException(MessageCode.MSG_FILE_NOT_FOUND, HttpStatus.NOT_FOUND);
            } else {
                throw new CustomException(MessageCode.MSG_DOWNLOAD_S3_FAIL, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IOException | SdkClientException e) {
            throw new CustomException(MessageCode.MSG_DOWNLOAD_S3_FAIL, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * save the file in the location specified by path
     *
     * @param type     storage type
     * @param path     file path
     * @param input    input stream of file data to save
     * @param metadata metadata to set for the file
     */
    public void put(final StorageType type, final String path, final InputStream input,
                    final StorageItemMetadata metadata) {
        final ObjectMetadata objectMetadata = convertToObjectMetadata(metadata);
        try {
            amazonS3.putObject(getBucket(type), path, input, objectMetadata);
        } catch (AmazonS3Exception e) {
            if (e.getErrorCode().equals(ERROR_CODE_NO_SUCH_KEY)) {
                throw new CustomException(MessageCode.MSG_FILE_NOT_FOUND, HttpStatus.NOT_FOUND);
            } else {
                throw new CustomException(MessageCode.MSG_UPLOAD_S3_FAIL, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (SdkClientException e) {
            throw new CustomException(MessageCode.MSG_UPLOAD_S3_FAIL, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * delete the file specified in path
     *
     * @param type storage type
     * @param path file path
     */
    public void remove(final StorageType type, final String path) {
        try {
            getS3Object(type, path)
                    .map(tmp -> {
                        amazonS3.deleteObject(getBucket(type), tmp.getKey());
                        return tmp;
                    })
                    .orElseThrow(() -> new CustomException(MessageCode.MSG_FILE_NOT_FOUND, HttpStatus.NOT_FOUND));
        } catch (SdkClientException e) {
            throw new CustomException(MessageCode.MSG_DELETE_S3_FAIL, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Optional<S3Object> getS3Object(final StorageType type, final String path) {
        // try to get as file
        try {
            S3Object object = amazonS3.getObject(getBucket(type), path);
            return Optional.of(object);
        } catch (AmazonS3Exception e) {
            if (!e.getErrorCode().equals(ERROR_CODE_NO_SUCH_KEY)) {
                throw new CustomException(MessageCode.MSG_RUNTIME_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        // If File does not exist, try to get it as a directory
        // NOTE: Amazon S3 recognizes a directory specification if / is added at the end of the object key.
        if (!path.endsWith("/")) {
            try {
                S3Object object = amazonS3.getObject(getBucket(type), path + "/");
                return Optional.of(object);
            } catch (AmazonS3Exception e) {
                if (!e.getErrorCode().equals(ERROR_CODE_NO_SUCH_KEY)) {
                    throw e;
                }
            }
        }
        return Optional.empty();
    }

    private String getBucket(StorageType type) {
        if (type.equals(StorageType.CDN)) {
            return cdnStorageName;
        } else if (type.equals(StorageType.EXPORTS)) {
            return exportStorageName;
        }
        return "";
    }

    /**
     * File copy between storages is executed asynchronously.
     *
     * @param srcType  copy source storage
     * @param srcPath  copy source file path
     * @param destType copy destination storage
     * @param destPath copy destination file path
     */
    public void asyncCopy(final StorageType srcType, final String srcPath, final StorageType destType,
                          final String destPath) {
        Thread thread = new Thread(() -> {
            String srcBucket = getBucket(srcType);
            String destBucket = getBucket(destType);
            try {
                amazonS3.copyObject(srcBucket, srcPath, destBucket, destPath);
            } catch (Exception e) {
                throw new CustomException(MessageCode.MSG_COPY_S3_FAIL, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        });
        thread.start();
    }
}
