import java.util.Arrays;

public class Distribution {
    int[][] dist;
    int[][] sorted;

    public Distribution(Board board, int[][] sorted) {
        dist = new int[sorted.length][sorted[0].length];
        for (int i = 0; i < dist.length; i++) {
            for (int j = 0; j < dist[0].length; j++) {
                dist[i][j] = board.findValueByCoordinate(j, i);
            }
        }
        this.sorted = sorted;
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
