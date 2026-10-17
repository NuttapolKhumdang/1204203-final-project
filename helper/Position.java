package helper;

public class Position {
    public int id;
    public int x = 0, y = 0;
    public double rotation = 0;
    public int vx = 0, vy = 0;
    public int speed = 0;

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

    public Position(int id, int x, int y, int vx, int vy, int speed) {
        this(id, x, y);
        this.vx = vx;
        this.vy = vy;
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "" + id + "::" + x + "::" + y + "::" + rotation + "::" + vx + "::" + vy + "::" + speed;
    }
}
