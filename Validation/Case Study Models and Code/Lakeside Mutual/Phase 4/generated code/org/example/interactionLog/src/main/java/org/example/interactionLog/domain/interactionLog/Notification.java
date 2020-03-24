package org.example.interactionLog.domain.interactionLog;

public class Notification {

    public Notification() {
    }

    private String username;

    public String getUsername() {
        return username;
    }

    private int count;

    public int getCount() {
        return count;
    }

    public Notification(String username, int count) {
        this.username = username;
        this.count = count;
    }
}
