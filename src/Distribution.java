import java.util.Arrays;

public class Distribution {
    int[][] dist;

    public Distribution(Board board) {
        dist = board.to2DArray();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distribution that = (Distribution) o;
        return Arrays.deepEquals(dist, that.dist);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(dist);
    }
}
