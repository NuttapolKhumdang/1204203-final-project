package objects;

public class PlayerData {
    public int id;
    public int x, y;
    public double rotation;
    public int life = -1;

    public PlayerData(int id) {
        this.id = id;
    }

    public PlayerData(int id, int life) {
        this(id);
        this.life = life;
    }

    public PlayerData(int id, int x, int y, double rotation) {
        this(id);

        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.life = -1;
    }
}
