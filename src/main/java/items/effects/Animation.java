package items.effects;

public class Animation {
    private int[][] frame_clips;

    /**
     * Tạo frame clips từ sprite sheet.
     * @param frame_number số frame
     * @param width_frame  chiều rộng mỗi frame
     * @param height_frame chiều cao mỗi frame
     */
    public void createFrame_clips(int frame_number, int width_frame, int height_frame) {
        frame_clips = new int[frame_number][4];
        for (int i = 0; i < frame_number; i++) {
            frame_clips[i][0] = i * width_frame; // x
            frame_clips[i][1] = 0;              // y
            frame_clips[i][2] = width_frame;     // width
            frame_clips[i][3] = height_frame;    // height
        }
    }

    /**
     * Lấy tất cả frame clips.
     */
    public int[][] getFrame_clips() {
        return frame_clips;
    }

    /**
     * Lấy clip của một frame cụ thể.
     * @param index chỉ số frame (0-based)
     * @return mảng [x, y, width, height]
     */
    public int[] getFrame(int index) {
        if (frame_clips == null || index < 0 || index >= frame_clips.length) return null;
        return frame_clips[index];
    }

    /**
     * Lấy tổng số frame.
     */
    public int getFrameCount() {
        return frame_clips != null ? frame_clips.length : 0;
    }
}
