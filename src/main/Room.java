package main;

import java.util.ArrayList;
import java.util.List;

public class Room {
	
	private List<Participant> participants = new ArrayList<Participant>();

	public List<Participant> getParticipants() {
		return participants;
	}

	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
	}
	
}
