package BaseObject;

public abstract class MovableObject extends GameObject {
    protected float dx, dy; // tốc độ di chuyển theo trục X, Y

    public MovableObject(float x, float y, int width, int height, float dx, float dy) {
        super(x, y, width, height);
        this.dx = dx;
        this.dy = dy;
    }

    public abstract void move();
}
