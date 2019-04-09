package exception;

public class ExceedsEquipmentLimit extends RuntimeException {
    public ExceedsEquipmentLimit() {
        super();
    }

    public ExceedsEquipmentLimit(String s) {
        super(s);
    }

    public ExceedsEquipmentLimit(String message, Throwable cause) {
        super(message, cause);
    }

    public ExceedsEquipmentLimit(Throwable cause) {
        super(cause);
    }

    static final long serialVersionUID = -1848912673592617417L;
}