package com.nashtech.rootkies.dto.request.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateRequestDTO {
    @NotBlank
    private String requestedBy;

    @NotNull
    private Long assignment;
}
