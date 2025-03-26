package com.ctsousa.mover.response;

import lombok.Getter;

import java.util.List;

@Getter
public class PaginationResponse<T> {

    private final List<T> content;
    private final int totalPages;
    private final long totalElements;
    private final int currentPage;
    private final int pageSize;
    private final boolean lastPage;

    public PaginationResponse(List<T> content, long totalElements, int currentPage, int pageSize) {
        this.content = content;
        this.totalElements = totalElements;
        this.currentPage = currentPage + 1;
        this.pageSize = pageSize;
        this.totalPages = (int) Math.ceil((double) totalElements / pageSize);
        this.lastPage = (this.currentPage == this.totalPages);
    }
}
