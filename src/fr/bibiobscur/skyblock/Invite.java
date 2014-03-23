package fr.bibiobscur.skyblock;

public class Invite {
	private String inviting;
	private String invited;
	
	public Invite(String inviting, String invited) {
		this.inviting = inviting;
		this.invited = invited;
	}
	
	public String getInviting() {
		return inviting;
	}
	
	public String getInvited() {
		return invited;
	}
}
