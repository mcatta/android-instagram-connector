package eu.marcocattaneo.instagramLibrary.connection.models;

public class ConnectionError extends Exception {

    private String msg;

    public ConnectionError(String msg) {
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
