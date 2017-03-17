package main;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Singleton;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@ServerEndpoint(value = "/", configurator = MyConfigurator.class)
@Singleton
public class MediaServer {
	
	Map<String, Session> userSession = new HashMap<String, Session>();

	@OnOpen
	public void onOpen(Session session) {
		System.out.println(session.getId() + " has opened a connection");
		try {
			session.getBasicRemote().sendText("Connection Established");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void sendTo(Session session, JSONObject message) {
		try {
			if (session != null && message != null) {
				session.getBasicRemote().sendText(message.toJSONString());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		;
	}

	
	@OnMessage
	public void onMessage(String message, Session session)
			throws ParseException {
		System.out.println("Message from " + session.getId() + ": " + message);

		JSONParser parser = new JSONParser();
		JSONObject data = (JSONObject) parser.parse(message);
		String type = (String) data.get("type");
		String name = (String) data.get("name");
		Session otherSession;

		switch (type) {

		case "login":
			JSONObject obj = new JSONObject();
			if (userSession.containsValue(session)) {
				obj.put("type", "login");
				obj.put("success", "false");
				sendTo(session, obj);
			} else {

				userSession.put(name, session);
				HashMap<String, String> map = new HashMap<String, String>();
				session.getUserProperties().put("userName", name);
				obj.put("type", "login");
				obj.put("success", "true");
				sendTo(session, obj);
			}
			break;

		case "offer":

			otherSession = userSession.get(name);
			JSONObject obj1 = new JSONObject();
			obj1.put("type", "offer");
			obj1.put("offer", data.get("offer"));
			obj1.put("name", session.getUserProperties().get("userName"));
			sendTo(otherSession, obj1);
			break;

		case "answer":
			otherSession = userSession.get(name);
			JSONObject obj2 = new JSONObject();
			obj2.put("type", "answer");
			obj2.put("answer", data.get("answer"));
			sendTo(otherSession, obj2);
			break;

		case "candidate":
			otherSession = userSession.get(name);
			JSONObject obj3 = new JSONObject();
			obj3.put("type", "candidate");
			obj3.put("candidate", data.get("candidate"));
			sendTo(otherSession, obj3);
			break;

		}
	}

	
	@OnClose
	public void onClose(Session session) {
		System.out.println("Session " + session.getId() + " has ended");
	}
}
