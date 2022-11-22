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

    public Coordinate moveUp(int a) {
        y -= a;
        return this;
    }

    public Coordinate moveDown(int a) {
        y += a;
        return this;
    }

    public Coordinate moveLeft(int a) {
        y += a;
        return this;
    }

    public Coordinate moveRight(int a) {
        y -= a;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        return ((Coordinate) obj).getX() == x && ((Coordinate) obj).getY() == y;
    }
}
