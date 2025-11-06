
import items.Ball;
import items.Brick;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CollisionTest {

    private Ball ball;
    private Brick brick;

    @BeforeEach
    public void setUp() {
        // Giả lập brick, không cần ảnh
        ball = new Ball(100, 100, 10, 10, 5, 1, 1);
        brick = new Brick(null, 105, 105, 50, 20, 1);
    }

    @Test
    public void testBallCollidesWithBrick() {
        assertTrue(ball.checkCollision(brick), "Ball should collide with brick");
    }

    @Test
    public void testBallDoesNotCollideWhenFar() {
        brick = new Brick(null, 300, 300, 50, 20, 1);
        assertFalse(ball.checkCollision(brick), "Ball should NOT collide when far away");
    }

    @Test
    public void testCollisionDecreasesHitPoints() {
        if (ball.checkCollision(brick)) {
            brick.sethitPoints(brick.gethitPoints() - 1);
        }
        assertEquals(0, brick.gethitPoints(), "Brick hitPoints should decrease after collision");
    }
}
