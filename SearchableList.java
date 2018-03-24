/****************************************************************************
 *
 * Created by: Matthew Lourenco
 * Created on: Mar 2018
 * This program creates a list of 250 values to test a class that preforms 
 *     sort and binary search methods.
 *
 ****************************************************************************/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SearchableList {
	
	public ArrayList<Integer> unsortedList = new ArrayList<Integer>();
	public ArrayList<Integer> sortedList = new ArrayList<Integer>();
	private int _min;
	private int _max;
	private int _length;
	Random rand = new Random();
	
	public SearchableList() {
		//Default constructor
		this(250, 0, 500);
	}
	
	public SearchableList(int length, int min, int max) {
		//Contructor for setting the size, upper, and lower bounds
		this._length = length;		
		this._min = min;
		this._max = max;
		
		this.newList();
	}
	
	public void newList() {
		//Creates a random list and returns it in string format. The
		//    random list is assigned to the unsortedlist field.
		
		for(int index=0; index<_length; index++) {
			unsortedList.add(rand.nextInt(_max + 1 - _min) + _min);
		}
		this.sort();
	}
	
	public void sort() {
		//Sorts the unsorted list and assigns the result to the sorted list
		@SuppressWarnings("unchecked")
		ArrayList<Integer> tempList = (ArrayList<Integer>) unsortedList.clone();
		Integer smallestNumber = 0;
		for(int repeat = 0 ; repeat<tempList.size() ; ) {
			
			smallestNumber = tempList.get(0);
			for(int number: tempList) {
				smallestNumber = smallestNumber > number ? number : smallestNumber;
			}
			
			sortedList.add(smallestNumber);
			tempList.remove(smallestNumber);
		}
	}
	
	public int searchBinary(int item, Boolean findClosest) {
		//Uses a binary search to find the inputted item. If the item is not
		//    in the list it returns -1. If findClosest is true it returns the
		//    index of the closest number to the inputted item.
		
		Double position = (double) (sortedList.size() / 2);
		ArrayList<Integer> oldPositions = new ArrayList<Integer>();
		int index = -2;
		int repeats = 1;
		int maxRecursiveChecks = 4;
		
		while(index == -2) {
			
			//Allow the program to end the loop if it has to recheck
			//    the same value multiple times.
			if(oldPositions.size() >= maxRecursiveChecks) {
				if(oldPositions.get(oldPositions.size() - 1) == 
						oldPositions.get(oldPositions.size() - maxRecursiveChecks)) {
					index = findClosest ? (int) Math.round(position) : -1;
				}
			}
			
			if((int) Math.round(position) >= sortedList.size()) {
				//Break loop if search resorts to checking outside of list
				index = findClosest ? (int) Math.round(position) : -1;
				break;
			}
			
			if(sortedList.get((int) Math.round(position)) == item) {
				index = (int) Math.round(position);
			} else if(sortedList.get((int) Math.round(position)) > item) {
				oldPositions.add((int) Math.round(position));
				position -= Math.pow(0.5, ++repeats) * sortedList.size();
			} else {
				oldPositions.add((int) Math.round(position));
				position += Math.pow(0.5, ++repeats) * sortedList.size();
			}
		}
		
		return index;
	}
	
	public void add(Integer number) {
		//Adds a number to the correct place in the sorted list.
		//Also adds the number to the end of the unsorted list.
		
		unsortedList.add(number);
		int index = this.searchBinary(number, true);
		
		//correct index for staying within the range of the list
		if(index >= sortedList.size()) {
			sortedList.add(number);
		} else {
			if(number > sortedList.get(index)) {
				//Correct index for the closest item in sortedList that is
				//    larger than 'number'
				sortedList.add(++index, number);
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		//Generate a SearchableList and allow the user to add a number and
		//    search for a number.
		BufferedReader reader = new BufferedReader(new InputStreamReader
				(System.in));
		
		SearchableList list = new SearchableList();
		System.out.println(list.unsortedList.toString());
		System.out.println(list.sortedList.toString());
		
		System.out.println("Which number would you like to add to the list?");
		String input1 = reader.readLine();
		Integer numberToAdd = Integer.parseInt(input1);
		list.add(numberToAdd);
		System.out.println(list.sortedList.toString());
		
		System.out.println("Which number would you like to search for?");
		String input2 = reader.readLine();
		Integer numberToSearch = Integer.parseInt(input2);
		int index = list.searchBinary(numberToSearch, false);
		if(index != -1) {
			System.out.println(numberToSearch + " is located at index = " + index);
		} else {
			System.out.println(numberToSearch + " is not located in the list");
		}
	}
}