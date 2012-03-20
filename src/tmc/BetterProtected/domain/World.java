package tmc.BetterProtected.domain;

import com.google.gson.annotations.Expose;

public class World {
    @Expose private String world;

    public World(String world) {
        this.world = world;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof World)) return false;

        World world1 = (World) o;

        if (world != null ? !world.equals(world1.world) : world1.world != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return world != null ? world.hashCode() : 0;
    }
}
