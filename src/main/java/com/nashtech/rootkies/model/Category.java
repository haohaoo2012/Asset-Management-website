package com.nashtech.rootkies.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.nashtech.rootkies.constants.ErrorCode;

import java.util.*;

@Entity
@Table(name = "categories", uniqueConstraints = { @UniqueConstraint(columnNames = "categorycode"),
        @UniqueConstraint(columnNames = "categoryname") }, indexes = {
                @Index(name = "category_name_idx", columnList = "categoryname") })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {
    @Id
    @Pattern(regexp = "(^[A-Z]{2,3}$)", message = ErrorCode.ERR_CATEGORY_IDS_NOT_CORRECT)
    @Column(name = "categorycode")
    private String categoryCode;

    @NotBlank
    @Column(name = "categoryname")
    private String categoryName;

    @OneToMany(mappedBy = "category")
    private Collection<Asset> assets;
}
