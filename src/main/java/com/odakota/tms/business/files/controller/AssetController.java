package com.odakota.tms.business.files.controller;

import com.odakota.tms.business.files.resource.UploadResource;
import com.odakota.tms.business.files.service.UploadService;
import com.odakota.tms.constant.ApiVersion;
import com.odakota.tms.enums.file.ImageType;
import com.odakota.tms.system.annotations.RequiredAuthentication;
import com.odakota.tms.system.config.data.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author haidv
 * @version 1.0
 */
@RestController
public class AssetController {

    private final UploadService assetService;

    @Autowired
    public AssetController(UploadService assetService) {
        this.assetService = assetService;
    }

    /**
     * upload an asset image
     *
     * @param file      upload file
     * @param imageType image type
     * @return file information file name file path
     */
    @SuppressWarnings("unchecked")
    @RequiredAuthentication
    @PostMapping(value = "/upload-assets", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
                 produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<ResponseData<UploadResource>> updateAssetFile(@RequestParam MultipartFile file,
                                                                        @RequestParam ImageType imageType) {
        return ResponseEntity.ok(new ResponseData<>().success(assetService.uploadFileToS3(file, imageType)));
    }
}
