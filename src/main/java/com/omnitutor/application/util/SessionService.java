package com.omnitutor.application.util;

import java.util.List;

import org.springframework.stereotype.Service;

import com.omnitutor.application.domain.query.LectureQuery;
import com.omnitutor.application.domain.query.UserQuery;
import com.vaadin.spring.annotation.UIScope;

import lombok.Getter;
import lombok.Setter;

@Service
@UIScope
@Getter
@Setter
public class SessionService {
	
	private UserQuery user;
	
	private List<LectureQuery> lectures;

}
