package fr.bibiobscur.skyblock;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

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
	
	public String getChallengeList() {
		
		String tab[] = new String[challenges.size()];
		int i = 0;
		
		Iterator<String> it = challenges.iterator();
		while(it.hasNext()) {
			tab[i] = it.next();
			i ++;
		}
		
		Arrays.sort(tab);
		
		String challengelist = "";
		for(i = 0; i < tab.length; i++) {
			challengelist += "§a" + tab[i] + "§f, ";
		}
		
		return challengelist;
	}
	
	/*private void echanger(String tableau[], int a, int b)
	{
	    String temp = tableau[a];
	    tableau[a] = tableau[b];
	    tableau[b] = temp;
	}

	private void quickSort(String tableau[], int debut, int fin)
	{
	    int gauche = debut-1;
	    int droite = fin+1;
	    String pivot = tableau[debut];

	    if(debut >= fin)
	        return;

	    while(true)
	    {
	        do droite--; while(tableau[droite] > pivot);
	        do gauche++; while(tableau[gauche] < pivot);

	        if(gauche < droite)
	            echanger(tableau, gauche, droite);
	        else break;
	    }

	    quickSort(tableau, debut, droite);
	    quickSort(tableau, droite+1, fin);
	}*/
	
}