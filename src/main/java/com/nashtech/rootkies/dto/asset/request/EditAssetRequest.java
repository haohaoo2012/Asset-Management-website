package com.nashtech.rootkies.dto.asset.request;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditAssetRequest {
    private String name;
    private String specification;
    private String installDate;
    private String state;
}
