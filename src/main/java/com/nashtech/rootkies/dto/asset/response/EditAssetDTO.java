package com.nashtech.rootkies.dto.asset.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditAssetDTO {
    private String assetCode;
    private String assetName;
    private Short state;
    private LocalDateTime installDate;
    private Long locationId;
    private String specification;
    private Boolean isDeleted;
    private String categoryId;
}
