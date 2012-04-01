package tmc.BetterProtected.domain;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.List;

public class TestBlock implements Block {

    private Material material = Material.AIR;
    private int z = 0;
    private int y = 0;
    private int x = 0;

    public TestBlock(int x, int y, int z, Material material) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.material = material;
    }
    public TestBlock(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        material = Material.AIR;
    }

    @Override
    public byte getData() {
        return 0;  
    }

    @Override
    public Block getRelative(int i, int i1, int i2) {
        return null;  
    }

    @Override
    public Block getRelative(BlockFace blockFace) {
        return null;  
    }

    @Override
    public Block getRelative(BlockFace blockFace, int i) {
        return null;  
    }

    @Override
    public Material getType() {
        return material;
    }

    @Override
    public int getTypeId() {
        return material.getId();
    }

    @Override
    public byte getLightLevel() {
        return 0;  
    }

    @Override
    public byte getLightFromSky() {
        return 0;  
    }

    @Override
    public byte getLightFromBlocks() {
        return 0;  
    }

    @Override
    public World getWorld() {
        return null;  
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public Location getLocation() {
        return null;  
    }

    @Override
    public Chunk getChunk() {
        return null;  
    }

    @Override
    public void setData(byte b) {
        
    }

    @Override
    public void setData(byte b, boolean bool) {
        
    }

    @Override
    public void setType(Material material) {
        this.material = material;
    }

    @Override
    public boolean setTypeId(int i) {
        return false;  
    }

    @Override
    public boolean setTypeId(int i, boolean b) {
        return false;  
    }

    @Override
    public boolean setTypeIdAndData(int i, byte b, boolean bool) {
        return false;  
    }

    @Override
    public BlockFace getFace(Block block) {
        return null;  
    }

    @Override
    public BlockState getState() {
        return null;  
    }

    @Override
    public Biome getBiome() {
        return null;  
    }

    @Override
    public void setBiome(Biome biome) {
        
    }

    @Override
    public boolean isBlockPowered() {
        return false;  
    }

    @Override
    public boolean isBlockIndirectlyPowered() {
        return false;  
    }

    @Override
    public boolean isBlockFacePowered(BlockFace blockFace) {
        return false;  
    }

    @Override
    public boolean isBlockFaceIndirectlyPowered(BlockFace blockFace) {
        return false;  
    }

    @Override
    public int getBlockPower(BlockFace blockFace) {
        return 0;  
    }

    @Override
    public int getBlockPower() {
        return 0;  
    }

    @Override
    public boolean isEmpty() {
        return false;  
    }

    @Override
    public boolean isLiquid() {
        return false;  
    }

    @Override
    public double getTemperature() {
        return 0;  
    }

    @Override
    public double getHumidity() {
        return 0;  
    }

    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        return null;  
    }

    @Override
    public boolean breakNaturally() {
        return false;  
    }

    @Override
    public boolean breakNaturally(ItemStack itemStack) {
        return false;  
    }

    @Override
    public Collection<ItemStack> getDrops() {
        return null;  
    }

    @Override
    public Collection<ItemStack> getDrops(ItemStack itemStack) {
        return null;  
    }

    @Override
    public void setMetadata(String s, MetadataValue metadataValue) {
        
    }

    @Override
    public List<MetadataValue> getMetadata(String s) {
        return null;  
    }

    @Override
    public boolean hasMetadata(String s) {
        return false;  
    }

    @Override
    public void removeMetadata(String s, Plugin plugin) {
        
    }
}
