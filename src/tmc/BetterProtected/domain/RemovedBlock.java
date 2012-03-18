package tmc.BetterProtected.domain;

import com.google.gson.annotations.Expose;
import org.bukkit.Material;
import org.joda.time.DateTime;

public class RemovedBlock extends Block {
    @Expose private DateTime removedOn;
    @Expose private Player removedBy;

    public RemovedBlock(DateTime removedOn, Player removedBy, BlockCoordinate blockCoordinate, ChunkCoordinate chunkCoordinate, String world, Material material) {
        this.removedOn = removedOn;
        this.removedBy = removedBy;
        super.setBlockCoordinate(blockCoordinate);
        super.setChunkCoordinate(chunkCoordinate);
        super.setWorld(world);
        super.setMaterial(material);
    }

    public DateTime getRemovedOn() {
        return removedOn;
    }

    public void setRemovedOn(DateTime removedOn) {
        this.removedOn = removedOn;
    }

    public Player getRemovedBy() {
        return removedBy;
    }

    public void setRemovedBy(Player removedBy) {
        this.removedBy = removedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RemovedBlock)) return false;
        if (!super.equals(o)) return false;

        RemovedBlock that = (RemovedBlock) o;

        if (removedBy != null ? !removedBy.equals(that.removedBy) : that.removedBy != null) return false;
        if (removedOn != null ? !removedOn.equals(that.removedOn) : that.removedOn != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (removedOn != null ? removedOn.hashCode() : 0);
        result = 31 * result + (removedBy != null ? removedBy.hashCode() : 0);
        return result;
    }
}
