import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class SudokuGenerator {
    private static final int SIZE = 9;
    private final SudokuGrid grid;
    private final List<Set<Integer>> possibilities;
    private final Random random;

    public SudokuGenerator() {
        grid = new SudokuGrid();
        possibilities = new ArrayList<>(SIZE * SIZE);
        for (int i = 0; i < SIZE * SIZE; i++) {
            possibilities.add(IntStream.rangeClosed(1, 9).boxed().collect(Collectors.toSet()));
        }
        random = new Random();
    }

    public SudokuGrid generate() {
        int attempts = 0;
        final int MAX_ATTEMPTS = 100;

        //if failed attempt , WCF algo stops and restarts
        // I said 100 max attempts, but you can set it to anything.
        while (attempts < MAX_ATTEMPTS) {
            if (tryGenerate()) {
                return grid;
            }
            attempts++;
        }

        throw new RuntimeException("Failed to generate a valid Sudoku grid after " + MAX_ATTEMPTS + " attempts.");
    }

    /**
     * This is the core of the Wave function collapse algorithm. It repeatedly:
     * Finds the cell with the least possibilities
     * Randomly chooses a value for that cell
     * Updates the possibilities for affected cells
     * @return
     */
    private boolean tryGenerate() {
        grid.clearAllBoxes();
        resetPossibilities();

        while (!grid.isFull()) {
            int index = getIndexWithLeastPossibilities();
            if (index == -1 || possibilities.get(index).isEmpty()) {
                return false; // Impossible board, try again
            }
            int row = index / SIZE;
            int col = index % SIZE;
            int value = getRandomValue(possibilities.get(index));
            grid.setBoxValue(row, col, value);
            updatePossibilities(row, col, value);
        }

        return true;
    }

    private void resetPossibilities() {
        possibilities.forEach(set -> {
            set.clear();
            set.addAll(IntStream.rangeClosed(1, 9).boxed().collect(Collectors.toSet()));
        });
    }

    private int getIndexWithLeastPossibilities() {
        return IntStream.range(0, SIZE * SIZE)
                .filter(i -> grid.getBoxValue(i / SIZE, i % SIZE) == 0)
                .boxed()
                .min(Comparator.comparingInt(i -> possibilities.get(i).size()))
                .orElse(-1);
    }

    private int getRandomValue(Set<Integer> possibleValues) {
        List<Integer> values = new ArrayList<>(possibleValues);
        return values.get(random.nextInt(values.size()));
    }

    private void updatePossibilities(int row, int col, int value) {
        IntStream.range(0, SIZE).forEach(i -> {
            removePossibility(row, i, value);
            removePossibility(i, col, value);
        });
        int blockRow = row - row % 3;
        int blockCol = col - col % 3;
        for (int i = blockRow; i < blockRow + 3; i++) {
            for (int j = blockCol; j < blockCol + 3; j++) {
                removePossibility(i, j, value);
            }
        }
    }

    private void removePossibility(int row, int col, int value) {
        possibilities.get(row * SIZE + col).remove(value);
    }
}


