package eu.marcocattaneo.androidinstagramconnector.connection.implementation;

import eu.marcocattaneo.androidinstagramconnector.connection.InstagramSession;
import eu.marcocattaneo.androidinstagramconnector.connection.models.ConnectionError;

public interface InstagramListener {

    void onConnect(InstagramSession session);

    void onError(ConnectionError error);

}
