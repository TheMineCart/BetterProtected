package tmc.BetterProtected.domain;

import com.google.gson.annotations.Expose;
import org.joda.time.DateTime;

public class PlacedBlock extends Block {
    @Expose private DateTime placedOn;
    @Expose private Player placedBy;

    public DateTime getPlacedOn() {
        return placedOn;
    }

    public void setPlacedOn(DateTime placedOn) {
        this.placedOn = placedOn;
    }

    public Player getPlacedBy() {
        return placedBy;
    }

    public void setPlacedBy(Player placedBy) {
        this.placedBy = placedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlacedBlock)) return false;
        if (!super.equals(o)) return false;

        PlacedBlock that = (PlacedBlock) o;

        if (placedBy != null ? !placedBy.equals(that.placedBy) : that.placedBy != null) return false;
        if (placedOn != null ? !placedOn.equals(that.placedOn) : that.placedOn != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (placedOn != null ? placedOn.hashCode() : 0);
        result = 31 * result + (placedBy != null ? placedBy.hashCode() : 0);
        return result;
    }
}