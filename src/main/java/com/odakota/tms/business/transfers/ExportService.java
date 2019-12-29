package com.odakota.tms.business.transfers;

import com.odakota.tms.business.auth.entity.Branch;
import com.odakota.tms.business.auth.entity.Role;
import com.odakota.tms.business.auth.entity.User;
import com.odakota.tms.business.auth.repository.BranchRepository;
import com.odakota.tms.business.auth.repository.RoleRepository;
import com.odakota.tms.business.auth.repository.UserRepository;
import com.odakota.tms.constant.Constant;
import com.odakota.tms.enums.FileGroup;
import com.odakota.tms.system.base.BaseEntity;
import com.odakota.tms.system.config.exception.CustomException;
import com.odakota.tms.system.export.ExcelExport;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author haidv
 * @version 1.0
 */
@Service
public class ExportService<T extends BaseEntity> {

    private final ExcelExport<T> excelExport;

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final BranchRepository branchRepository;

    @Autowired
    public ExportService(ExcelExport<T> excelExport, RoleRepository roleRepository,
                         UserRepository userRepository,
                         BranchRepository branchRepository) {
        this.excelExport = excelExport;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.branchRepository = branchRepository;
    }

    public byte[] export(FileGroup fileGroup, HttpServletResponse response) {

        String fileName;
        Map<String, Object> map = new HashMap<>();
        switch (fileGroup) {
            case ROLE:
                fileName = "role-information-" + new Date().getTime() + ".xls";
                map.put(Constant.MODEL_LIST_DATA, roleRepository.findByDeletedFlagFalse());
                map.put(Constant.MODEL_SHEET_NAME, "role");
                map.put(Constant.MODEL_CLAZZ, Role.class);
                break;
            case USER:
                fileName = "user-information-" + new Date().getTime() + ".xls";
                map.put(Constant.MODEL_LIST_DATA, userRepository.findByDeletedFlagFalse());
                map.put(Constant.MODEL_SHEET_NAME, "user");
                map.put(Constant.MODEL_CLAZZ, User.class);
                break;
            case BRANCH:
                fileName = "branch-information-" + new Date().getTime() + ".xls";
                map.put(Constant.MODEL_LIST_DATA, branchRepository.findByDeletedFlagFalse());
                map.put(Constant.MODEL_SHEET_NAME, "branch");
                map.put(Constant.MODEL_CLAZZ, Branch.class);
                break;
            default:
                throw new CustomException("", HttpStatus.INTERNAL_SERVER_ERROR);

        }
        try {
            XSSFWorkbook workbook;
            byte[] contentReturn;
            /* Logic to Export Excel */
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

            OutputStream out;
            workbook = (XSSFWorkbook) excelExport.buildExcelDocument(map);
            out = response.getOutputStream();
            workbook.write(out);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            workbook.write(byteArrayOutputStream);
            contentReturn = byteArrayOutputStream.toByteArray();
            return contentReturn;
        } catch (Exception e) {
            throw new CustomException("", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
