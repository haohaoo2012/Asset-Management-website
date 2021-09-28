package com.nashtech.rootkies.repository.specs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SearchCriteria1 {
    private String key;
    private String operation;
    private Object value;

}
