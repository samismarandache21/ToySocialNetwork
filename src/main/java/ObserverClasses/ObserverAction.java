package ObserverClasses;

public class    ObserverAction {
    ActionType actionType;
    Long to, from;

    public ObserverAction(ActionType actionType, Long from, Long to) {
        this.actionType = actionType;
        this.to = to;
        this.from = from;
    }


    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }
}
