package tmc.BetterProtected.domain.types;

public enum BlockEventType {
    PLACED("Placed"),
    REMOVED("Removed"),
    SHIFT("Shift"),
    ;
    private String description;

    BlockEventType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
