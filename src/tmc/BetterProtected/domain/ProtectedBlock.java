package tmc.BetterProtected.domain;

import org.bukkit.Material;
import org.joda.time.LocalDateTime;

public class ProtectedBlock {
    private final int x;
    private final int y;
    private final int z;
    private final ProtectedBlockKey key;
    private final Material blockType;
    private final String player;
    private final LocalDateTime timestamp;

    public ProtectedBlock(int x, int y, int z, Material blockType, String player) {
        this(x, y, z, blockType, player, LocalDateTime.now());
    }

    public ProtectedBlock(int x, int y, int z, Material blockType, String player, LocalDateTime timestamp) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.key = new ProtectedBlockKey(y);
        this.blockType = blockType;
        this.player = player;
        this.timestamp = timestamp;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public ProtectedBlockKey getKey() {
        return key;
    }

    public Material getBlockType() {
        return blockType;
    }

    public String getPlayer() {
        return player;
    }

    public Boolean isSamePositionAs(ProtectedBlock block) {
        return ((x == block.getX()) && (y == block.getY()) && (z == block.getZ()));
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProtectedBlock)) return false;

        ProtectedBlock that = (ProtectedBlock) o;

        if (x != that.x) return false;
        if (y != that.y) return false;
        if (z != that.z) return false;
        if (blockType != that.blockType) return false;
        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        if (player != null ? !player.equals(that.player) : that.player != null) return false;
        if (timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + z;
        result = 31 * result + (key != null ? key.hashCode() : 0);
        result = 31 * result + (blockType != null ? blockType.hashCode() : 0);
        result = 31 * result + (player != null ? player.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        return result;
    }
}
