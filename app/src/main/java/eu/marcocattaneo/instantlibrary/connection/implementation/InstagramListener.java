package eu.marcocattaneo.instantlibrary.connection.implementation;

import eu.marcocattaneo.instantlibrary.connection.InstagramSession;
import eu.marcocattaneo.instantlibrary.connection.models.ConnectionError;

public interface InstagramListener {

    void onConnect(InstagramSession session);

    void onError(ConnectionError error);

}
