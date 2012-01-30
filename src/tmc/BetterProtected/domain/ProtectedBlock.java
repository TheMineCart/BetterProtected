package tmc.BetterProtected.domain;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ProtectedBlock {
    private final int x;
    private final int y;
    private final int z;
    private final ProtectedBlockKey key;
    private Material blockType;
    private String player;

    public ProtectedBlock(int x, int y, int z, Material blockType, String player) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.key = new ProtectedBlockKey(y);
        this.blockType = blockType;
        this.player = player;
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

    public void setBlockType(Material blockType) {
        this.blockType = blockType;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public Boolean isSamePositionAs(ProtectedBlock block) {
        return ((x == block.getX()) && (y == block.getY()) && (z == block.getZ()));
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
        return result;
    }
}
