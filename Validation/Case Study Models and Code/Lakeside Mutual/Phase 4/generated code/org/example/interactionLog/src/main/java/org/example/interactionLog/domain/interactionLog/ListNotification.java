package org.example.interactionLog.domain.interactionLog;

import java.util.ArrayList;

public class ListNotification extends ArrayList<ListNotification.ListNotificationItem> {

    public static class ListNotificationItem {

        public ListNotificationItem() {
        }

        private Notification notification;

        public Notification getNotification() {
            return notification;
        }

        public void setNotification(Notification notification) {
            this.notification = notification;
        }

        public ListNotificationItem(Notification notification) {
            this.notification = notification;
        }
    }
}
