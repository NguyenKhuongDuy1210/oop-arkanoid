package BaseObject;

import java.awt.Graphics;

public abstract class GameObject {
    protected float x, y;
    protected int width, height;
    protected boolean visible = true;
    public GameObject(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // Phương thức trừu tượng
    public abstract void update();

    // Getter & Setter
    public boolean isVisible()
    {
        return visible;
    }
    public void setVisible(boolean visible)
    {
        this.visible=visible;
    }
    public float getX() { return x; }
    public float getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
