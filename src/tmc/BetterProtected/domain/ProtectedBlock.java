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
}
