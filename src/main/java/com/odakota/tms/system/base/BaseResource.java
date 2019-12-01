package com.odakota.tms.system.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Super interface for all resource classes of applications. <br> A resource is a unit of data input / output handled in
 * the service controller layer, and is usually defined as a collection of multiple entities. <br> Resource classes
 * provide methods for retrieving and storing properties associated with a resource.
 *
 * @param <E> {@link BaseEntity}
 * @author haidv
 * @version 1.0
 */
@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseResource<E extends BaseEntity> {

    protected Long id;
}
