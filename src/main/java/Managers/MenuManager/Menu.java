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

import Managers.GameConfig.GameConfig;

public class Menu {

    private List<MenuItem> menuItems;
    private List<MenuItem> mainMenu;
    private List<MenuItem> pauseMenu;
    private List<MenuItem> settingsMenu;
    private int selectedItemIndex;

    private Image selectorIcon;
    private Font titleFont;
    private Font itemFont;

    private double selectorCurrentY;
    private double selectorTargetY;
    private final double LERP_SPEED = 0.15;

    private final double TITLE_X = GameConfig.SCREEN_X + 300;
    private final double TITLE_Y = 200;
    private final double MENU_START_X = GameConfig.SCREEN_X + 300;
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

        mainMenu = new ArrayList<>();
        mainMenu.add(new MenuItem("START", (int)MENU_START_X, (int)(MENU_START_Y)));
        mainMenu.add(new MenuItem("SETTINGS", (int)MENU_START_X, (int)(MENU_START_Y + MENU_ITEM_SPACING)));
        mainMenu.add(new MenuItem("EXIT", (int)MENU_START_X, (int)(MENU_START_Y + MENU_ITEM_SPACING * 2)));

        createPauseMenu();
        createSettingMenu();

        menuItems = mainMenu;
        
        selectedItemIndex = 0;

        if (!menuItems.isEmpty()) {
            updateTargetY();
            selectorCurrentY = selectorTargetY;
        }
    }

    private void createPauseMenu() {
        pauseMenu = new ArrayList<>();
        pauseMenu.add(new MenuItem("RESUME", (int)MENU_START_X, (int)(MENU_START_Y)));
        pauseMenu.add(new MenuItem("RESTART", (int)MENU_START_X, (int)(MENU_START_Y + MENU_ITEM_SPACING)));
        pauseMenu.add(new MenuItem("BACK TO MENU", (int)MENU_START_X, (int)(MENU_START_Y + MENU_ITEM_SPACING * 2)));
    }

    private void createSettingMenu() {
        settingsMenu = new ArrayList<>();
        settingsMenu.add(new MenuItem("SOUND", (int)MENU_START_X, (int)(MENU_START_Y)));
        settingsMenu.add(new MenuItem("BACK TO MENU", (int)MENU_START_X, (int)(MENU_START_Y + MENU_ITEM_SPACING * 2)));
    }

    public void switchToMainMenu() {
        menuItems = mainMenu;
        selectedItemIndex = 0;
        updateTargetY();
        selectorCurrentY = selectorTargetY;
    }

    public void switchToPauseMenu() {
        menuItems = pauseMenu;
        selectedItemIndex = 0;
        updateTargetY();
        selectorCurrentY = selectorTargetY;
    }

    public void switchToSettingMenu() {
        menuItems = settingsMenu;
        selectedItemIndex = 0;
        updateTargetY();
        selectorCurrentY = selectorTargetY;
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
        
        String title = "ARKANOID";

        if (menuItems == pauseMenu) {
            title = "PAUSED";
        } else if (menuItems == settingsMenu) {
            title = "SETTINGS";
        }
        
        gc.setTextAlign(TextAlignment.CENTER); // căn ngang giữa
        gc.setTextBaseline(VPos.CENTER); // căn dọc giữa
        gc.setFont(titleFont); // Đặt font cho tiêu đề
        gc.setFill(Color.WHITE); // Màu cho tiêu đề
        gc.fillText(title, TITLE_X, TITLE_Y); // Vẽ tiêu đề
        gc.setFont(itemFont); // Đặt font cho các mục menu

        for (MenuItem item : menuItems) {
            gc.setFill(Color.web("#ffffffff"));
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

    public MenuItem getSelectedItem() {
        return menuItems.get(selectedItemIndex); // trả về mục menu được chọn hiện tại
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

    public Font getTitleFont() {
        return titleFont;
    }
}