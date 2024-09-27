import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class SudokuGenerator {
    private static final int SIZE = 9;
    private final SudokuGrid grid;
    private final List<Set<Integer>> possibilities;

    public SudokuGenerator() {
        grid = new SudokuGrid();
        possibilities = new ArrayList<>(SIZE * SIZE);
        for (int i = 0; i < SIZE * SIZE; i++) {
            possibilities.add(IntStream.rangeClosed(1, 9).boxed().collect(Collectors.toSet()));
        }
    }

    public SudokuGrid generate() {
        grid.clearAllBoxes();
        while (!grid.isFull()) {
            int index = getIndexWithLeastPossibilities();
            if (index == -1 || possibilities.get(index).isEmpty()) {
                // Impossible board, start over
                return generate();
            }
            int row = index / SIZE;
            int col = index % SIZE;
            int value = getRandomValue(possibilities.get(index));
            grid.setBoxValue(row, col, value);
            updatePossibilities(row, col, value);
        }
        return grid;
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
        return values.get(new Random().nextInt(values.size()));
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


