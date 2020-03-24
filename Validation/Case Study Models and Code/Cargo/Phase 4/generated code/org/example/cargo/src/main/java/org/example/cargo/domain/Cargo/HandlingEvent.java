package org.example.cargo.domain.Cargo;

import java.util.Date;

public class HandlingEvent {

    public HandlingEvent() {
    }

    private Date completiontime;

    public Date getCompletiontime() {
        return completiontime;
    }

    public void setCompletiontime(Date completiontime) {
        this.completiontime = completiontime;
    }

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private Cargo handled;

    public Cargo getHandled() {
        return handled;
    }

    public void setHandled(Cargo handled) {
        this.handled = handled;
    }

    public HandlingEvent(Date completiontime, String type, Cargo handled) {
        this.completiontime = completiontime;
        this.type = type;
        this.handled = handled;
    }
}
