package remote;

import java.io.Serializable;

/**
 * Class managing the tictactoe grid
 * 
 * @author rkm
 *
 */
public class Tictactoe implements Serializable {

	/**
	 * Multidimensional array representing the tictactoe grid
	 */
	private String[][] grid;

	/**
	 * Size of the grid
	 */
	private final int SIZE = 3;

	/**
	 * Indicates whether the grid contains a straight line
	 */
	private boolean containsStraightLine = false;

	/**
	 * Indicates whether the grid has a winner
	 */
	private boolean hasNoWinner = false;

	/**
	 * Default constructor
	 */
	public Tictactoe() {
		init();
	}

	/**
	 * Initialise the each cell of the grid to " "
	 */
	private void init() {
		grid = new String[SIZE][SIZE];
		int col = 0;
		for (int line = 0; line < SIZE; line++) {
			for (col = 0; col < SIZE; col++) {
				grid[line][col] = " ";
			}
		}
	}

	/**
	 * Prints to console the grid contents
	 */
	public void display() {

		System.out.println();
		System.out.println(" --- --- --- ");

		int col = 0;

		for (int line = 0; line < SIZE; line++) {
			System.out.print("| ");
			for (col = 0; col < SIZE; col++) {
				System.out.print(grid[line][col] + " | ");
			}
			System.out.println();
			System.out.println(" --- --- --- ");
		}

	}

	/**
	 * Mark a cell of the grid at the given line and column
	 * 
	 * @param line    index of the line in the grid
	 * @param col     index of the column in the grid
	 * @param symbole symbole used to mark to the cell at the position [line,col]
	 */
	public void mark(int line, int col, String symbole) {

		grid[line][col] = symbole;

		if (constainsStraightLine(symbole)) {
			containsStraightLine = true;
		}

		if (gridIsFull()) {
			hasNoWinner = true;
		}
	}

	/**
	 * Returns a boolean indicating whether the cell at the position [line,col] is
	 * marked
	 * 
	 * @param line index of the line in the grid
	 * @param col  index of the column in the grid
	 * @return true is cell at position [line,col] is marked, false otherwise
	 */
	public boolean isMarked(int line, int col) {
		return !grid[line][col].equals(" ");
	}

	/***
	 * Returns a boolean indicating whether grid is full
	 * 
	 * @return true if all cells containsa symbole, false otherwise
	 */
	public boolean gridIsFull() {

		int line = 0;
		int col = 0;
		boolean full = true;

		while (line < SIZE) {
			while (col < SIZE) {
				if (grid[line][col].equals(" ")) {
					full = false;
					break;
				}
				col++;
			}
			col = 0;
			line++;
		}

		return full;
	}

	/**
	 * Returns a boolean indicating in advance if the current disposition of symbole
	 * is a no winner case
	 * 
	 * @return true if contains a no winner case, false otherwise
	 */
	public boolean hasNoWinnerCase() {
		// TODO : detect in advance if case has no winner
		return false;
	}

	/**
	 * Returns a boolean indicating whether the game has no winner
	 * 
	 * @return true if there's no winner, false otherwise
	 */
	public boolean hasNoWinner() {
		return hasNoWinner;
	}

	/**
	 * Returns the boolean indicating whether the grid contains a straight line
	 * 
	 * @return containsStraightLine
	 */
	public boolean getContainsStraightLine() {
		return containsStraightLine;
	}

	/**
	 * Checks whether the grid contains a straight line of symbole
	 * 
	 * @param symbole symbole used by the player
	 * @return true if contains a straight line, false otherwise
	 */
	public boolean constainsStraightLine(String symbole) {
		return (containsHorizontalLine(symbole) || containsVerticalLine(symbole) || containsDiagonalLine(symbole));
	}

	/**
	 * Checks whether the grid contains a horizontal line of symbole
	 * 
	 * @param symbole symbole used by the player
	 * @return if contains a horizontal straight line, false otherwise
	 */
	public boolean containsHorizontalLine(String symbole) {

		boolean containsLine = false;
		int line = 0;
		int counter = 0;

		// Go through each line
		while (line < SIZE) {

			// Increment the counter for each col in this line containing the symbole
			for (int col = 0; col < SIZE; col++) {
				if (grid[line][col].equals(symbole)) {
					counter++;
				}
			}

			// If the counter is equal to SIZE then we have a straight line
			if (counter == 3) {
				containsLine = true;
				break;
			}

			counter = 0;
			line++;
		}

		return containsLine;
	}

	/**
	 * Checks whether the grid contains a vertical line of symbole
	 * 
	 * @param symbole symbole used by the player
	 * @return if contains a vertical straight line, false otherwise
	 */
	public boolean containsVerticalLine(String symbole) {

		boolean containsLine = false;
		int col = 0;
		int counter = 0;

		// Go through each line
		while (col < SIZE) {

			// Increment the counter for each col in this line containing the symbole
			for (int line = 0; line < SIZE; line++) {
				if (grid[line][col].equals(symbole)) {
					counter++;
				}
			}

			// If the counter is equal to SIZE then we have a straight line
			if (counter == 3) {
				containsLine = true;
				break;
			}

			counter = 0;
			col++;
		}

		return containsLine;
	}

	/**
	 * Checks whether the grid contains a diagonal line of symbole
	 * 
	 * @param symbole symbole used by the player
	 * @return if contains a diagonal straight line, false otherwise
	 */
	public boolean containsDiagonalLine(String symbole) {

		boolean containsLine = false;
		int counter = 0;

		// Check top left to bottom right diagonal
		for (int index = 0; index < SIZE; index++) {
			if (grid[index][index].equals(symbole)) {
				counter++;
			}
		}
		if (counter == SIZE) {
			containsLine = true;
		}
		// If no line found for TL to BR, check in TR to BL
		else {
			counter = 0;
			for (int index = SIZE - 1; index > 0; index--) {
				if (grid[index][index].equals(symbole)) {
					counter++;
				}
			}
		}

		if (counter == SIZE) {
			containsLine = true;
		}

		return containsLine;
	}

}
