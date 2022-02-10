import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Debug {
	
	private static int rows;
	private static int columns;
	
	private static char[][] board;
	
	public static void main(String[] args) throws FileNotFoundException {
		CircuitBoard("valid1.dat");
		print(board);
	}
	
	private static void print(char[][] referenceBoard) {
		int rowIndex = 0;
		while (rowIndex < rows) {
			int columnIndex = 0;
			while (columnIndex < columns) {
				System.out.print(" " + referenceBoard[rowIndex][columnIndex] + " ");
				columnIndex++;
			}
			System.out.println();
			rowIndex++;
		}
	}

	public static void CircuitBoard(String filename) throws FileNotFoundException {
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
			while(readingLine.hasNext()) {
				board[rowIndex][columnIndex] = readingLine.next().charAt(0);
				columnIndex++;
			}
			readingLine.close();
			if (columnIndex != columns) {
				throw new InvalidFileFormatException("Invalid file format, file contains incorrect number of columns");
			}
			rowIndex++;
		}
		if (rowIndex != rows) {
			throw new InvalidFileFormatException("Invalid file format, file contains incorrect number of rows");
		}
		
		readingFile.close();
	}
	
	
}
