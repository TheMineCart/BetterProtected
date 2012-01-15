package tmc.BetterProtected.domain;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ProtectedBlock {
    private final int x;
    private final int y;
    private final int z;
    private Material blockType;
    private Player player;

    public ProtectedBlock(int x, int y, int z, Material blockType, Player player) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.blockType = blockType;
        this.player = player;
    }

    public ProtectedBlock(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
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

    public Material getBlockType() {
        return blockType;
    }

    public Player getPlayer() {
        return player;
    }

    public void setBlockType(Material blockType) {
        this.blockType = blockType;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
