package org.example.interactionLog.domain.interactionLog;

import java.util.Date;

public class InteractionEntity {

    public InteractionEntity() {
    }

    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private boolean sentByOperator;

    public boolean getSentByOperator() {
        return sentByOperator;
    }

    public void setSentByOperator(boolean sentByOperator) {
        this.sentByOperator = sentByOperator;
    }

    public InteractionEntity(long id, Date date, String content, boolean sentByOperator) {
        this.id = id;
        this.date = date;
        this.content = content;
        this.sentByOperator = sentByOperator;
    }
}
