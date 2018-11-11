package com.omnitutor.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.omnitutor.application.entity.LectureEntity;
import com.omnitutor.application.entity.UserEntity;
import com.omnitutor.application.entity.UserOnLectureEntity;

public interface UserOnLectureRepository extends JpaRepository<UserOnLectureEntity, Long> {

	List<UserOnLectureEntity> findAllByUser(UserEntity user);

	UserOnLectureEntity findByUserAndLecture(UserEntity user, LectureEntity lecture);

}
