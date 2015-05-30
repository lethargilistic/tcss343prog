package teamGuru;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
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
		Main z = new Main();

		int input[][] = z.readIn("input.txt");
		z.bruteForce(input);
		
		
		//prints the table
		int x;
		for(int i = 0; i < ARRAY_WIDTH; i++){
			for(int j = 0; j < ARRAY_WIDTH; j++){
				x = input[i][j];
				if(x < 0)
					System.out.print(x+" ");
				else
					System.out.print(" "+x+" ");
			}
			System.out.println();
		}
	}
	
	
	//feel free to make changes to this piece of crap
	/** @author Robbie */
	private int[][] readIn(String fileName) throws FileNotFoundException{
		Scanner in = new Scanner(new FileReader(fileName));
		int inArray[][], x = 0;
		List<String> lines = new LinkedList<String>();
		while(in.hasNextLine()){
			lines.add(in.nextLine());
			x++;
		}
		inArray = new int[x][x];
		ARRAY_WIDTH = x;
		Scanner strScan;
		for(int i = 0; i < x; i++){
			int j = 0;
			strScan = new Scanner(lines.get(i));
			while(strScan.hasNext()){
				String str = strScan.next();
				if(str.equals("NA"))//if the String is NA then set to sentinel -1
					inArray[i][j++] = -1;
				else 
					inArray[i][j++] = Integer.parseInt(str); //otherwise parse int and store.
			}
 			strScan.close();
		}
		in.close();
		return inArray;
	}
	
	/** @author Robbie */
	private void bruteForce(int input[][]){
		//only does computes 1 solution
		//should probably recursive
		int sum = 0, oldSum = 0;
		List<Point> sequence = new ArrayList<Point>(), oldSeq;
		
		for (int i = 0; i < ARRAY_WIDTH; i++){
			
			for(int j = 0; j < ARRAY_WIDTH; j++){
				
				if (input[i][j] > 0){//if the value is not bologna
					sum += input[i][j];
					sequence.add(new Point(i, j));
					i = j;
				}
			}
			if(sum < oldSum){ //if sum is less than oldSum then store it as the old value
				oldSum = sum;
				oldSeq = new ArrayList<Point>(sequence);
				sequence = new ArrayList<Point>();
			}
		}
		System.out.println(sum);
	}
}

