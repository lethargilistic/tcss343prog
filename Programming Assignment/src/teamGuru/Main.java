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
		String in = args[0];//gets the command line argument, which will be the name of the file to use
		Main z = new Main();
		int input[][];

		input = z.readIn("input.txt");
		z.printTable(input);
		z.bruteForce(input);
		
		//testing for brute force
		int cost[][] = {{0,2,3,7},{0,0,2,4},{0,0,0,2},{0,0,0,0}};
		System.out.println(z.aBruteForce(cost));
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
	
	private void printTable(int input[][]){
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
	
	/** @author Robbie */
	private void bruteForce(int input[][]){
		//only computes 1 solution
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
	
	//BRUTE FORCE SOLUTION & HELPER METHODS
	/** @author ian
	 *  Computes all possible solutions by using bitstrings.
	 */
	private int aBruteForce(int cost[][]) {
		int minCost = Integer.MAX_VALUE;
		for(int i=1; i<Math.pow(2, cost.length-1); i++) {
			//any even solutions never reach the last post
			if(i%2==0) {
				continue;
			}
			String bin = toBitString(i, cost.length-1);
			int solution = 0;
			int y = 0;
			for(int x=0; x<bin.length(); x++) {
				if(bin.charAt(x)=='1') {
					solution += cost[y][x+1];
					y = x+1;
				}
			}
			if(solution < minCost) {
				minCost = solution;
			}
		}
		return minCost;
	}
	
	//returns a bitString with leading 0's
	private String toBitString(int nummy, int size) {
		StringBuilder str = new StringBuilder();
		str.append(Integer.toBinaryString(nummy));
		while(str.length()<size) {
			str.insert(0, 0);
		}
		return str.toString();
	}
}

