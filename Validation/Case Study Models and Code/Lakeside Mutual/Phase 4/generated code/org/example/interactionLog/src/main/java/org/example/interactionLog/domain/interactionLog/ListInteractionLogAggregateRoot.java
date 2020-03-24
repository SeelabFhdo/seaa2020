package org.example.interactionLog.domain.interactionLog;

import java.util.ArrayList;

public class ListInteractionLogAggregateRoot extends ArrayList<ListInteractionLogAggregateRoot.ListInteractionLogAggregateRootItem> {

    public static class ListInteractionLogAggregateRootItem {

        public ListInteractionLogAggregateRootItem() {
        }

        private InteractionLogAggregateRoot interactionLogAggregateRoot;

        public InteractionLogAggregateRoot getInteractionLogAggregateRoot() {
            return interactionLogAggregateRoot;
        }

        public void setInteractionLogAggregateRoot(InteractionLogAggregateRoot interactionLogAggregateRoot) {
            this.interactionLogAggregateRoot = interactionLogAggregateRoot;
        }

        public ListInteractionLogAggregateRootItem(InteractionLogAggregateRoot interactionLogAggregateRoot) {
            this.interactionLogAggregateRoot = interactionLogAggregateRoot;
        }
    }
}
