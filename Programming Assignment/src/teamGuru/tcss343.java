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

	private static int GRAPH_SIZE = 4;
	
	public static void main(String[] args) throws FileNotFoundException{
		tcss343 z = new tcss343();

		//int[][] input = z.readIn();
		
		//testing with sample data
		int cost[][] = {{0,2,3,7},{0,0,2,4},{0,0,0,2},{0,0,0,0}};
		//int cost[][] = z.generateMatrix(GRAPH_SIZE);
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
		//retrieve data from String
		Pair solution = aDivideandConquer(cost, cost.length-1, cost.length-1);
		String path = solution.getPath().substring(0, solution.getPath().lastIndexOf(","));;
		return "Minimum Cost: " + solution.getCost() + " Path: " + path;
	}
	
	/**
	 * @author Ian
	 * Finds the minimum cost and path through recursion.
	 * @param cost
	 * @param x
	 * @param y
	 * @return the minimum cost and it's path
	 */
	private Pair aDivideandConquer(int cost[][], int x, int y) {
		if (x == 0) {
			return new Pair("1, " + (y+1) + ", ", cost[0][y]);
		} else if (x < y) {
			Pair solution = aDivideandConquer(cost, x, x);
			return new Pair(solution.getPath() + (y+1) + ", ",
					solution.getCost() + cost[x][y]);
		} else {
			Pair winningSolution = new Pair("", Integer.MAX_VALUE);
			for (int i = 0; i < y; i++) {
				//retrieve minimum cost from solution
				Pair solution = aDivideandConquer(cost, i, y);
				if (solution.getCost() < winningSolution.getCost()) {
					winningSolution = solution;
				}
			}
			return winningSolution;
		}
	}

    /*************************************************
     * DYNAMIC PROGRAMMING SOLUTION & HELPER METHODS *
     *************************************************/
	
	public String dynamicProgramming(int cost[][]) {
		Pair []minSolutions = new Pair[cost.length];
		minSolutions[0] = new Pair("1, ", 0);
		minSolutions[1] = new Pair("1, 2, ", cost[0][1]);

		for(int i=2; i<cost.length; i++) {
			int minnie = cost[0][i];
			String pathos = "1, " + (i+1) + ", ";
			for(int j=1; j<i; j++) {
				int currCost = minSolutions[j].getCost() + cost[j][i];
				if(currCost < minnie) {
					minnie = currCost;
					pathos = minSolutions[j].getPath() + (i+1) + ", ";
				}
			}
			minSolutions[i] = new Pair(pathos, minnie);
		}

		return "Minimum Cost: " + minSolutions[cost.length-1].getCost() + " Path: " + 
			minSolutions[cost.length-1].getPath().substring(0, minSolutions[cost.length-1].getPath().lastIndexOf(","));
	}
	
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
    	
    	@Override
    	public String toString() {
    		return path + " : " + cost;
    	}
    }
}
