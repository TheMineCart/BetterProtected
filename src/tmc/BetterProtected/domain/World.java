package tmc.BetterProtected.domain;

import com.google.gson.annotations.Expose;

public class World {
    @Expose private String name;

    public World(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof World)) return false;

        World world1 = (World) o;

        if (name != null ? !name.equals(world1.name) : world1.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
