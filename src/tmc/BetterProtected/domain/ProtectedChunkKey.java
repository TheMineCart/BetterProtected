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
}
