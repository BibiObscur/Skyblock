package fr.bibiobscur.skyblock;

import java.io.Serializable;
import java.util.HashSet;

public class Island implements Serializable {
	/**
	 * change serialUID if we add anything besides x and z coordinates
	 */
	private static final long serialVersionUID = 1L;
	public int x;
	public int z;
	public int level;
	public HashSet<String> challenges = new HashSet<String>();
	
	public boolean containsChallenge(String challengename) {System.out.println("[TEST]"); return challenges.contains(challengename);}
}