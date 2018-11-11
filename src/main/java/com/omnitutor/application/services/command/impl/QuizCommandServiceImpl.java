package com.omnitutor.application.services.command.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.omnitutor.application.domain.command.QuizCommand;
import com.omnitutor.application.entity.CourseEntity;
import com.omnitutor.application.entity.QuestionAnswerEntity;
import com.omnitutor.application.entity.QuestionEntity;
import com.omnitutor.application.entity.QuizEntity;
import com.omnitutor.application.entity.QuizScoreEntity;
import com.omnitutor.application.entity.UserEntity;
import com.omnitutor.application.entity.UserOnCourseEntity;
import com.omnitutor.application.repository.CourseRepository;
import com.omnitutor.application.repository.QuestionAnswerRepository;
import com.omnitutor.application.repository.QuestionRepository;
import com.omnitutor.application.repository.QuizRepository;
import com.omnitutor.application.repository.QuizScoreRepository;
import com.omnitutor.application.repository.UserOnCourseRepository;
import com.omnitutor.application.repository.UserRepository;
import com.omnitutor.application.services.command.QuizCommandService;

@Service
public class QuizCommandServiceImpl implements QuizCommandService {

	private final QuizRepository quizRepository;

	private final QuestionRepository questionRepository;

	private final QuestionAnswerRepository questionAnswerRepository;

	private final CourseRepository courseRepository;

	private final UserRepository userRepository;

	private final UserOnCourseRepository userOnCourseRepository;

	private final QuizScoreRepository quizScoreRepository;

	public QuizCommandServiceImpl(QuizRepository quizRepository, QuestionRepository questionRepository,
			QuestionAnswerRepository questionAnswerRepository, CourseRepository courseRepository,
			UserRepository userRepository, UserOnCourseRepository userOnCourseRepository,
			QuizScoreRepository quizScoreRepository) {
		this.quizRepository = quizRepository;
		this.questionRepository = questionRepository;
		this.questionAnswerRepository = questionAnswerRepository;
		this.courseRepository = courseRepository;
		this.userRepository = userRepository;
		this.userOnCourseRepository = userOnCourseRepository;
		this.quizScoreRepository = quizScoreRepository;
	}

	@Override
	public QuizEntity createQuiz(QuizCommand quiz) throws Exception {
		CourseEntity course = courseRepository.findById(quiz.getCourseId())
				.orElseThrow(() -> new Exception("Quiz not found in the database!"));
		List<QuestionEntity> questions = new ArrayList<>();
		List<QuestionAnswerEntity> answers = new ArrayList<>();
		QuizEntity entity = new QuizEntity();
		entity.setCourse(course);
		entity.setDescription(quiz.getDescription());
		entity.setName(quiz.getName());
		entity.setMaxPoints(0);
		quiz.getQuestions().forEach(question -> {
			QuestionEntity questionEntity = new QuestionEntity();
			questionEntity.setQuiz(entity);
			questionEntity.setQuestionText(question.getQuestionText());
			question.getAnswers().forEach(answer -> {
				QuestionAnswerEntity answerEntity = new QuestionAnswerEntity();
				answerEntity.setText(answer.getText());
				answerEntity.setQuestion(questionEntity);
				answerEntity.setIsCorrect(answer.getIsCorrect());
				entity.setMaxPoints(entity.getMaxPoints() + 10);
				answers.add(answerEntity);
			});
			questions.add(questionEntity);
		});
		quizRepository.save(entity);
		questions.forEach(question -> {
			questionRepository.save(question);
		});
		answers.forEach(answer -> {
			questionAnswerRepository.save(answer);
		});
		course.setMaxPoints(course.getMaxPoints() + entity.getMaxPoints());
		courseRepository.save(course);
		return entity;
	}

	@Override
	public void addPointsToCourse(Long userId, Long courseId, Long quizId, Integer score) throws Exception {
		UserEntity user = userRepository.findById(userId)
				.orElseThrow(() -> new Exception("User not found in the database!"));
		CourseEntity course = courseRepository.findById(courseId)
				.orElseThrow(() -> new Exception("Course not found in the database!"));
		QuizEntity quiz = quizRepository.findById(quizId)
				.orElseThrow(() -> new Exception("Quiz not found in the database!"));
		UserOnCourseEntity userOnCourse = userOnCourseRepository.findAllByUserAndCourse(user, course)
				.orElseThrow(() -> new Exception("User on course not found!"));
		QuizScoreEntity quizScore = quizScoreRepository.findByUserOnCourse(userOnCourse);
		if(quizScore == null) {
			quizScore = new QuizScoreEntity();
			quizScore.setQuiz(quiz);
			quizScore.setScore(score);
			quizScore.setUserOnCourse(userOnCourse);
		}
		quizScoreRepository.save(quizScore);
		List<QuizScoreEntity> quizesScore = quizScoreRepository.findAllByUserOnCourse(userOnCourse);
		Integer newScore = 0;
		for(QuizScoreEntity q : quizesScore)
			newScore += q.getScore();
		userOnCourse.setHasQuiz(true);
		userOnCourse.setScore(newScore);
		userOnCourseRepository.save(userOnCourse);
	}

}
