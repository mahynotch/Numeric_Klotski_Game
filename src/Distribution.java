import java.util.Arrays;

public class Distribution implements Comparable<Distribution> {
    int[][] dist;
    int step;

    public Distribution(Board board, int step) {
        dist = board.to2DArray();
        this.step = step;
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

    @Override
    public int compareTo(Distribution o) {
        return step - o.step;
    }
}
