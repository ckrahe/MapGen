package org.krahe.chris.mapgen.core.util;

public class OptionsException extends Exception {
    private final Exception cause;

    public OptionsException(String message) {
        super(message);
        cause = null;
    }

    public OptionsException(String message, Exception cause) {
        super(message);
        this.cause = cause;
    }

    public boolean hasCause() {
        return cause != null;
    }

    public Exception getCause() {
        return cause;
    }
}
