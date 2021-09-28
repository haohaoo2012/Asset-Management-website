package com.nashtech.rootkies.model;

import com.nashtech.rootkies.enums.ERole;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "roles",
		indexes ={
				@Index(name = "role_idx" , columnList = "rolename")
		}
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Role {
	@Id
	@GeneratedValue
	@Column(name = "roleid")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "rolename", nullable = false)
	private ERole roleName;

	@OneToMany(mappedBy = "role")
	private Collection<User> users;
}