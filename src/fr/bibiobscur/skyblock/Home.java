package fr.bibiobscur.skyblock;

import java.io.Serializable;
import java.util.Map;

public class Home implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int x;
	private int y;
	private int z;
	private Map<String, Object> direction;
	
	public int getX() { return x; }
	public int getY() { return y; }
	public int getZ() { return z; }
	public Map<String, Object> getDirection() { return direction; }
	
	public void setX(int x) { this.x = x; }
	public void setY(int y) { this.y = y; }
	public void setZ(int z) { this.z = z; }
	public void setDirection(Map<String, Object> direction) { this.direction = direction; }
	
}