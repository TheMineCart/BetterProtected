package tmc.BetterProtected.domain.types;

public enum BlockEventType {
    PLACED("Placed"),
    UNPROTECTED("Unprotected"),
    REMOVED("Removed"),
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
