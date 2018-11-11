package com.omnitutor.application.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import com.omnitutor.application.util.EntityUtil;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String firstName;
	
	private String lastName;
	
	private String username;
	
	private String password;
	
	private String email;
	
	private Date firstLogin;
	
	private Date lastLogin;
	
	@Column(columnDefinition = EntityUtil.CLOB)
	@Lob
	private String imageURL;
	
	@ManyToOne()
	private RoleEntity role;

}
