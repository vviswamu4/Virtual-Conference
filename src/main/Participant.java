package main;

import javax.websocket.Session;

public class Participant {
	
	private String name;
	private Session session;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}

}
