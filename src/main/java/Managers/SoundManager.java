package Managers;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {

    private static final Map<String, AudioClip> sounds = new HashMap<>();
    private static MediaPlayer bgMusic = null; // nhạc nền menu/start

    private static boolean musicEnabled = true;
    private static boolean sfxEnabled = true;
    private static double musicVolume = 0.5; // Âm lượng nhạc nền
    private static double sfxVolume = 1.0; // Âm lượng hiệu ứng âm thanh

    public static void loadSounds() {
        load("hit_brick", "assets/sounds/hit_brick.wav");
        load("bonus", "assets/sounds/bonus.wav");
        loadBackgroundMusic("start_game", "assets/sounds/start_game.wav"); // nhạc nền
        load("level-up", "assets/sounds/level-up.wav");
        load("game-over", "assets/sounds/game-over.wav");
    }

    // load sound effect
    private static void load(String key, String path) {
        File file = new File(path);
        if (!file.exists()) {
            System.err.println("Không tìm thấy âm thanh: " + path);
            return;
        }
        sounds.put(key, new AudioClip(file.toURI().toString()));
    }

    // load nhạc nền, lặp vô hạn
    private static void loadBackgroundMusic(String key, String path) {
        File file = new File(path);
        if (!file.exists()) {
            System.err.println("Không tìm thấy nhạc nền: " + path);
            return;
        }
        Media media = new Media(file.toURI().toString());
        bgMusic = new MediaPlayer(media);
        bgMusic.setCycleCount(MediaPlayer.INDEFINITE); // lặp liên tục
        bgMusic.setVolume(musicVolume); // đặt âm lượng ban đầu
    }

    public static void play(String key) {
        if (key.equals("start_game")) {
            if (bgMusic != null && musicEnabled) {
                bgMusic.play(); // phát nhạc nền
            }
        } else {
            if (!sfxEnabled) {
                return;
            }

            AudioClip clip = sounds.get(key);

            if (clip != null) {
                clip.play(sfxVolume); // phát hiệu ứng âm thanh với âm lượng đã đặt
            }
        }
    }

    public static void stopBackgroundMusic() {
        if (bgMusic != null) {
            bgMusic.stop();
        }
    }

    public static void setBackgroundVolume(double volume) {
        if (bgMusic != null) {
            bgMusic.setVolume(volume); // 0.0 -> 1.0
        }
    }

    public static void toggleMusic() { // bật/tắt nhạc nền
        musicEnabled = !musicEnabled; // chuyển trạng thái
        if (musicEnabled) {
            if (bgMusic != null) {
                bgMusic.play(); // phát nhạc nền
            }
        } else {
            if (bgMusic != null) {
                bgMusic.stop(); // dừng nhạc nền
            }
        }
    }

    public static void toggleSfx() { // bật/tắt hiệu ứng âm thanh
        sfxEnabled = !sfxEnabled;
    }

    public static void setMusicVolume(double volume) { // đặt âm lượng nhạc nền
        musicVolume = Math.max(0.0, Math.min(1.0, volume)); // giới hạn trong khoảng 0.0 -> 1.0
        if (bgMusic != null) {
            bgMusic.setVolume(musicVolume); // cập nhật âm lượng nếu có nhạc nền đang chạy
        }
    }

    public static void setSfxVolume(double volume) { // đặt âm lượng hiệu ứng âm thanh
        sfxVolume = Math.max(0.0, Math.min(1.0, volume));
    }

    // Getters và Setters

    public static boolean isMusicEnabled() {
        return musicEnabled;
    }

    public static boolean isSfxEnabled() {
        return sfxEnabled;
    }

    public static double getMusicVolume() {
        return musicVolume;
    }

    public static double getSfxVolume() {
        return sfxVolume;
    }
    
}
