package com.omnitutor.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.omnitutor.application.entity.QuestionAnswerEntity;
import com.omnitutor.application.entity.QuestionEntity;

public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswerEntity, Long> {

	List<QuestionAnswerEntity> findAllByQuestion(QuestionEntity question);

}
