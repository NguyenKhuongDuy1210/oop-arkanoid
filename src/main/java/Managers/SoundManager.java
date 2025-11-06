package Managers;

import javafx.scene.media.AudioClip;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private static final Map<String, AudioClip> sounds = new HashMap<>();

    public static void loadSounds() {
        load("hit_brick", "sounds/hit_brick.wav");
        load("bounce", "sounds/bounce.wav");
        load("powerup", "sounds/powerup.wav");
        load("lose_life", "sounds/lose_life.wav");
        load("level_up", "sounds/level_up.wav");
        load("gameover", "sounds/gameover.wav");
    }

    private static void load(String key, String path) {
        URL resource = SoundManager.class.getClassLoader().getResource(path);
        if (resource != null) {
            sounds.put(key, new AudioClip(resource.toString()));
        } else {
            System.err.println("⚠️ Không tìm thấy âm thanh: " + path);
        }
    }

    public static void play(String key) {
        AudioClip clip = sounds.get(key);
        if (clip != null) clip.play();
    }

    public static void stop(String key) {
        AudioClip clip = sounds.get(key);
        if (clip != null) clip.stop();
    }
}
