package com.nashtech.rootkies.dto.asset.request;

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
public class SearchFilterSortAssetDTO {
    private List<Short> states;

    private List<String> categoriesCode;

    private String sortField;

    private String sortType;

    private String searchKeyWord;
}
