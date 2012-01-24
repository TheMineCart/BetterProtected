package tmc.BetterProtected.domain;

public class ProtectedBlockKey {
    private final int y;

    public ProtectedBlockKey(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProtectedBlockKey)) return false;

        ProtectedBlockKey that = (ProtectedBlockKey) o;

        if (y != that.y) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return y;
    }
}
