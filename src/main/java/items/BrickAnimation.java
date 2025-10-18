package items;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class BrickAnimation {
    private final ImageView imageView;
    private final Timeline timeline;
    private final int frameCount;
    private final int frameWidth;
    private final int frameHeight;

    public BrickAnimation(Pane root, double x, double y,
                          String spritePath, int frameCount,
                          int frameWidth, int frameHeight, double frameDurationMs) {
        this.frameCount = frameCount;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;

        Image spriteSheet = new Image(getClass().getResourceAsStream(spritePath));
        imageView = new ImageView(spriteSheet);
        imageView.setViewport(new Rectangle2D(0, 0, frameWidth, frameHeight));
        imageView.setX(x);
        imageView.setY(y);

        root.getChildren().add(imageView);

        timeline = new Timeline();
        for (int i = 0; i < frameCount; i++) {
            final int frameIndex = i;
            KeyFrame kf = new KeyFrame(
                    Duration.millis(i * frameDurationMs),
                    e -> imageView.setViewport(new Rectangle2D(
                            frameIndex * frameWidth, 0, frameWidth, frameHeight
                    ))
            );
            timeline.getKeyFrames().add(kf);
        }

        // Khi xong animation thì remove hình vỡ
        timeline.setOnFinished(e -> root.getChildren().remove(imageView));
    }

    public void play() {
        timeline.play();
    }
}
