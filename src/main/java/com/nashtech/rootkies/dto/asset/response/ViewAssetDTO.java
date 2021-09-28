package com.nashtech.rootkies.dto.asset.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ViewAssetDTO {
    private String assetCode;

    private String assetName;

    private Short state;

    private CategoryDTO category;
}
