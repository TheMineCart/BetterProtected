package tmc.BetterProtected.domain;

import com.google.gson.annotations.Expose;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class ChunkCoordinate {
    @Expose private Long x;
    @Expose private Long z;

    public static List<ChunkCoordinate> findChunkCoordinatesInRadius(int radius, int originX, int originZ) {
        List<ChunkCoordinate> chunkCoordinates = newArrayList();
        int xMin = originX - radius;
        int xMax = originX + radius;
        int zMin = originZ - radius;
        int zMax = originZ + radius;

        for (int i = xMin; i <= xMax; i++) {
            for (int j = zMin; j <= zMax; j++) {
                if (coordinateInsideRadius(i, j, originX, originZ, radius)) {
                    chunkCoordinates.add(new ChunkCoordinate(i, j));
                }
            }
        }

        return chunkCoordinates;
    }

    public static boolean coordinateInsideRadius(int x, int z, int originX, int originZ, int radius) {
        return (((x - originX) * (x - originX)) + ((z - originZ) * (z - originZ))) <= (radius * radius);
    }

    public ChunkCoordinate(Long x, Long z) {
        this.x = x;
        this.z = z;
    }

    public ChunkCoordinate(Integer x, Integer z) {
        this.x = x.longValue();
        this.z = z.longValue();
    }

    public Long getX() {
        return x;
    }

    public void setX(Long x) {
        this.x = x;
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
