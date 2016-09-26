package eu.marcocattaneo.instagramLibrary.connection.implementation;

import eu.marcocattaneo.instagramLibrary.connection.InstagramSession;
import eu.marcocattaneo.instagramLibrary.connection.models.ConnectionError;

public interface InstagramListener {

    void onConnect(InstagramSession session);

    void onError(ConnectionError error);

}
