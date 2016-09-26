package eu.marcocattaneo.instagramLibrary.connection.client.models;

import java.io.UnsupportedEncodingException;

public class HttpResponse {

    private int responseCode;

    private byte[] body;

    private UnsupportedEncodingException error;

    public HttpResponse(UnsupportedEncodingException e) {
        error = e;
    }

    public HttpResponse(int responseCode, byte[] body) {
        this.responseCode = responseCode;
        this.body = body;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

}

