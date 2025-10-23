package items.effects;

public class Animation {
    private int[][] frame_clips;

    public void createFrame_clips(int frame_number, int width_frame, int height_frame) {
        frame_clips = new int[frame_number][4];
        for (int i = 0; i < 10; i++) {
            frame_clips[i][0] = i * width_frame;
            frame_clips[i][1] = 0;
            frame_clips[i][2] = width_frame;
            frame_clips[i][3] = height_frame;
        }
    }

    public int[][] getFrame_clips() {
        return frame_clips;
    }
}
