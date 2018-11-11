package com.omnitutor.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.omnitutor.application.entity.QuestionEntity;
import com.omnitutor.application.entity.QuizEntity;

public interface QuestionRepository extends JpaRepository<QuestionEntity, Long> {

	List<QuestionEntity> findAllByQuiz(QuizEntity quiz);

}
