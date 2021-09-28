package com.nashtech.rootkies.dto.asset.response;

import java.time.LocalDateTime;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DetailAssetDTO {
    private String assetCode;

    private String assetName;

    private Short state;

    private LocalDateTime installDate;

    private LocationDTO location;

    private String specification;

    private CategoryDTO category;

    private Collection<AssignmentDTO> assignments;
}
