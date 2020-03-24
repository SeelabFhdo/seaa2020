package org.example.interactionLog.domain.interactionLog;

public class InteractionLogAggregateRoot {

    public InteractionLogAggregateRoot() {
    }

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private long lastAcknowledgedInteractionId;

    public long getLastAcknowledgedInteractionId() {
        return lastAcknowledgedInteractionId;
    }

    public void setLastAcknowledgedInteractionId(long lastAcknowledgedInteractionId) {
        this.lastAcknowledgedInteractionId = lastAcknowledgedInteractionId;
    }

    private ListInteractionEntity interactions;

    public ListInteractionEntity getInteractions() {
        return interactions;
    }

    public void setInteractions(ListInteractionEntity interactions) {
        this.interactions = interactions;
    }

    public InteractionLogAggregateRoot(String username, long lastAcknowledgedInteractionId, ListInteractionEntity interactions) {
        this.username = username;
        this.lastAcknowledgedInteractionId = lastAcknowledgedInteractionId;
        this.interactions = interactions;
    }

    public int getNumberOfUnacknowledgedInteractions() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
