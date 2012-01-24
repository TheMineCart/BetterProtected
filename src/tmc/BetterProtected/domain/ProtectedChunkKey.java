package tmc.BetterProtected.domain;

public class ProtectedChunkKey {
    private final int x;
    private final int z;

    public ProtectedChunkKey(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProtectedChunkKey)) return false;

        ProtectedChunkKey that = (ProtectedChunkKey) o;

        if (x != that.x) return false;
        if (z != that.z) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + z;
        return result;
    }
}
