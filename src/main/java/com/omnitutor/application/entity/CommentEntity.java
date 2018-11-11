package com.omnitutor.application.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.omnitutor.application.util.EntityUtil;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CommentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(columnDefinition = EntityUtil.CLOB)
	@Lob
	private String text;
	
	@OneToOne
	private UserEntity creator;
	
	@ManyToOne
	private CourseEntity course;
	
}
