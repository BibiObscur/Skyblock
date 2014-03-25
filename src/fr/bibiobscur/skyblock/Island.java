package fr.bibiobscur.skyblock;

import java.io.Serializable;
import java.util.HashSet;

public class Island implements Serializable {
	private static final long serialVersionUID = 1L;
	public int x;
	public int z;
	private int level;
	public HashSet<String> challenges = new HashSet<String>();
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public boolean containsChallenge(String challengename) {System.out.println("[TEST]"); return challenges.contains(challengename);}
}