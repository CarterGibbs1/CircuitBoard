import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Represents a 2D circuit board as read from an input file.
 *  
 * @author mvail
 */
public class CircuitBoard {
	/** current contents of the board */
	private char[][] board;
	/** location of row,col for '1' */
	private Point startingPoint;
	/** location of row,col for '2' */
	private Point endingPoint;

	//constants you may find useful
	private final int ROWS; //initialized in constructor
	private final int COLS; //initialized in constructor
	private final char OPEN = 'O'; //capital 'o'
	//private final char CLOSED = 'X';
	private final char TRACE = 'T';
	private final char START = '1';
	private final char END = '2';
	private final String ALLOWED_CHARS = "OXT12";

	private int rows, columns; // assumed rows and columns of a file's circuit board
	private char[] allowChars = ALLOWED_CHARS.toCharArray(); // array of allowed characters
	
	private boolean foundStart = false; // if the start has been found
	private boolean foundEnd = false; // if the end has been found.
	
	/** Construct a CircuitBoard from a given board input file, where the first
	 * line contains the number of rows and columns as ints and each subsequent
	 * line is one row of characters representing the contents of that position.
	 * Valid characters are as follows:
	 *  'O' an open position
	 *  'X' an occupied, unavailable position
	 *  '1' first of two components needing to be connected
	 *  '2' second of two components needing to be connected
	 *  'T' is not expected in input files - represents part of the trace
	 *   connecting components 1 and 2 in the solution
	 * 
	 * @param filename
	 * 		file containing a grid of characters
	 * @throws FileNotFoundException if Scanner cannot read the file
	 * @throws InvalidFileFormatException for any other format or content issue that prevents reading a valid input file
	 */
	public CircuitBoard(String filename) throws FileNotFoundException {
		Scanner readingFile = new Scanner (new File(filename));
		rows = readingFile.nextInt();
		columns = readingFile.nextInt();
		board = new char[rows][columns];
		readingFile.nextLine();
		int rowIndex = 0;
		while(readingFile.hasNextLine()) {
			int columnIndex = 0;
			String fileLine = readingFile.nextLine();
			Scanner readingLine = new Scanner(fileLine);
			if (rowIndex >= rows) {
				readingLine.close();
				throw new InvalidFileFormatException("Number of rows incorrect.");
			}
			while(readingLine.hasNext()) {
				if (columnIndex >= columns) {
					readingLine.close();
					throw new InvalidFileFormatException("Number of columns incorrect.");
				}
				String token = readingLine.next();
				board[rowIndex][columnIndex] = token.charAt(0);
				boolean allowedChar = false;
				for (int i = 0; i < allowChars.length; i++) {
					if (board[rowIndex][columnIndex] == allowChars[i])  allowedChar = true;
				}
				if (!allowedChar) {
					readingLine.close();
					throw new InvalidFileFormatException(board[rowIndex][columnIndex] + " is not a valid component.");
				}
				if (board[rowIndex][columnIndex] == START) {
					if (foundStart) {
						readingLine.close();
						throw new InvalidFileFormatException("More than one starting point.");
					}
					foundStart = true;
					startingPoint = new Point(rowIndex, columnIndex);
				}
				if (board[rowIndex][columnIndex] == END) {
					if (foundEnd) {
						readingLine.close();
						throw new InvalidFileFormatException("More than one ending point.");
					}
					foundEnd = true;
					endingPoint = new Point(rowIndex, columnIndex);
				}
				columnIndex++;
			}
			readingLine.close();
			if (columnIndex != columns) {
				throw new InvalidFileFormatException("Number of columns incorrect");
			}
			rowIndex++;
		}
		if (rowIndex != rows) {
			throw new InvalidFileFormatException("Number of rows incorrect.");
		}
		
		if (!foundStart || !foundEnd) {
			throw new InvalidFileFormatException("Missing starting or ending point.");
		}
		
		ROWS = rows; //replace with initialization statements using values from file
		COLS = columns;
		
		readingFile.close();
	}
	
	/** Copy constructor - duplicates original board
	 * 
	 * @param original board to copy
	 */
	public CircuitBoard(CircuitBoard original) {
		board = original.getBoard();
		startingPoint = new Point(original.startingPoint);
		endingPoint = new Point(original.endingPoint);
		ROWS = original.numRows();
		COLS = original.numCols();
	}

	/** utility method for copy constructor
	 * @return copy of board array */
	private char[][] getBoard() {
		char[][] copy = new char[board.length][board[0].length];
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				copy[row][col] = board[row][col];
			}
		}
		return copy;
	}
	
	/** Return the char at board position x,y
	 * @param row row coordinate
	 * @param col col coordinate
	 * @return char at row, col
	 */
	public char charAt(int row, int col) {
		return board[row][col];
	}
	
	/** Return whether given board position is open
	 * @param row
	 * @param col
	 * @return true if position at (row, col) is open 
	 */
	public boolean isOpen(int row, int col) {
		if (row < 0 || row >= board.length || col < 0 || col >= board[row].length) {
			return false;
		}
		return board[row][col] == OPEN;
	}
	
	/** Set given position to be a 'T'
	 * @param row
	 * @param col
	 * @throws OccupiedPositionException if given position is not open
	 */
	public void makeTrace(int row, int col) {
		if (isOpen(row, col)) {
			board[row][col] = TRACE;
		} else {
			throw new OccupiedPositionException("row " + row + ", col " + col + "contains '" + board[row][col] + "'");
		}
	}
	
	/** @return starting Point(row,col) */
	public Point getStartingPoint() {
		return new Point(startingPoint);
	}
	
	/** @return ending Point(row,col) */
	public Point getEndingPoint() {
		return new Point(endingPoint);
	}
	
	/** @return number of rows in this CircuitBoard */
	public int numRows() {
		return ROWS;
	}
	
	/** @return number of columns in this CircuitBoard */
	public int numCols() {
		return COLS;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				str.append(board[row][col] + " ");
			}
			str.append("\n");
		}
		return str.toString();
	}
	
}// class CircuitBoard
