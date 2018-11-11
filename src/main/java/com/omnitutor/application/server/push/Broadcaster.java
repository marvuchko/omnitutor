package com.omnitutor.application.server.push;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.omnitutor.application.domain.query.UserQuery;

public class Broadcaster implements Serializable {

	private static final long serialVersionUID = 1L;

	static ExecutorService executorService = Executors.newSingleThreadExecutor();

	public interface BroadcastListener {
		
		void receiveBroadcast(String message);
		
		void showNotification(String notification, UserQuery sender, UserQuery reciever);
		
	}

	private static LinkedList<BroadcastListener> listeners = new LinkedList<BroadcastListener>();

	public static synchronized void register(BroadcastListener listener) {
		listeners.add(listener);
	}

	public static synchronized void unregister(BroadcastListener listener) {
		listeners.remove(listener);
	}

	public static synchronized void broadcast(final String message) {
		listeners.forEach(listener -> {
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					listener.receiveBroadcast(message);
				}
			});
		});
	}
	
	public static synchronized void broadcast(final String notification, UserQuery sender, UserQuery reciever) {
		listeners.forEach(listener -> {
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					listener.showNotification(notification, sender, reciever);
				}
			});
		});
			
	}
}
