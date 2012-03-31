package tmc.BetterProtected.domain;

import com.google.gson.annotations.Expose;

public class BlockCoordinate {
    @Expose private Long x;
    @Expose private Long y;
    @Expose private Long z;

    public BlockCoordinate(Long x, Long y, Long z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BlockCoordinate(Integer x, Integer y, Integer z) {
        this.x = x.longValue();
        this.y = y.longValue();
        this.z = z.longValue();
    }

    public Long getX() {
        return x;
    }

    public void setX(Long x) {
        this.x = x;
    }

    public Long getY() {
        return y;
    }

    public void setY(Long y) {
        this.y = y;
    }

    public Long getZ() {
        return z;
    }

    public void setZ(Long z) {
        this.z = z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BlockCoordinate)) return false;

        BlockCoordinate that = (BlockCoordinate) o;

        if (x != null ? !x.equals(that.x) : that.x != null) return false;
        if (y != null ? !y.equals(that.y) : that.y != null) return false;
        if (z != null ? !z.equals(that.z) : that.z != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x != null ? x.hashCode() : 0;
        result = 31 * result + (y != null ? y.hashCode() : 0);
        result = 31 * result + (z != null ? z.hashCode() : 0);
        return result;
    }
}
