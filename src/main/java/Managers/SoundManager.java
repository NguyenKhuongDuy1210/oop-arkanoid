package Managers;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {

    private static final Map<String, AudioClip> sounds = new HashMap<>();
    private static MediaPlayer StartGame = null;

    public static void loadSounds() {
        load("hit_brick", "assets/sounds/hit_brick.wav");
        load("bonus", "assets/sounds/bonus.wav");
        //load("powerup", "assets/sounds/powerup.wav");
        load("start_game", "assets/sounds/start_game.wav");
        load("level-up", "assets/sounds/level-up.wav");
        load("game-over", "assets/sounds/game-over.wav");
    }

    private static void load(String key, String path) {
        File file = new File(path);
        if (!file.exists()) {
            System.err.println("Không tìm thấy âm thanh: " + path);
            return;
        }

        if (key.equals("start_game")) {
            Media media = new Media(file.toURI().toString());
            StartGame = new MediaPlayer(media);
        } else {
            sounds.put(key, new AudioClip(file.toURI().toString()));
        }
    }

    public static void play(String key) {
        if (key.equals("start_game")) {
            if (StartGame != null) {
                StartGame.stop(); // reset
                StartGame.play();
            }
        } else {
            AudioClip clip = sounds.get(key);
            if (clip != null) clip.play();
        }
    }

}
