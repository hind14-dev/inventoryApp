package ma.scs.inventory_app.exception;

public class RangeAlreadyAssignedException extends RuntimeException {
    public RangeAlreadyAssignedException(String message) {
        super(message);
    }
}