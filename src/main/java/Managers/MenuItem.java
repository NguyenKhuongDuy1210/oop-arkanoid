package Managers;

public class MenuItem {
    private String text;
    private int x, y; // Sử dụng số nguyên (int) cho tọa độ

    // Constructor nhận vào một Chuỗi (String) và hai số nguyên (int)
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
}