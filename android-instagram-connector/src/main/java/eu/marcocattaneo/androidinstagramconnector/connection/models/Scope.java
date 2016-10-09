package eu.marcocattaneo.androidinstagramconnector.connection.models;

/**
 * Instagram Authorization Scope
 */
public enum Scope {

    // basic - to read a user’s profile info and media
    BASIC("basic"),

    // public_content - to read any public profile info and media on a user’s behalf
    PUBLIC("public_content"),

    // follower_list - to read the list of followers and followed-by users
    FOLLOWER("follower_list"),

    // comments - to post and delete comments on a user’s behalf
    COMMENTS("comments"),

    // relationships - to follow and unfollow accounts on a user’s behalf
    RELATIONSHIPS("RELATIONSHIPS"),

    // likes - to like and unlike media on a user’s behalf
    LIKES("likes");

    String value;

    Scope(String value) {
        this.value = value;
    }

    public String getScopeValue() {
        return value;
    }
}
