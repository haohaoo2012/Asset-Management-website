package com.nashtech.rootkies.dto.asset.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CreateAssetResponseDTO {
    private String assetCode;

    private String assetName;

    private Short state;

    private LocalDateTime installDate;

    private LocationDTO location;

    private String specification;

    private CategoryDTO category;

}
