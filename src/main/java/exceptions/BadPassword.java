package exceptions;

public class BadPassword extends Exception {
    public BadPassword(String message) {
        super(message);
    }
}
