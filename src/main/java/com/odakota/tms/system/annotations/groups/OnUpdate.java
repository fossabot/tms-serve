package com.odakota.tms.system.annotations.groups;

/**
 * Bean verification group applied when updating resources
 *
 * <p>
 * Constraints belonging to this group are only applied when {@link BaseService # updateResource (ID, BaseResource)} is
 * called. <br> Example of use:
 *
 * <pre>
 * <code>
 * public DemoResource extends BaseResource{@literal <}DemoEntity{@literal >} {
 *
 *     {@literal @}NotNull(groups = OnUpdate.class)
 *     private String name;
 * }
 * </code>
 * </pre>
 *
 * @author haidv
 * @version 1.0
 */
public interface OnUpdate {
}
