package teamGuru;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

/*
 * Authors: Robbie Nichols, Mike Overby, Ian McPeek, Jeffrey LeCompte
 * Team: Team Guru
 * Assignment: Programming Assignment
 * Class: TCSS 343 Spring 2015
 */

public class Main {
	private static int ARRAY_WIDTH;
	
	public static void main(String[] args) throws FileNotFoundException{
//		String in = args[0];//gets the command line argument, which will be the name of the file to use
		Main z = new Main();
//		int input[][];

		int[][] input = z.readIn("src/testInput.csv");
		
//		input = z.readIn("input.txt");
//		z.printTable(input);
//		z.bruteForce(input);
		
		//testing for brute force
		int cost[][] = {{0,2,3,7},{0,0,2,4},{0,0,0,2},{0,0,0,0}};
		System.out.println(z.aBruteForce(input));
		int x = 0, y = 0; // I'm not sure if any value between 0-4 work? - Jef
		System.out.println(z.aDivideandConquer(cost, x, y));
	}
	
	
	/****************
     * FILE READING *
     ****************/
    
	
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
				for (String s : str.split(","))
				{
    				if(s.equals("NA"))//if the String is NA then set to sentinel -1
    					inArray[i][j++] = -1;
    				else
    					inArray[i][j++] = Integer.parseInt(s); //otherwise parse int and store.
				}
			}
 			strScan.close();
		}
		in.close();
		return inArray;
	}
	
	/** @author ian 
	 *  Reads in 2D array from file.
	 */
	private int[][] readData(String filename) {
		ArrayList<ArrayList<Integer>> arrO = 
				new ArrayList<ArrayList<Integer>>();
		InputStream in = this.getClass().getResourceAsStream(filename);
		//abort if no file found
		if(in==null) {return null;}
		Scanner scanner = new Scanner(in);
        while(scanner.hasNextLine()) {
        	StringTokenizer token = new StringTokenizer(scanner.nextLine(), ",");
        	ArrayList<Integer> arrI = new ArrayList<Integer>();
        	while(token.hasMoreTokens()) {
        		arrI.add(Integer.parseInt(token.nextToken()));
        	}
        	arrO.add(arrI);
        }
        //close open streams
        scanner.close();
        try {in.close();
		} catch (IOException e) {
			e.printStackTrace();}
        //craft 2D array from Double-Decker ArrayList
		int [][]inArray = new int[arrO.size()][arrO.size()];
		for(int i=0; i<arrO.size(); i++) {
			for(int j=0; j<arrO.size(); j++) {
				inArray[i][j] = arrO.get(i).get(j);
			}
		}
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
	

    /*****************************************
     * BRUTE FORCE SOLUTION & HELPER METHODS *
     *****************************************/
	
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
	
	/** @author ian
	 *  Computes all possible solutions by using bitstrings.
	 *  @return a String containing the minimum cost and path.
	 */
	private String aBruteForce(int cost[][]) {
		int minCost = Integer.MAX_VALUE;
		String winningSolution = "";
		for(int i=1; i<Math.pow(2, cost.length-1); i++) {
			//any even solutions never reach the last post
			if(i%2==0) {
				continue;
			}
			String bin = toBitString(i, cost.length-1);
			int solution = 0;
			StringBuilder solutionString = new StringBuilder("1, ");
			int y = 0;
			for(int x = 0; x < bin.length(); x++) {
				if(bin.charAt(x) == '1') {
					solution += cost[y][x + 1];
					solutionString.append(x + 2);
					solutionString.append(", ");
					y = x + 1;
				}
			}
			if(solution < minCost) {
				minCost = solution;
				winningSolution = solutionString.substring(0, 
						solutionString.length() - 1);
			}
		}
		//convert winningSolution to 
		return "Minimum Cost: " + minCost + " Path: " + winningSolution;
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
	
    /************************************************
     * DIVIDE AND CONQUER SOLUTION & HELPER METHODS *
     ************************************************/
	
	//Not sure if this is considered a 'divide' and conquer or
	// 'decrease' and conquer but its a good starting point.
	//Still needs testing.
	// Looks pretty good :) I made a stupid mistake
	/**
	 * @author Ian
	 * Finds the minimum cost and path through recursion.
	 * @param cost
	 * @param x
	 * @param y
	 * @return the minimum cost and it's path
	 */
	private String aDivideandConquer(int cost[][], int x, int y) {
		if (x == 1) {
			return cost[1][y] + ",";
		} else if (x < y) {
			return aDivideandConquer(cost, x, x) + cost[x][y] + ",";
		} else {
			String winningSolution = "";
			int minCost = Integer.MAX_VALUE;
			for (int i = 1; i < y - 1; i++) {
				int solutionCost = 0;
				String solution = aDivideandConquer(cost, i, y);
				//retrieve minimum cost from solution
				//solutionCost = retrieve(solution);
				if (solutionCost < minCost) {
					minCost = solutionCost;
					winningSolution = solution;
				}
			}
			return "Minimum Cost: " + minCost + " Path: " + winningSolution;
		}
	}

    /*************************************************
     * DYNAMIC PROGRAMMING SOLUTION & HELPER METHODS *
     *************************************************/
	
}
