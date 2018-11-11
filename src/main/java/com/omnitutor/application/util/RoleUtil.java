package com.omnitutor.application.util;


import com.omnitutor.application.domain.query.UserQuery;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RoleUtil {

	public static final String USER = "User";
	
	public static final String ADMIN = "Admin";
	
	public static final String GUEST = "Guest";

	public static String getRole(UserQuery user) {
		if(user == null) return GUEST;
		return user.getRole();
	}

}
