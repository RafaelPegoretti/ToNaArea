package br.com.hbsis.tonaarea.util;

public class Validator {

    boolean isValid;
    String message;

    public Validator(boolean isValid, String message) {
        this.isValid = isValid;
        this.message = message;
    }

    public boolean isValid() {
        return isValid;
    }

    public String getMessage() {
        return message;
    }
}
