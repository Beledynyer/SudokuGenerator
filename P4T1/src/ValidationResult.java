import java.util.ArrayList;
import java.util.List;

class ValidationResult {
    public static final ValidationResult VALID = new ValidationResult(false, false, false);

    private final boolean rowConflict;
    private final boolean colConflict;
    private final boolean blockConflict;

    public ValidationResult(boolean rowConflict, boolean colConflict, boolean blockConflict) {
        this.rowConflict = rowConflict;
        this.colConflict = colConflict;
        this.blockConflict = blockConflict;
    }

    public boolean isValid() {
        return !rowConflict && !colConflict && !blockConflict;
    }

    @Override
    public String toString() {
        if (isValid()) return "Valid";
        List<String> conflicts = new ArrayList<>();
        if (rowConflict) conflicts.add("row");
        if (colConflict) conflicts.add("column");
        if (blockConflict) conflicts.add("block");
        return "Invalid due to " + String.join(" and ", conflicts) + " conflict(s)";
    }
}
