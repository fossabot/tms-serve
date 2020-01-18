package com.odakota.tms.business.auth.controller;

import com.odakota.tms.business.auth.entity.Brand;
import com.odakota.tms.business.auth.resource.BrandResource;
import com.odakota.tms.business.auth.service.BrandService;
import com.odakota.tms.constant.ApiVersion;
import com.odakota.tms.enums.auth.ApiId;
import com.odakota.tms.system.annotations.RequiredAuthentication;
import com.odakota.tms.system.base.BaseController;
import com.odakota.tms.system.base.BaseParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author haidv
 * @version 1.0
 */
@RestController
public class BrandController extends BaseController<Brand, BrandResource> {

    private final BrandService service;

    @Autowired
    protected BrandController(BrandService service) {
        super(service);
        this.service = service;
    }

    /**
     * API get brand list
     *
     * @param baseReq List acquisition request
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication(value = ApiId.R_BRAND)
    @GetMapping(value = "/brands", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> getBrands(@ModelAttribute @Valid BaseParameter baseReq) {
        return super.getResources(baseReq);
    }

    /**
     * API get brand by id
     *
     * @param id role id
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication(value = ApiId.R_BRAND)
    @GetMapping(value = "/brands/{id}", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> getBrand(@PathVariable Long id) {
        return super.getResource(id);
    }

    /**
     * API create new brand
     *
     * @param resource {@link BrandResource}
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication(value = ApiId.C_BRAND)
    @PostMapping(value = "/brands", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> createBrand(@Validated @RequestBody BrandResource resource) {
        return super.createResource(resource);
    }

    /**
     * API update brand
     *
     * @param id       brand id
     * @param resource {@link BrandResource}
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication(value = ApiId.U_BRAND)
    @PutMapping(value = "/brands/{id}", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> updateBrand(@PathVariable Long id, @RequestBody BrandResource resource) {
        return super.updateResource(id, resource);
    }

    /**
     * API delete brand
     *
     * @param id brand id
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication(value = ApiId.D_BRAND)
    @DeleteMapping(value = "/brands/{id}", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<Void> deleteBrand(@PathVariable Long id) {
        return super.deleteResource(id);
    }
}
