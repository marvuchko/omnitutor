package com.omnitutor.application.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GmailUtil {

	private static final String username = "omnitutor.site@gmail.com";

	private static final String password = "tutor1234";
	
	public static void sendMail(String recipientEmail, String title, String message) {
		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("omnitutor.site@gmail.com"));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
			msg.setSubject(title);
			msg.setText(message);
			Transport.send(msg);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

}
