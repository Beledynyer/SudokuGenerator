import java.util.*;
import java.util.stream.*;

class SudokuGrid {
    private final List<List<Integer>> grid;
    private static final int SIZE = 9;

    public SudokuGrid() {
        grid = new ArrayList<>(SIZE);
        for (int i = 0; i < SIZE; i++) {
            grid.add(new ArrayList<>(Collections.nCopies(SIZE, 0)));
        }
    }

    /**
     * method to clear all box values
     */
    public void clearAllBoxes() {
        grid.forEach(row -> Collections.fill(row, 0));
    }

    /**
     * method to set a box value
     * @param row
     * @param col
     * @param value
     */
    public void setBoxValue(int row, int col, int value) {
        grid.get(row).set(col, value);
    }

    /**
     * method to query a box value
     * @param row
     * @param col
     * @return
     */
    public int getBoxValue(int row, int col) {
        return grid.get(row).get(col);
    }

    /**
     *
     * @param row
     * @param col
     * @param value
     * @return a ValidationResult object, indicating whether the placement is valid
     * and the reason(s) for invalidity.
     */
    public ValidationResult isValidPlacement(int row, int col, int value) {
        boolean rowConflict = grid.get(row).contains(value);
        boolean colConflict = grid.stream().anyMatch(r -> r.get(col) == value);
        boolean blockConflict = isBlockConflict(row, col, value);

        if (!rowConflict && !colConflict && !blockConflict) {
            return ValidationResult.VALID;
        }

        return new ValidationResult(rowConflict, colConflict, blockConflict);
    }

    /**
     * checking if placing a value in a specific cell would conflict with
     * any existing values in the same 3x3 block of the Sudoku grid.
     * @param row
     * @param col
     * @param value
     * @return
     */
    private boolean isBlockConflict(int row, int col, int value) {
        //These lines calculate the top-left corner of the 3x3 block that contains the cell we're checking.
        int blockRow = row - row % 3;
        int blockCol = col - col % 3;
        return IntStream.range(blockRow, blockRow + 3)
                .anyMatch(i -> IntStream.range(blockCol, blockCol + 3)
                        .anyMatch(j -> grid.get(i).get(j) == value));
    }

    public boolean isFull() {
        return grid.stream().allMatch(row -> row.stream().noneMatch(val -> val == 0));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SIZE; i++) {
            if (i % 3 == 0 && i != 0) {
                sb.append("-".repeat(25)).append("\n");
            }
            for (int j = 0; j < SIZE; j++) {
                if (j % 3 == 0 && j != 0) {
                    sb.append("| ");
                }
                sb.append(grid.get(i).get(j) == 0 ? "  " : grid.get(i).get(j) + " ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}