//本类用来储存坐标
public class Coordinate {
    int x;
    int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public void move(int dx, int dy) {
        this.x = x + dx;
        this.y = y + dy;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Coordinate) return ((Coordinate) obj).getX() == x && ((Coordinate) obj).getY() == y;
        else throw new IllegalArgumentException("This is not a Coordinate!");
    }
}
