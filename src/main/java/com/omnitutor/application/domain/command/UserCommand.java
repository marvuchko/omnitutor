package com.omnitutor.application.domain.command;

import java.sql.Date;
import java.util.List;

import com.omnitutor.application.domain.query.UserOnCourseQuery;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserCommand {

	private Long id;
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private String email;
	private String imageURL;
	private String role;
	private Date firstLogin;
	private Date lastLogin;
	private List<UserOnCourseQuery> userOnCourses;
	
}