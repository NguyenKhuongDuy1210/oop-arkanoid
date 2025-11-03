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

    private List<MenuItem> menuItems; // danh sách mục menu hiện tại
    private List<MenuItem> mainMenu; // danh sách mục menu chính
    private List<MenuItem> pauseMenu; // danh sách mục menu tạm dừng
    private List<MenuItem> settingsMenu; // danh sách mục menu cài đặt

    private int selectedItemIndex; // chỉ số mục menu được chọn hiện tại

    private Image selectorIcon; // hình ảnh con trỏ
    private Font titleFont; // font tiêu đề
    private Font itemFont; // font các mục của menu

    private double selectorCurrentY; // vị trí Y hiện tại của con trỏ
    private double selectorTargetY; // vị trí Y mục tiêu của con trỏ
    private final double LERP_SPEED = 0.15; // tốc độ nội suy tuyến tính

    private final double TITLE_X = GameConfig.SCREEN_X + 300; // vị trí X của tiêu đề
    private final double TITLE_Y = 200; // vị trí Y của tiêu đề
    private final double MENU_START_X = GameConfig.SCREEN_X + 300; //  vị trí X bắt đầu của menu
    private final double MENU_START_Y = 350; // vị trí Y bắt đầu của menu
    private final double MENU_ITEM_SPACING = 60; // khoảng cách giữa các mục menu
    private final double SELECTOR_PADDING = 25; // khoảng cách giữa con trỏ và mục menu

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
        mainMenu.add(new MenuItem("START", (int) MENU_START_X, (int) (MENU_START_Y))); // thêm mục "START" vào menu chính
        mainMenu.add(new MenuItem("OPTIONS", (int) MENU_START_X, (int) (MENU_START_Y + MENU_ITEM_SPACING))); // thêm mục "OPTIONS" vào menu chính
        mainMenu.add(new MenuItem("EXIT", (int) MENU_START_X, (int) (MENU_START_Y + MENU_ITEM_SPACING * 2))); //  thêm mục "EXIT" vào menu chính

        createPauseMenu(); // tạo menu tạm dừng
        createSettingMenu(); // tạo menu cài đặt

        menuItems = mainMenu; // khởi đầu với menu chính

        selectedItemIndex = 0; // mục được chọn ban đầu là mục đầu tiên (START)

        if (!menuItems.isEmpty()) {
            updateTargetY();
            selectorCurrentY = selectorTargetY;
        }
    }

    private void createPauseMenu() { // tạo menu tạm dừng
        pauseMenu = new ArrayList<>();
        pauseMenu.add(new MenuItem("RESUME", (int) MENU_START_X, (int) (MENU_START_Y)));
        pauseMenu.add(new MenuItem("RESTART", (int) MENU_START_X, (int) (MENU_START_Y + MENU_ITEM_SPACING)));
        pauseMenu.add(new MenuItem("BACK TO MENU", (int) MENU_START_X, (int) (MENU_START_Y + MENU_ITEM_SPACING * 2)));
    }

    private void createSettingMenu() { // tạo menu cài đặt
        settingsMenu = new ArrayList<>();
        settingsMenu.add(new MenuItem("SOUND", (int) MENU_START_X, (int) (MENU_START_Y)));
        settingsMenu.add(new MenuItem("BACK TO MENU", (int) MENU_START_X, (int) (MENU_START_Y + MENU_ITEM_SPACING * 2)));
    }

    public void switchToMainMenu() {
        menuItems = mainMenu; // chuyển sang menu chính
        selectedItemIndex = 0; // đặt mục được chọn về mục đầu tiên (START)
        updateTargetY(); // cập nhật vị trí mục được chọn
        selectorCurrentY = selectorTargetY; // đặt vị trí con trỏ hiện tại bằng vị trí mục được chọn
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

    public void updateTargetY() { // cập nhật vị trí Y của con trỏ dựa trên mục được chọn
        if (!menuItems.isEmpty()) {
            MenuItem selectedItem = menuItems.get(selectedItemIndex); // lấy mục được chọn hiện tại
            selectorTargetY = selectedItem.getY() - (itemFont.getSize() / 2); // tính toán vị trí Y mục tiêu của con trỏ    
        }
    }

    public void update() { // cập nhật vị trí Y hiện tại của con trỏ bằng nội suy tuyến tính
        selectorCurrentY += (selectorTargetY - selectorCurrentY) * LERP_SPEED;
    }

    public MenuItem getSelectedItem() {
        return menuItems.get(selectedItemIndex); // trả về mục menu được chọn hiện tại
    }

    public void navigateUp() { // di chuyển lựa chọn lên trên
        selectedItemIndex = (selectedItemIndex - 1 + menuItems.size()) % menuItems.size();
        updateTargetY();
    }

    public void navigateDown() { // di chuyển lựa chọn xuống dưới
        selectedItemIndex = (selectedItemIndex + 1) % menuItems.size();
        updateTargetY();
    }

    public int getSelectedItemIndex() {
        return selectedItemIndex;
    }

    public Font getTitleFont() {
        return titleFont;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public Font getItemFont() {
        return itemFont;
    }

    public void setSelectedItemIndex(int index) {
        selectedItemIndex = index;
    }

    public List<MenuItem> getPauseMenu() {
        return pauseMenu;
    }

    public List<MenuItem> getSettingsMenu() {
        return settingsMenu;
    }

    public Image getSelectorIcon() {
        return selectorIcon;
    }

    public double getSelectorCurrentY() {
        return selectorCurrentY;
    }

    public double getTitleX() {
        return TITLE_X;
    }

    public double getTitleY() {
        return TITLE_Y;
    }
}
