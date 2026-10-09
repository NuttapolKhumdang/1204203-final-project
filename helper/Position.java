package helper;

public class Position {
    public int id;
    public int x = 0, y = 0;
    public double rotation = 0;

    public Position(int id) {
        this.id = id;
    }

    public Position(int id, int x, int y) {
        this(id);

        this.x = x;
        this.y = y;
    }

    public Position(int id, int x, int y, double rotation) {
        this(id, x, y);
        this.rotation = rotation;
    }

    @Override
    public String toString() {
        return "" + x + "::" + y + "::" + rotation;
    }
}
