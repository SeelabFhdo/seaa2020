package org.example.cargo.domain.Cargo;

import java.util.ArrayList;

public class ListHandlingEvent extends ArrayList<ListHandlingEvent.ListHandlingEventItem> {

    public static class ListHandlingEventItem {

        public ListHandlingEventItem() {
        }

        private HandlingEvent handlingEvent;

        public HandlingEvent getHandlingEvent() {
            return handlingEvent;
        }

        public void setHandlingEvent(HandlingEvent handlingEvent) {
            this.handlingEvent = handlingEvent;
        }

        public ListHandlingEventItem(HandlingEvent handlingEvent) {
            this.handlingEvent = handlingEvent;
        }
    }
}
