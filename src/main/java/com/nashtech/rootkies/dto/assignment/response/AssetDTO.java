package com.nashtech.rootkies.dto.assignment.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AssetDTO {
    private String assetCode;

    private String assetName;

    private String specification;

    private CategoryDTO category;
}
