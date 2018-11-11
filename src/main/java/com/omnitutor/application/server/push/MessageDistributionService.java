package com.omnitutor.application.server.push;

import org.springframework.stereotype.Service;

import com.vaadin.spring.annotation.UIScope;

import rx.Observable;
import rx.subjects.PublishSubject;

@Service
@UIScope
public class MessageDistributionService {

	private PublishSubject<String> messagePublisher = PublishSubject.create();
	
	public void broadcastMessage(String message) {
		messagePublisher.onNext(message);
	}
	
	public Observable<String> getObservable() {
		return messagePublisher.asObservable();
	}
	
}
