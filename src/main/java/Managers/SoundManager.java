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
        bgMusic.setVolume(0.5); // âm lượng mặc định
    }

    public static void play(String key) {
        if (key.equals("start_game")) {
            if (bgMusic != null) {
                bgMusic.play(); // không stop nữa, tránh reset
            }
        } else {
            AudioClip clip = sounds.get(key);
            if (clip != null) clip.play();
        }
    }

    public static void stopBackgroundMusic() {
        if (bgMusic != null) bgMusic.stop();
    }

    public static void setBackgroundVolume(double volume) {
        if (bgMusic != null) bgMusic.setVolume(volume); // 0.0 -> 1.0
    }
}
