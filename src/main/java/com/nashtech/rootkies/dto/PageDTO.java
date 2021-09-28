package com.nashtech.rootkies.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PageDTO {
    private int totalPages;
    private Long totalElements;
    private Object data;
}
