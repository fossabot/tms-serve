package com.odakota.tms.system.base;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Resource list acquisition response
 *
 * @param <R> {@link BaseResource}
 * @author haidv
 * @version 1.0
 */
@Getter
public class BaseResponse<R extends BaseResource<?>> {

    private Pagination pagination;

    private List<R> data;

    public BaseResponse(List<R> data) {
        this.data = data;
    }

    public BaseResponse(Page<R> page) {
        this.data = page.getContent();
        this.pagination = new Pagination(page.getTotalPages(), page.getNumber() + 1, page.getSize(),
                                         page.getTotalElements());
    }

    public BaseResponse(List<R> data, Page page) {
        this.data = data;
        this.pagination = new Pagination(page.getTotalPages(), page.getNumber() + 1, page.getSize(),
                                         page.getTotalElements());
    }

    @Getter
    private static class Pagination {

        private long totalPage;

        private long currentPage;

        private long pageSize;

        private long totalElement;

        Pagination(long totalPage, long currentPage, long pageSize, long totalElement) {
            this.totalPage = totalPage;
            this.currentPage = currentPage;
            this.pageSize = pageSize;
            this.totalElement = totalElement;
        }
    }
}
