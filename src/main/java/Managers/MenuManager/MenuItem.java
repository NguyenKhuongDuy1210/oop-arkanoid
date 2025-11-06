package Managers.MenuManager;

public class MenuItem {
    private String text;
    private int x, y;

    public MenuItem(String text, int x, int y) {
        this.text = text;
        this.x = x;
        this.y = y;
    }

    public String getText() {
        return text;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setText(String text) {
        this.text = text;
    }
}