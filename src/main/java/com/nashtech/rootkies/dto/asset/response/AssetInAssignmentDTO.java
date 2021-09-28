package com.nashtech.rootkies.dto.asset.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssetInAssignmentDTO {
    String code;
    String name;
    String category;
}
