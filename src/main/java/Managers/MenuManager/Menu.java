// Trong Managers/MenuManager/Menu.java
package Managers.MenuManager;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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
    private final double LERP_SPEED = 0.15;

    private final double TITLE_X = 300;
    private final double TITLE_Y = 200;
    private final double MENU_START_X = 300;
    private final double MENU_START_Y = 350;
    private final double MENU_ITEM_SPACING = 60;
    private final double SELECTOR_PADDING = 25;

    public Menu() {
        try {
            titleFont = Font.loadFont(new FileInputStream("assets/fonts/Cinzel-Bold.ttf"), 70);
            itemFont = Font.loadFont(new FileInputStream("assets/fonts/Cinzel-Regular.ttf"), 32);
            selectorIcon = new Image(new FileInputStream("assets/selector.png"));
        } catch (FileNotFoundException e) {
            System.err.println("LỖI: Không tìm thấy file tài nguyên (font/icon). " + e.getMessage());
            titleFont = new Font("Arial Black", 70);
            itemFont = new Font("Arial", 32);
        } catch (Exception e) {
            System.err.println("Lỗi không xác định khi tải tài nguyên: " + e.getMessage());
            e.printStackTrace();
            titleFont = new Font("Arial Black", 70);
            itemFont = new Font("Arial", 32);
        }

        menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("START", (int)MENU_START_X, (int)(MENU_START_Y)));
        menuItems.add(new MenuItem("OPTION", (int)MENU_START_X, (int)(MENU_START_Y + MENU_ITEM_SPACING)));
        menuItems.add(new MenuItem("EXIT", (int)MENU_START_X, (int)(MENU_START_Y + MENU_ITEM_SPACING * 2)));
        
        selectedItemIndex = 0;

        if (!menuItems.isEmpty()) {
            updateTargetY();
            selectorCurrentY = selectorTargetY;
        }
    }
    
    private void updateTargetY() {
        if (!menuItems.isEmpty()) {
            MenuItem selectedItem = menuItems.get(selectedItemIndex);
            selectorTargetY = selectedItem.getY() - (itemFont.getSize() / 2);
        }
    }

    public void handleMouseMove(double mouseX, double mouseY) {
        for (int i = 0; i < menuItems.size(); i++) {
            MenuItem item = menuItems.get(i);
            
            Text textNode = new Text(item.getText());
            textNode.setFont(itemFont);
            double textWidth = textNode.getLayoutBounds().getWidth();
            // THAY ĐỔI Ở ĐÂY: Giảm chiều cao của hộp va chạm để nó "ôm sát" vào chữ hơn
            double textHeight = textNode.getLayoutBounds().getHeight() * 0.7;

            double xMin = item.getX() - textWidth / 2.0;
            double xMax = item.getX() + textWidth / 2.0;
            double yMin = item.getY() - textHeight / 2.0;
            double yMax = item.getY() + textHeight / 2.0;

            if (mouseX >= xMin && mouseX <= xMax && mouseY >= yMin && mouseY <= yMax) {
                if (selectedItemIndex != i) {
                    selectedItemIndex = i;
                    updateTargetY();
                }
                return;
            }
        }
    }

    public void update() {
        selectorCurrentY += (selectorTargetY - selectorCurrentY) * LERP_SPEED;
    }

    public void render(GraphicsContext gc) {
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);

        gc.setFont(titleFont);
        gc.setFill(Color.WHITE);
        gc.fillText("ARKANOID", TITLE_X, TITLE_Y);

        gc.setFont(itemFont);
        for (MenuItem item : menuItems) {
            gc.setFill(Color.web("#CCCCCC"));
            gc.fillText(item.getText(), item.getX(), item.getY());
        }

        if (!menuItems.isEmpty() && selectorIcon != null) {
            MenuItem selectedItem = menuItems.get(selectedItemIndex);

            Text textNode = new Text(selectedItem.getText());
            textNode.setFont(itemFont);
            double textWidth = textNode.getLayoutBounds().getWidth();

            double leftIconX = selectedItem.getX() - (textWidth / 2) - SELECTOR_PADDING - selectorIcon.getWidth();
            double rightIconX = selectedItem.getX() + (textWidth / 2) + SELECTOR_PADDING;

            double iconY = selectorCurrentY + (itemFont.getSize() / 2) - (selectorIcon.getHeight() / 2);

            gc.drawImage(selectorIcon, leftIconX, iconY);

            gc.save();
            gc.translate(rightIconX + selectorIcon.getWidth(), iconY);
            gc.scale(-1, 1);
            gc.drawImage(selectorIcon, 0, 0);
            gc.restore();
        }
    }

    public void navigateUp() {
        selectedItemIndex = (selectedItemIndex - 1 + menuItems.size()) % menuItems.size();
        updateTargetY();
    }

    public void navigateDown() {
        selectedItemIndex = (selectedItemIndex + 1) % menuItems.size();
        updateTargetY();
    }

    public int getSelectedItemIndex() {
        return selectedItemIndex;
    }
}