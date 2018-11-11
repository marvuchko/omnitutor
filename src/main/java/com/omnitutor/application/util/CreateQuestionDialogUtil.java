package com.omnitutor.application.util;

import com.omnitutor.application.components.CreateQuestionDialog;
import com.omnitutor.application.domain.command.QuestionCommand;
import com.omnitutor.application.services.command.QuizCommandService;
import com.vaadin.ui.UI;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import rx.Observable;
import rx.subjects.PublishSubject;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateQuestionDialogUtil {

	private static UI rootUI;
	
	private static PublishSubject<QuestionCommand> subject = PublishSubject.create();
	
	public static void showDialog(QuizCommandService quizCommandService) {
		CreateQuestionDialog dialog = new CreateQuestionDialog(quizCommandService);
		rootUI.addWindow(dialog);
	}

	public static void publish(QuestionCommand questionCommand) {
		subject.onNext(questionCommand);;
	}
	
	public static Observable<QuestionCommand> getObservable() {
		return subject.asObservable();
	}
	
	public static void setRootUI(UI ui) {
		rootUI = ui;
	}
	
}
