package exception;

public class NoSuchEquipmentException extends RuntimeException {
    public NoSuchEquipmentException() {
        super();
    }

    public NoSuchEquipmentException(String s) {
        super(s);
    }

    public NoSuchEquipmentException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchEquipmentException(Throwable cause) {
        super(cause);
    }

    static final long serialVersionUID = -1848914673593113417L;
}
