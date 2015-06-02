package teamGuru;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

/*
 * Authors: Robbie Nichols, Mike Overby, Ian McPeek, Jeffrey LeCompte
 * Team: Team Guru
 * Assignment: Programming Assignment
 * Class: TCSS 343 Spring 2015
 */

public class tcss343 {
	private static int ARRAY_WIDTH;
	private static int GRAPH_SIZE = 100;
	
	public static void main(String[] args) throws FileNotFoundException{
		tcss343 z = new tcss343();

		//int[][] input = z.readIn();
		
		//testing with sample data
		//int cost[][] = {{0,2,3,7},{0,0,2,4},{0,0,0,2},{0,0,0,0}};
		int cost[][] = z.generateMatrix(GRAPH_SIZE);
		for(int[]arr:cost) {
			System.out.println(Arrays.toString(arr));
		}
		
        long time = System.currentTimeMillis();
		//System.out.println("Dynamic Programming~ \t"+ z.aDynamicProgramming(cost));
        System.out.println("Dynamic Programming~ \t"+ z.dynamicProgramming(cost));
        System.out.println("Time elapsed: " + (System.currentTimeMillis() - time) + " ms");
	    System.out.println();
	    
        time = System.currentTimeMillis();
        System.out.println("Divide & Conquer~ \t"+ z.aDivideandConquer(cost));
        System.out.println("Time elapsed: " + (System.currentTimeMillis() - time) + " ms");
        System.out.println();
        
        time = System.currentTimeMillis();
        System.out.println("Brute Force~ \t\t"+ z.aBruteForce(cost));
        System.out.println("Time elapsed: " + (System.currentTimeMillis() - time) + " ms");
        System.out.println();

	}
	
	/****************
     * FILE READING *
     ****************/
	
	/** 
	 * @author Robbie 
	 * 
	 * This method reads a tab delineated sample file -- NOT A .CSV
	 */
//	private int[][] readIn(String fileName) throws FileNotFoundException{
	private int[][] readIn(){
//		Scanner in = new Scanner(new FileReader(fileName));
		Scanner in = new Scanner(System.in);
		
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
	
    /*****************************************
     * BRUTE FORCE SOLUTION & HELPER METHODS *
     *****************************************/
	
	/** @author ian
	 *  Computes all possible solutions by using bitstrings.
	 *  @return a String containing the minimum cost and path.
	 */
	public String aBruteForce(int cost[][]) {
		int minCost = Integer.MAX_VALUE;
		String winningSolution = "";
		for(int i=1; i<Math.pow(2, cost.length-1); i+=2 /*Even solutions never complete*/) {
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
						solutionString.lastIndexOf(","));
			}
		}
		//convert winningSolution to 
		return "Minimum Cost: " + minCost + " Path: " + winningSolution;
	}
	
	//returns a bitString with leading 0's
	private String toBitString(int nummy, int size) {

		//System.out.println("Comparing solution #" + nummy);
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
	//will do a full write-up of how it works for presentations
	//but it's pretty close to the self-reduction mentioned in the
	//DivideConquer.txt. file.
	// Looks pretty good :) - Jef
	private String aDivideandConquer(int cost[][]) {
		//retrive data from String
		String solution = aDivideandConquer(cost, cost.length-1, cost.length-1);
		int minCost = retrieveCost(solution);
		solution = solution.substring(0, solution.lastIndexOf(","));;
		return "Minimum Cost: " + minCost + " Path: " + solution;
	}
	
	/**
	 * @author Ian
	 * Finds the minimum cost and path through recursion.
	 * @param cost
	 * @param x
	 * @param y
	 * @return the minimum cost and it's path
	 */
	private String aDivideandConquer(int cost[][], int x, int y) {
		if (x == 0) {
			return "1, " + (y+1) + ", :" + cost[0][y];
		} else if (x < y) {
			//clip off the cost from path "1,2,n...,:c"
			String path = aDivideandConquer(cost, x, x);
			int theCost = retrieveCost(path) + cost[x][y];
			path = path.substring(0, path.indexOf(":"));
			return path + (y+1) + ", :" + theCost;
		} else {
			String winningSolution = "";
			int minCost = Integer.MAX_VALUE;
			for (int i = 0; i < y; i++) {
				//retrieve minimum cost from solution
				String solution = aDivideandConquer(cost, i, y);
				int solutionCost = retrieveCost(solution)+cost[x][y];
				solution = solution.substring(0, solution.indexOf(":"));
				if (solutionCost < minCost) {
					minCost = solutionCost;
					winningSolution = solution+ ":" + solutionCost;
				}
			}
			return winningSolution;
		}
	}
	
	private int retrieveCost(String path) {
		String costS = path.substring(path.indexOf(":")+1, 
				path.length());
		return Integer.parseInt(costS);
	}

    /*************************************************
     * DYNAMIC PROGRAMMING SOLUTION & HELPER METHODS *
     *************************************************/
	
	public String dynamicProgramming(int cost[][]) {
		int []minCosts = new int[cost.length];
		minCosts[0] = 0;
		minCosts[1] = cost[0][1];

		for(int i=2; i<cost.length; i++) {
			int minnie = cost[0][i];
			for(int j=1; j<i; j++) {
				int currCost = minCosts[j] + cost[j][i];
				if(currCost < minnie) {
					minnie = currCost;
				}
			}
			minCosts[i] = minnie;
		}

		return "Cost: " + minCosts[cost.length-1];
	}
	
	
	/*
	 * 	Pair []minSolutions = new Pair[cost.length];
	//minCosts[0] = 0;
	minSolutions[0] = new Pair("1,", 0);
	//minCosts[1] = cost[0][1];
	minSolutions[1] = new Pair("1,2,", cost[0][1]);

	for(int i=2; i<cost.length; i++) {
		int minnie = cost[0][i];
		String pathos = "1," + i + ",";
		for(int j=1; j<i; j++) {
			int currCost = minSolutions[j].getCost() + cost[j][i];
				//minCosts[j] + cost[j][i];
			if(currCost < minnie) {
				minnie = currCost;
				pathos = minSolutions[j].getPath() + i + ",";
			}
		}
		//minCosts[i] = minnie;
		minSolutions[i] = new Pair(pathos, minnie);
	}

	//return "Cost: " + minCosts[cost.length-1];
	return "Path: " + minSolutions[cost.length-1].getCost() + "Path: " + minSolutions[cost.length-1].getPath();
}

private class Pair {
	String path;
	int cost;

	public Pair(String apath, int acost) {
		path = apath;
		cost = acost;
	}

	public String getPath() {
		return path;
	}

	public int getCost() {
		return cost;
	}
	 */
	
	
    /*************************************************
     * COST MATRIX RANDOM GENERATER & HELPER METHODS *
     *************************************************/
    
    /**
     * @author ian
     * @param n
     * @return
     */
    private int[][] generateMatrix(int n) {
        int [][]arr = new int[n][n];
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                if(j>i) {
                    arr[i][j] = (int)(Math.random()*9) +1;
                }
            }
        }
        return arr;
    }
}
