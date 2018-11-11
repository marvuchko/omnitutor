package com.omnitutor.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.omnitutor.application.entity.NotificationEntity;

public interface NotoficationRepository extends JpaRepository<NotificationEntity, Long> {

}
