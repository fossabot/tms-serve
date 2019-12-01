package com.odakota.tms.system.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Base class for application repositories. <br> The repository provides methods for read / write operations on a single
 * table. <br> This class provides an implementation of the most common methods that should be supported by all
 * repository classes.
 *
 * @param <E> the domain type the repository manages
 * @author haidv
 * @version 1.0
 */
public interface BaseRepository<E extends BaseEntity, C extends BaseCondition> extends JpaRepository<E, Long>, JpaSpecificationExecutor<E> {

    /**
     * Returns a {@link Page} of entities meeting the paging restriction provided in the {@code Pageable} object and
     * {@code C condition} object.
     *
     * @param condition condition search
     * @param pageable  paging restriction
     * @return a page of entities
     */
    @Query("select e from #{#entityName}  e where e.deletedFlag = false ")
    Page<E> findByCondition(C condition, Pageable pageable);

    /**
     * Returns all entities matched condition find {@code C condition}.
     *
     * @param condition condition search
     * @return list entities
     */
    @Query("select e from #{#entityName}  e where e.deletedFlag = false ")
    List<E> findByCondition(C condition);

    /**
     * Get an entity from the database by specifying the entity identifier.
     *
     * @param id entity identifier
     * @return entity
     */
    Optional<E> findByIdAndDeletedFlagFalse(Long id);

    /**
     * Checks whether the data store contains elements with the given entity identifier.
     *
     * @param id entity identifier
     * @return boolean
     */
    boolean existsByIdAndDeletedFlagFalse(Long id);

    /**
     * Deletes the entity with the given entity identifier.
     *
     * @param id entity identifier
     */
    default void deleteById(Long id) {
        Optional<E> optionalE = this.findByIdAndDeletedFlagFalse(id);
        if (optionalE.isPresent()) {
            E e = optionalE.get();
            e.setDeletedFlag(true);
            this.save(e);
        }
    }

    /**
     * Deletes the entity.
     *
     * @param e entity
     */
    default void delete(E e) {
        e.setDeletedFlag(true);
        this.save(e);
    }
}
