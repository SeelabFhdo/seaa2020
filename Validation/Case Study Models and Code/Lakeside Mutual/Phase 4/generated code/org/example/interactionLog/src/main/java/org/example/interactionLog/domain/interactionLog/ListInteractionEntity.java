package org.example.interactionLog.domain.interactionLog;

import java.util.ArrayList;

public class ListInteractionEntity extends ArrayList<ListInteractionEntity.ListInteractionEntityItem> {

    public static class ListInteractionEntityItem {

        public ListInteractionEntityItem() {
        }

        private InteractionEntity interactionEntity;

        public InteractionEntity getInteractionEntity() {
            return interactionEntity;
        }

        public void setInteractionEntity(InteractionEntity interactionEntity) {
            this.interactionEntity = interactionEntity;
        }

        public ListInteractionEntityItem(InteractionEntity interactionEntity) {
            this.interactionEntity = interactionEntity;
        }
    }
}
