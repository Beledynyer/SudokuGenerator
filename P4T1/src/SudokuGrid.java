import java.util.*;
import java.util.stream.*;

class SudokuGrid {
    private final int[][] grid;
    private static final int SIZE = 9;

    public SudokuGrid() {
        grid = new int[SIZE][SIZE];
    }

    /**
     * method to clear all box values
     */
    public void clearAllBoxes() {
        Arrays.stream(grid).forEach(row -> Arrays.fill(row, 0));
    }

    /**
     * method to set a box value
     * @param row
     * @param col
     * @param value
     */
    public void setBoxValue(int row, int col, int value) {
        grid[row][col] = value;
    }

    /**
     * method to query a box value
     * @param row
     * @param col
     * @return
     */
    public int getBoxValue(int row, int col) {
        return grid[row][col];
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
        boolean rowConflict = IntStream.range(0, SIZE)
                .anyMatch(i -> grid[row][i] == value);
        boolean colConflict = IntStream.range(0, SIZE)
                .anyMatch(i -> grid[i][col] == value);
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
                        .anyMatch(j -> grid[i][j] == value));
    }

    public boolean isFull() {
        return Arrays.stream(grid)
                .allMatch(row -> Arrays.stream(row).allMatch(val -> val != 0));
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
                sb.append(grid[i][j] == 0 ? "  " : grid[i][j] + " ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}

