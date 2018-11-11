package com.omnitutor.application;

import org.springframework.beans.factory.annotation.Autowired;

import com.omnitutor.application.domain.query.UserQuery;
import com.omnitutor.application.pages.CourseFormPage;
import com.omnitutor.application.pages.CourseOverviewPage;
import com.omnitutor.application.pages.CoursePage;
import com.omnitutor.application.pages.CoursesPage;
import com.omnitutor.application.pages.CreateQuizPage;
import com.omnitutor.application.pages.LandingPage;
import com.omnitutor.application.pages.LoginPage;
import com.omnitutor.application.pages.MyCoursesPage;
import com.omnitutor.application.pages.ProfilePage;
import com.omnitutor.application.pages.QuizPage;
import com.omnitutor.application.pages.RegisterPage;
import com.omnitutor.application.pages.VideoPage;
import com.omnitutor.application.server.push.Broadcaster;
import com.omnitutor.application.server.push.MessageDistributionService;
import com.omnitutor.application.util.CreateLectureDialogUtil;
import com.omnitutor.application.util.CreateQuestionDialogUtil;
import com.omnitutor.application.util.ErrorMessageUtil;
import com.omnitutor.application.util.MailDialogUtil;
import com.omnitutor.application.util.NavigatorUtil;
import com.omnitutor.application.util.RateCourseDialogUtil;
import com.omnitutor.application.util.RouteUtil;
import com.omnitutor.application.util.SessionService;
import com.omnitutor.application.util.SessionUtil;
import com.omnitutor.application.util.SuccessMessageUtil;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

@Push
@Theme("omnitutor-theme")
@SpringUI
public class RootUI extends UI implements Broadcaster.BroadcastListener {

	private static final long serialVersionUID = 1L;

	@Autowired
	LandingPage landingPage;

	@Autowired
	LoginPage loginPage;

	@Autowired
	RegisterPage registerPage;

	@Autowired
	CoursesPage coursesPage;

	@Autowired
	CoursePage coursePage;

	@Autowired
	ProfilePage profilePage;

	@Autowired
	CourseOverviewPage courseOverview;

	@Autowired
	VideoPage videoPage;

	@Autowired
	MyCoursesPage myCoursesPage;

	@Autowired
	CourseFormPage courseFormPage;

	@Autowired
	CreateQuizPage createQuizPage;

	@Autowired
	QuizPage quizPage;

	@Autowired
	NavigatorUtil navigator;

	@Autowired
	SessionService session;

	@Autowired
	MessageDistributionService messageDistributionService;

	@Override
	protected void init(VaadinRequest request) {
		setTheme("omnitutor-theme");
		setPageTitle("OmniTutor");
		Responsive.makeResponsive(this);
		initNavigatorService();
		addRoutes();
		initUtilWindows();
		setSessionTimeout();
		registerToServerBroadcaster();
	}

	@Override
	public void detach() {
		Broadcaster.unregister(this);
		super.detach();
	}

	private void registerToServerBroadcaster() {
		Broadcaster.register(this);
	}

	private void setSessionTimeout() {
		VaadinSession.getCurrent().getSession().setMaxInactiveInterval(SessionUtil.TIMEOUT);
	}

	private void initUtilWindows() {
		ErrorMessageUtil.setRootUI(this);
		SuccessMessageUtil.setRootUI(this);
		RateCourseDialogUtil.setRootUI(this);
		CreateLectureDialogUtil.setRootUI(this);
		CreateQuestionDialogUtil.setRootUI(this);
		MailDialogUtil.setRootUI(this);
	}

	private void initNavigatorService() {
		navigator.init(this, this);
	}

	private void setPageTitle(String title) {
		getPage().setTitle(title);
	}

	private void addRoutes() {
		navigator.addRoute(RouteUtil.ROOT, landingPage);
		navigator.addRoute(RouteUtil.LOGIN, loginPage);
		navigator.addRoute(RouteUtil.REGISTER, registerPage);
		navigator.addRoute(RouteUtil.COURSES, coursesPage);
		navigator.addRoute(RouteUtil.COURSE, coursePage);
		navigator.addRoute(RouteUtil.PROFILE, profilePage);
		navigator.addRoute(RouteUtil.COURSE_OVERVIEW, courseOverview);
		navigator.addRoute(RouteUtil.VIDEO, videoPage);
		navigator.addRoute(RouteUtil.MY_COURSES, myCoursesPage);
		navigator.addRoute(RouteUtil.COURSE_FORM, courseFormPage);
		navigator.addRoute(RouteUtil.CREATE_QUIZ_PAGE, createQuizPage);
		navigator.addRoute(RouteUtil.QUIZ, quizPage);
	}

	@Override
	public void receiveBroadcast(String message) {
		access(new Runnable() {
			@Override
			public void run() {
				messageDistributionService.broadcastMessage(message);
			}
		});
	}

	@Override
	public void showNotification(String notification, UserQuery sender, UserQuery reciever) {
		if (isNotificationForUser(sender, reciever))
			new Notification("Notification", notification, Notification.Type.HUMANIZED_MESSAGE, true)
					.show(getPage());
	}

	private boolean isNotificationForUser(UserQuery sender, UserQuery reciever) {
		return reciever.getId().longValue() == session.getUser().getId().longValue() &&
				sender.getId().longValue() != reciever.getId().longValue();
	}

}
