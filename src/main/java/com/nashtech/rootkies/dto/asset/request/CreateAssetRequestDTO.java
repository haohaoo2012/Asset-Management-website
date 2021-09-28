package com.nashtech.rootkies.dto.asset.request;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.nashtech.rootkies.constants.ErrorCode;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAssetRequestDTO {
    @NotBlank
    private String assetName;

    @NotNull
    private Short state;

    @NotNull
    private LocalDateTime installDate;

    @NotBlank
    private String specification;

    @NotBlank
    @Pattern(regexp = "(^[A-Z]{2,3}$)", message = ErrorCode.ERR_CATEGORY_IDS_NOT_CORRECT)
    private String categoryCode;
}
