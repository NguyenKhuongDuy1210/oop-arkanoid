package BaseObject;

import java.awt.Graphics;

public abstract class GameObject {
    protected float x, y;
    protected int width, height;
    public GameObject(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // Phương thức trừu tượng
    public abstract void update();

    // Getter & Setter
    public void setX(float x) { this.x=x; }
    public void setY(float y) { this.y=y; }
    public float getX() { return x; }
    public float getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public void setWidth(int Width)
    {
        this.width=Width;
    }
    public void setHeight(int H)
    {
        this.height=H;
    }

}
