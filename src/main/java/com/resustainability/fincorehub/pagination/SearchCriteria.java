package com.resustainability.fincorehub.pagination;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.resustainability.fincorehub.commons.StringUtils;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Pagination and search criteria")
public class SearchCriteria {

    @Schema(description = "Page size", example = "20")
    private Integer size;

    @Schema(description = "Page number (0-based)", example = "0")
    private Integer page;

    @Schema(description = "Sort field", example = "id")
    private String sort;

    @Schema(description = "Sort direction", allowableValues = {"ASC", "DESC"})
    private Sort.Direction direction;

    @Schema(description = "Free text search query")
    private String q;

    @Schema(description = "Ignore paging and return all results")
    private Boolean unpaged;

    @Schema(description = "Filter start local date-time format (ISO-8601)", example = "2023-01-01T00:00:00")
    private LocalDateTime start;

    @Schema(description = "Filter end local date-time format (ISO-8601)", example = "2060-12-31T23:59:59")
    private LocalDateTime end;

    @Schema(description = "Disable sorting")
    private boolean sortDisabled;

    public SearchCriteria() {}

    public SearchCriteria(Integer size, Integer page, String sort, Sort.Direction direction, String q, Boolean unpaged, LocalDateTime start, LocalDateTime end, boolean sortDisabled) {
        this.size = size;
        this.page = page;
        this.sort = sort;
        this.direction = direction;
        this.q = q;
        this.unpaged = unpaged;
        this.start = start;
        this.end = end;
        this.sortDisabled = sortDisabled;
    }

    public boolean isEmpty() {
        return StringUtils.isBlank(q) && null == start && null == end;
    }

    public Pageable toPageRequest() {
        if (Boolean.TRUE.equals(unpaged)) {
            return Pageable.unpaged();
        }

        if (null == size || size < 1 || size > 100) {
            size = 10;
        }

        if (null == page || page < 0) {
            page = 0;
        }

        return PageRequest.of(page, size, buildSort());
    }

    public Pageable toPageRequestUnLimit() {
        if (Boolean.TRUE.equals(unpaged)) {
            return Pageable.unpaged();
        }

        if (null == size || size < 1 || size > 100) {
            size = 1000000;
        }

        if (null == page || page < 0) {
            page = 0;
        }

        return PageRequest.of(page, size, buildSort());
    }
    
    public SearchCriteria setHibernateFields(Map<String, String> hibernateFields) {
        if (sortDisabled || hibernateFields == null || hibernateFields.isEmpty()) {
            return this;
        }

        Map<String, String> map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        map.putAll(hibernateFields);

        if (StringUtils.isNotBlank(sort) && map.containsKey(sort)) {
            sort = map.get(sort);
        } else {
            sort = null;
        }

        return this;
    }

    public SearchCriteria disableSort() {
        setSortDisabled(true);
        return setHibernateFields(Collections.emptyMap());
    }

    private Sort buildSort() {
        if (StringUtils.isBlank(sort)) {
            return Sort.unsorted();
        }

        if (null == direction) {
            direction = Sort.Direction.ASC;
        }

        return Sort.by(direction, sort);
    }

    public Integer getSize() {
        return size;
    }

    public Integer getPage() {
        return page;
    }

    public String getSort() {
        return sort;
    }

    public Sort.Direction getDirection() {
        return direction;
    }

    public String getQ() {
        return q;
    }

    public Boolean getUnpaged() {
        return unpaged;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public boolean isSortDisabled() {
        return sortDisabled;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public void setDirection(Sort.Direction direction) {
        this.direction = direction;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public void setUnpaged(Boolean unpaged) {
        this.unpaged = unpaged;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public void setSortDisabled(boolean sortDisabled) {
        this.sortDisabled = sortDisabled;
    }
}
