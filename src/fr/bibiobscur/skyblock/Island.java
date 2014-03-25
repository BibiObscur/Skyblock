package fr.bibiobscur.skyblock;

import java.io.Serializable;
import java.util.HashSet;

public class Island implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int x;
	private int z;
	private int level;
	private HashSet<String> challenges = new HashSet<String>();
	
	public int getLevel() { return level; }
	public int getX() { return x; }
	public int getZ() { return z; }
	
	
	public void setLevel(int level) { this.level = level; }
	public void setX(int x) { this.x = x; }
	public void setZ(int z) { this.z = z; }
	
	public HashSet<String> getChallenges() { return challenges; }
	
}