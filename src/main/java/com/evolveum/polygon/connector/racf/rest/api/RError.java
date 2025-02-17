package com.evolveum.polygon.connector.racf.rest.api;

public class RError {

    private int status;
    private String message;

    public RError(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() { return status; }

    public String getMessage() { return message; }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RError{");
        sb.append("status=").append(status);
        sb.append(", message=").append(message).append('\'');
        sb.append('}');

        return sb.toString();
    }
}
