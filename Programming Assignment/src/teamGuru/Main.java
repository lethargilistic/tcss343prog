package teamGuru;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/*
 * Authors: Robbie Nichols, Mike Overby, Ian McPeek, Jeffrey LeCompte
 * Team: Team Guru
 * Assignment: Programming Assignment
 * Class: TCSS 343 Spring 2015
 */

public class Main {
	private static int ARRAY_WIDTH;
	
	public static void main(String[] args) throws FileNotFoundException{
		int input[][] = readIn("input.txt");
	}
	
	private static int[][] readIn(String fileName) throws FileNotFoundException{
		Scanner in = new Scanner(new FileReader(fileName));
		int inArray[][], x = 0;
		List<String> lines = new LinkedList<String>();
		while(in.hasNextLine()){
			lines.add(in.nextLine());
			x++;
		}
		inArray = new int[x][x];
		ARRAY_WIDTH = x;
		Scanner strRd;
		for(int i = 0; i < x; i++){
			int j = 0;
			strRd = new Scanner(lines.get(i));
			while(strRd.hasNext()){
				String str = strRd.next();
				if(str.equals("NA"))//if the String is NA then set to sentinel -1
					inArray[i][j++] = -1;
				else 
					inArray[i][j++] = Integer.parseInt(str); //otherwise parse int and store.
			}
 			strRd.close();
		}
		in.close();
		return inArray;
	}
}

