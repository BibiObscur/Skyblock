package fr.bibiobscur.skyblock.group;

import java.io.Serializable;
import java.util.HashSet;

public class Group implements Serializable {
	private static final long serialVersionUID = 1L;
	private String leader;
	private HashSet<String> members = new HashSet<String>();

	public String getLeader() {
		return leader;
	}
	
	public void setLeader(String leader) {
		this.leader = leader;
	}
	
	public HashSet<String> getMembers() {
		return members;
	}
}
