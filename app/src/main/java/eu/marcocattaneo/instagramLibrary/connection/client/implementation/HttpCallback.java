package eu.marcocattaneo.instagramLibrary.connection.client.implementation;

public interface HttpCallback {

    void onResponse(String body, int resultCode);

    void onFail(int resultCode);


}
