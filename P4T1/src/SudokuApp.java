import java.util.Scanner;

public class SudokuApp {
    public static void main(String[] args) {
        SudokuGenerator generator = new SudokuGenerator();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Press Enter to generate a new Sudoku board (or 'q' to quit):");
        while (!scanner.nextLine().equalsIgnoreCase("q")) {
            SudokuGrid grid = generator.generate();
            System.out.println(grid);
            System.out.println("Number of invalid boxes: " + countInvalidBoxes(grid));
            System.out.println("\nPress Enter to generate another board (or 'q' to quit):");
        }

        scanner.close();
    }

    private static int countInvalidBoxes(SudokuGrid grid) {
        int invalidCount = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int value = grid.getBoxValue(i, j);
                grid.setBoxValue(i, j, 0);
                if (!grid.isValidPlacement(i, j, value).isValid()) {
                    invalidCount++;
                }
                grid.setBoxValue(i, j, value);
            }
        }
        return invalidCount;
    }
}
