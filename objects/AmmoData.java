package objects;

public class AmmoData {
    public int id;
    public int x, y;
    public double rotation;

    public AmmoData(int id) {
        this.id = id;
    }

    public AmmoData(int id, int x, int y, double rotation) {
        this(id);

        this.x = x;
        this.y = y;
        this.rotation = rotation;
    }
}
