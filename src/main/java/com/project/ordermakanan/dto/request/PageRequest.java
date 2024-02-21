package com.project.ordermakanan.dto.request;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageRequest {
    private String sortBy;
    private Integer pageSize;
    private Integer pageNumber;

    public Pageable getPage(){
        int pageNumberValue = (pageNumber != null) ? pageNumber < 1 ? 1 : pageNumber : 1;
        int pageSizeValue = (pageSize != null) ? pageSize : 10;
        Sort sort = null;

        if (sortBy != null) {
            String[] parts = sortBy.split(",");
            String sortField = parts[0];
            String sortOrder = parts.length > 1 ? parts[1] : "ASC";
            sort = Sort.by(Sort.Direction.fromString(sortOrder), sortField);
        } else {
            sort = Sort.by(Direction.ASC, "foodName");
        }

        return org.springframework.data.domain.PageRequest.of(pageNumberValue - 1, pageSizeValue, sort);
    }
}
