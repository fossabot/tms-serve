package com.odakota.tms.business.auth.controller;

import com.odakota.tms.business.auth.entity.Branch;
import com.odakota.tms.business.auth.resource.BranchResource;
import com.odakota.tms.business.auth.service.BranchService;
import com.odakota.tms.business.transfers.ExportService;
import com.odakota.tms.constant.ApiVersion;
import com.odakota.tms.enums.ApiId;
import com.odakota.tms.enums.FileGroup;
import com.odakota.tms.system.annotations.RequiredAuthentication;
import com.odakota.tms.system.base.BaseController;
import com.odakota.tms.system.base.BaseParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@RestController
public class BranchController extends BaseController<Branch, BranchResource> {

    private final BranchService service;

    private final ExportService exportService;

    @Autowired
    protected BranchController(BranchService service, ExportService exportService) {
        super(service);
        this.service = service;
        this.exportService = exportService;
    }

    /**
     * API get branch list
     *
     * @param baseReq List acquisition request
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication(value = ApiId.R_BRANCH)
    @GetMapping(value = "/branches", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> getBranches(@ModelAttribute @Valid BaseParameter baseReq) {
        return super.getResources(baseReq);
    }

    /**
     * API get branch by id
     *
     * @param id role id
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication(value = ApiId.R_BRANCH)
    @GetMapping(value = "/branches/{id}", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> getBranch(@PathVariable Long id) {
        return super.getResource(id);
    }

    /**
     * API create new branch
     *
     * @param resource {@link BranchResource}
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication(value = ApiId.C_BRANCH)
    @PostMapping(value = "/branches", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> createBranch(@Validated @RequestBody BranchResource resource) {
        return super.createResource(resource);
    }

    /**
     * API update branch
     *
     * @param id       branch id
     * @param resource {@link BranchResource}
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication(value = ApiId.U_BRANCH)
    @PutMapping(value = "/branches/{id}", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> updateBranch(@PathVariable Long id, @RequestBody BranchResource resource) {
        return super.updateResource(id, resource);
    }

    /**
     * API delete branch
     *
     * @param id branch id
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication(value = ApiId.D_BRANCH)
    @DeleteMapping(value = "/branches/{id}", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<Void> deleteBranch(@PathVariable Long id) {
        return super.deleteResource(id);
    }

    /**
     * API batch delete branch
     *
     * @param ids list branch id
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication(value = ApiId.D_BRANCH)
    @DeleteMapping(value = "/branches", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<Void> batchDeleteBranch(@RequestParam List<Long> ids) {
        return super.batchDeleteResource(ids);
    }

    /**
     * API export branches
     *
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication(value = ApiId.E_BRANCH)
    @GetMapping(value = "/branches/export", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<byte[]> exportBranch(HttpServletResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
                MediaType.APPLICATION_OCTET_STREAM);//MediaType.parseMediaType("application/vnd.ms-excel")
        return new ResponseEntity<>(exportService.export(FileGroup.BRANCH, response), headers, HttpStatus.OK);
    }
}
