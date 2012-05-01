package tmc.TestUtilities.Mocks;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class TestLocation extends Location {
    private Block block = new TestBlock(1, 1, 1);
    private World world = new TestWorld("test");

    public TestLocation(org.bukkit.World world, double x, double y, double z) {
        super(world, x, y, z);
        this.world = world;
        this.block = world.getBlockAt((int) x, (int) y, (int) z);
    }

    public TestLocation(World world, double x, double y, double z, float yaw, float pitch) {
        super(world, x, y, z, yaw, pitch);
        this.world = world;
        this.block = world.getBlockAt((int) x, (int) y, (int) z);
    }

    public TestLocation(TestWorld world, TestBlock block) {
        this(world, block.getX(), block.getY(), block.getZ());
        this.world = world;
        this.block = block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    @Override
    public Block getBlock() {
        return block;
    }

    public World getWorld() {
        return world;
    }
}
