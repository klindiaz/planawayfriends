package exception;

public class NoSuchProgramException extends RuntimeException {
    public NoSuchProgramException() {
        super();
    }

    public NoSuchProgramException(String s) {
        super(s);
    }

    public NoSuchProgramException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchProgramException(Throwable cause) {
        super(cause);
    }

    static final long serialVersionUID = -1848914673793213413L;
}
