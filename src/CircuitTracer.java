import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Search for shortest paths between start and end points on a circuit board
 * as read from an input file using either a stack or queue as the underlying
 * search state storage structure and displaying output to the console or to
 * a GUI according to options specified via command-line arguments.
 * 
 * @author mvail
 */
public class CircuitTracer {
	
	private static Storage<TraceState> stateStore; // Storage of TraceState Objects
	private static ArrayList<TraceState> bestPaths; // ArrayList holding best paths
	private static CircuitBoard board; // Circuit board
	
	private static int shortestPathLength = Integer.MAX_VALUE; // initialized at the max value of an integer
	private static int starting_x; // x position of '1'
	private static int starting_y; // y position of '1'
	
	/** launch the program
	 * @param args three required arguments:
	 *  first arg: -s for stack or -q for queue
	 *  second arg: -c for console output or -g for GUI output
	 *  third arg: input file name 
	 */
	public static void main(String[] args) {
		if (args.length != 3) {
			printUsage();
			System.exit(1);
		}
		try {
			new CircuitTracer(args); //create this with args
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/** Print instructions for running CircuitTracer from the command line. */
	private static void printUsage() {
		System.out.print("To use CircuitTracer, the command to use is: ");
		System.out.println("java CircuitTracer (first argument) (second argument) (fileName)");
		System.out.println("The first argument has two options: \"-s\" or \"-q\"");
		System.out.println("\"-s\" runs as a stack data type and \"-q\" runs as a queue data type");
		System.out.println("The second argument has two options: \"-c\" or \"-g\"");
		System.out.println("\"-c\" prints the output to the console and \"-g\" prints the output to a GUI");
		System.out.println("\"-g\" is currently not implemented");
		System.out.println("The last argument is the file name of the file you are trying to read in.");

		
	}
	/** 
	 * Set up the CircuitBoard and all other components based on command
	 * line arguments.
	 * 
	 * @param args command line arguments passed through from main()
	 */
	private CircuitTracer(String[] args) throws FileNotFoundException {
		if (args[0].equals("-s")) {
			stateStore = new Storage<TraceState>(Storage.DataStructure.stack);
		} else if (args[0].equals("-q")) {
			stateStore = new Storage<TraceState>(Storage.DataStructure.queue);		
		}
		if (args[1].equals(("-g"))) {
			System.out.println("GUI not supported");
		} else if (args[1].equals("-c")){
			board = new CircuitBoard(args[2]);
			bestPaths = new ArrayList<TraceState>();
			starting_x = board.getStartingPoint().x;
			starting_y = board.getStartingPoint().y;
			traceStateStart(starting_x, starting_y);
			while (!stateStore.isEmpty()) {
				TraceState item = stateStore.retrieve();
				if (item.isComplete()) {
					if (item.pathLength() == shortestPathLength) {
						bestPaths.add(item);
					} else if (item.pathLength() < shortestPathLength) {
						bestPaths = new ArrayList<TraceState>();
						bestPaths.add(item);
						shortestPathLength = item.pathLength();
					}
				} else {
					generateNextState(item);
				}
			}
			
		output();
		}
	}
	
	/**
	 * starting TraceState 
	 * @param row - row of starting pos.
	 * @param column - column of starting pos.
	 */
	private void traceStateStart(int row, int column) {
		if (board.isOpen(row + 1, column)) {
			stateStore.store(new TraceState(board, row + 1, column));
		}
		if (board.isOpen(row - 1, column)) {
			stateStore.store(new TraceState(board, row - 1, column));
		}
		if (board.isOpen(row, column + 1)) {
			stateStore.store(new TraceState(board, row, column + 1));
		}
		if (board.isOpen(row, column - 1)) {
			stateStore.store(new TraceState(board, row, column - 1));
		}
	}
	
	/**
	 * Next position of TraceState
	 * @param item - current TraceState
	 */
	private void generateNextState(TraceState item) {
		if (item.isOpen(item.getRow() + 1, item.getCol())) {
			stateStore.store(new TraceState(item, item.getRow() + 1, item.getCol()));
		}
		if (item.isOpen(item.getRow() - 1, item.getCol())) {
			stateStore.store(new TraceState(item, item.getRow() - 1, item.getCol()));
		}
		if (item.isOpen(item.getRow(), item.getCol() + 1)) {
			stateStore.store(new TraceState(item, item.getRow(), item.getCol() + 1));
		}
		if (item.isOpen(item.getRow(), item.getCol() - 1)) {
			stateStore.store(new TraceState(item, item.getRow(), item.getCol() - 1));
		}
	}
	
	/**
	 * Results will be output to the console with this function. It will output the best paths.
	 */
	private static void output() {
		while(!bestPaths.isEmpty()) {
			TraceState currentPath = bestPaths.remove(0);
			System.out.println(currentPath.toString());
			System.out.println();
		}
	}
} // class CircuitTracer
