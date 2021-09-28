package com.nashtech.rootkies.dto.assignment.request;

import java.time.LocalDateTime;
import java.util.List;

import com.nashtech.rootkies.enums.SortType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SearchFilterSortAssignmentDTO {
    private List<Short> states;

    private LocalDateTime localDateTime;

    private String sortField;

    private String sortType;

    private String searchKeyWord;
}
