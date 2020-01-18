package com.odakota.tms.business.auth.resource;

import com.odakota.tms.business.auth.entity.Brand;
import com.odakota.tms.system.base.BaseCondition;
import com.odakota.tms.system.base.BaseResource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author haidv
 * @version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
public class BrandResource extends BaseResource<Brand> {

    private static final long serialVersionUID = 1L;

    private String brandName;

    private String brandCode;

    private String brandImage;

    public BrandResource(Long id) {
        super(id);
    }

    /**
     * @author haidv
     * @version 1.0
     */
    @Setter @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BrandCondition extends BaseCondition {

        private String brandName;
    }
}
