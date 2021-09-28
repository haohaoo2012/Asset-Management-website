package com.nashtech.rootkies.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Table(name = "assets", indexes = { @Index(name = "asset_name_idx", columnList = "assetname"),
                @Index(name = "asset_category_idx", columnList = "categorycode"),
                @Index(name = "asset_state_idx", columnList = "state") })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asset {
        @Id
        @Column(name = "assetcode")
        @GeneratedValue(generator = "assetCodeGenerator")
        @GenericGenerator(name = "assetCodeGenerator", strategy = "com.nashtech.rootkies.generator.AssetCodeGenerator")
        private String assetCode;

        @NotBlank
        @Column(name = "assetname")
        private String assetName;

        @NotNull
        @Column(name = "state")
        private Short state;

        @NotNull
        @Column(name = "installdate")
        private LocalDateTime installDate;

        @NotNull
        @ManyToOne
        @JoinColumn(name = "locationid")
        private Location location;

        @NotBlank
        @Column(name = "specification")
        private String specification;

        @NotNull
        @Column(name = "isdeleted")
        private Boolean isDeleted;

        @NotNull
        @ManyToOne
        @JoinColumn(name = "categorycode")
        private Category category;

        @OneToMany(mappedBy = "asset" ,fetch = FetchType.LAZY)
        private Collection<Assignment> assignments;
}
