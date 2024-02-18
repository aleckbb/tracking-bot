package edu.java.bot.user;

import java.net.URL;
import java.util.ArrayList;

public final class User {
    private final String userName;
    private final long id;
    private final ArrayList<URL> tracks;
    private Boolean isWaitLink;

    public User(String userName, long id, ArrayList<URL> tracks, Boolean isWaitLink) {
        this.userName = userName;
        this.id = id;
        this.tracks = tracks;
        this.isWaitLink = isWaitLink;
    }

    public String userName() {
        return userName;
    }

    public long id() {
        return id;
    }

    public ArrayList<URL> tracks() {
        return tracks;
    }

    public Boolean isWaitLink() {
        return isWaitLink;
    }

    public void setWaitLink(Boolean waitLink) {
        isWaitLink = waitLink;
    }
}
