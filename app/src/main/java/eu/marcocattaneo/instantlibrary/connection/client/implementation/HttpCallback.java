package eu.marcocattaneo.instantlibrary.connection.client.implementation;

public interface HttpCallback {

    void onResponse(String body, int resultCode);

    void onFail(int resultCode);


}
