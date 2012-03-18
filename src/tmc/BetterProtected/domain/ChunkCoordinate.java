package tmc.BetterProtected.domain;

import com.google.gson.annotations.Expose;

public class ChunkCoordinate {
    @Expose private Integer x;
    @Expose private Integer z;

    public ChunkCoordinate(Integer x, Integer z) {
        this.x = x;
        this.z = z;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getZ() {
        return z;
    }

    public void setZ(Integer z) {
        this.z = z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChunkCoordinate)) return false;

        ChunkCoordinate that = (ChunkCoordinate) o;

        if (x != null ? !x.equals(that.x) : that.x != null) return false;
        if (z != null ? !z.equals(that.z) : that.z != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x != null ? x.hashCode() : 0;
        result = 31 * result + (z != null ? z.hashCode() : 0);
        return result;
    }
}