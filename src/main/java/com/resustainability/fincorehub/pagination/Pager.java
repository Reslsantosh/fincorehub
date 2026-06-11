package com.resustainability.fincorehub.pagination;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public class Pager<T> {
	
	    private List<T> content;
	    private boolean first;
	    private boolean last;
	    private boolean empty;
	    private boolean sorted;
	    private long totalElements;
	    private long totalPages;
	    private long size;
	    private long page;
	    private long numberOfElements;

	    public Pager(List<T> content, boolean first, boolean last, boolean empty, boolean sorted, long totalElements, long totalPages, long size, long page, long numberOfElements) {
	        this.content = content;
	        this.first = first;
	        this.last = last;
	        this.empty = empty;
	        this.sorted = sorted;
	        this.totalElements = totalElements;
	        this.totalPages = totalPages;
	        this.size = size;
	        this.page = page;
	        this.numberOfElements = numberOfElements;
	    }

	    public static <T> Pager<T> of(Page<T> page) {
	        return new Pager<>(
	                page.getContent(),
	                page.isFirst(),
	                page.isLast(),
	                page.isEmpty(),
	                page.getSort().isSorted(),
	                page.getTotalElements(),
	                page.getTotalPages(),
	                page.getSize(),
	                page.getNumber(),
	                page.getNumberOfElements()
	        );
	    }

	    public static <T> Pager<T> empty(Pageable pageable) {
	        return new Pager<>(
	                List.of(),
	                true,
	                true,
	                true,
	                pageable.getSort().isSorted(),
	                0,
	                0,
	                pageable.getPageSize(),
	                pageable.getPageNumber(),
	                0
	        );
	    }

	    public List<T> getContent() {
	        return content;
	    }

	    public boolean isFirst() {
	        return first;
	    }

	    public boolean isLast() {
	        return last;
	    }

	    public boolean isEmpty() {
	        return empty;
	    }

	    public boolean isSorted() {
	        return sorted;
	    }

	    public long getTotalElements() {
	        return totalElements;
	    }

	    public long getTotalPages() {
	        return totalPages;
	    }

	    public long getSize() {
	        return size;
	    }

	    public long getPage() {
	        return page;
	    }

	    public long getNumberOfElements() {
	        return numberOfElements;
	    }

	    public void setContent(List<T> content) {
	        this.content = content;
	    }

	    public void setFirst(boolean first) {
	        this.first = first;
	    }

	    public void setLast(boolean last) {
	        this.last = last;
	    }

	    public void setEmpty(boolean empty) {
	        this.empty = empty;
	    }

	    public void setSorted(boolean sorted) {
	        this.sorted = sorted;
	    }

	    public void setTotalElements(long totalElements) {
	        this.totalElements = totalElements;
	    }

	    public void setTotalPages(long totalPages) {
	        this.totalPages = totalPages;
	    }

	    public void setSize(long size) {
	        this.size = size;
	    }

	    public void setPage(long page) {
	        this.page = page;
	    }

	    public void setNumberOfElements(long numberOfElements) {
	        this.numberOfElements = numberOfElements;
	    }
	

}
