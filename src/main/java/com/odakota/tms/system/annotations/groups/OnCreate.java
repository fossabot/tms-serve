package com.odakota.tms.system.annotations.groups;

/**
 * Bean verification group applied when creating resources
 *
 * <p>
 * Constraints belonging to this group are only applied when calling {@link BaseService # createResource
 * (BaseResource)}. <br> Example of use:
 *
 * <pre>
 * <code>
 * public DemoResource extends BaseResource{@literal <}DemoEntity{@literal >} {
 *
 *     {@literal @}NotNull(groups = OnCreate.class)
 *     private String name;
 * }
 * </code>
 * </pre>
 *
 * @author haidv
 * @version 1.0
 */
public interface OnCreate {
}
