// Trong Managers/Menu.java
package Managers;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Menu {

    private List<MenuItem> menuItems;
    private int selectedItemIndex;

    private Image selectorIcon;
    private Font titleFont;
    private Font itemFont;

    private double selectorCurrentY;
    private double selectorTargetY;
    private final double LERP_SPEED = 0.2;

    public Menu() {
        try {
            // Load tài nguyên từ thư mục assets
            titleFont = Font.loadFont(new FileInputStream("assets/fonts/Cinzel-Bold.ttf"), 70);
            itemFont = Font.loadFont(new FileInputStream("assets/fonts/Cinzel-Regular.ttf"), 32);
            selectorIcon = new Image(new FileInputStream("assets/selector.png"));

        } catch (FileNotFoundException e) {
            System.err.println("LỖI: Không tìm thấy file tài nguyên (font/icon). " + e.getMessage());
            // Sử dụng font mặc định để game không bị crash
            titleFont = new Font("Arial Black", 70);
            itemFont = new Font("Arial", 32);
        } catch (Exception e) {
            e.printStackTrace();
            titleFont = new Font("Arial Black", 70);
            itemFont = new Font("Arial", 32);
        }

        menuItems = new ArrayList<>();
        // Sử dụng số nguyên (int) khi tạo MenuItem
        menuItems.add(new MenuItem("Bắt đầu", 300, 380));
        menuItems.add(new MenuItem("Thoát", 300, 440));
        selectedItemIndex = 0;

        // Khởi tạo vị trí ban đầu cho icon bộ chọn
        if (!menuItems.isEmpty()) {
            MenuItem firstItem = menuItems.get(selectedItemIndex);
            // Căn lề Y của icon dựa trên chiều cao của nó
            double iconOffsetY = (selectorIcon != null) ? selectorIcon.getHeight() / 2 : 0;
            selectorCurrentY = firstItem.getY() - iconOffsetY;
            selectorTargetY = selectorCurrentY;
        }
    }

    // Phương thức này cập nhật logic của menu (như chuyển động)
    public void update() {
        // Di chuyển vị trí hiện tại của icon một khoảng bằng 20% quãng đường còn lại
        selectorCurrentY = selectorCurrentY + (selectorTargetY - selectorCurrentY) * LERP_SPEED;
    }

    public void render(GraphicsContext gc) {
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);

        // Vẽ tiêu đề game
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 60));
        gc.fillText("ARKANOID", 300, 200);

        // Vẽ các lựa chọn menu
        for (int i = 0; i < menuItems.size(); i++) {
            MenuItem item = menuItems.get(i);
            if (i == selectedItemIndex) {
                gc.setFill(Color.YELLOW); // Màu cho lựa chọn đang được chọn
            } else {
                gc.setFill(Color.WHITE);
            }
            gc.setFont(new Font("Arial", 30));
            gc.fillText(item.getText(), item.getX(), item.getY());
        }
    }

    public void navigateUp() {
        selectedItemIndex--;
        if (selectedItemIndex < 0) {
            selectedItemIndex = menuItems.size() - 1;
        }
    }

    public void navigateDown() {
        selectedItemIndex++;
        if (selectedItemIndex >= menuItems.size()) {
            selectedItemIndex = 0;
        }
    }

    public int getSelectedItemIndex() {
        return selectedItemIndex;
    }
}
