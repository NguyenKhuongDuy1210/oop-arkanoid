package Managers.GameConfig;

import Managers.MenuManager.GameState;

public class GameConfig {
    // KÍCH THƯỚC MÀN HÌNH GAME
    public static final int SCREEN_PLAY_WIDTH = 600;
    public static final int SCREEN_PLAY_HEIGHT = 800;
    public static final int SCREEN_X = 400;
    public static final int SCREEN_Y = 0;
    // KÍCH THƯỚC MÀN HÌNH
    public static final int SCREEN_WIDTH = 1400;
    public static final int SCREEN_HEIGHT = 801;
    // KÍCH THƯỚC VÀ TỐC ĐỘ PADDLE
    public static final int PADDLE_WIDTH = 120;
    public static final int PADDLE_HEIGHT = 20;
    // KÍCH THƯỚC VÀ TỐC ĐỘ BÓNG
    public static final int BALL_WIDTH = 15;
    public static final int BALL_HEIGHT = 15;
    public static final float BALL_SPEED = 6;
    // KÍCH THƯỚC VÀ KHOẢNG CÁCH GẠCH
    public static final int BRICK_WIDTH = 50;
    public static final int BRICK_HEIGHT = 25;
    public static final int BRICK_GAP = 0;
    //
    public static final GameState DEFAULT_STATE = GameState.Menu;

    // Có thể thêm các giá trị mặc định khác như âm thanh, độ khó, v.v.
    public static final int DEFAULT_VOLUME = 70;

    // Hàm khởi tạo hoặc reset nếu cần
    public static void resetToDefault() {
        // Logic để reset game về trạng thái mặc định
    }
}