package carboard.test.ustwo.carboardplayground;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;

/**
 * Helper class for drawing 2D objects in a 3D space.  It has 2 canvases, one for each eye, it lets you draw
 * a bitmap and set it's apparent depth.  Objects still need to be drawn in the correct draw order
 *
 * @author luke@ustwo.com
 */
public class CardboardCanvas {

    /* Scale values to use */
    public static final int DEPTH_SCALE_DEFAULT = 50;
    public static final int DEPTH_SCALE_MORE = 75;
    public static final int DEPTH_SCALE_MOST = 100;

    /* The main canvas we draw everything to */
    private Canvas mCanvas;

    /*Canvases for the left and right side of the screen*/
    private Canvas mLeftCanvas;
    private Canvas mRightCanvas;

    /* The bitmaps that we draw to the main canvas which contain the data drawn
     to the left and right canvases */
    private Bitmap mLeftEyeBitmap;
    private Bitmap mRightEyeBitmap;

    /* Width and height of each half of the screen */
    private int mEyeWidth;
    private int mEyeHeight;

    /* The amount we scale the depth effect by, bigger means the furthest away something can be
     is increased */
    private int mDepthScale;

    /**
     * Default constructor, sets depth scale to the default value
     */
    public CardboardCanvas() {
        mDepthScale = DEPTH_SCALE_DEFAULT;
    }

    /**
     * Constructor that lets you set the depth scale
     *
     * @param depthScale The scale factor for the depth effect
     */
    public CardboardCanvas(int depthScale) {
        mDepthScale = depthScale;
    }

    /**
     * Set up the left and right eye canvases
     *
     * @param canvas The canvas the left and right eye canvases should be drawn to
     */
    public void setupCanvas(Canvas canvas) {
        mEyeWidth = canvas.getWidth() / 2;
        mEyeHeight = canvas.getHeight();

        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        mLeftEyeBitmap = Bitmap.createBitmap(mEyeWidth, mEyeHeight, conf);
        mRightEyeBitmap = Bitmap.createBitmap(mEyeWidth, mEyeHeight, conf);

        mLeftCanvas = new Canvas(mLeftEyeBitmap);
        mRightCanvas = new Canvas(mRightEyeBitmap);
    }

    /**
     * Sets the main canvas and clears the left and right ones.
     * The canvas needs to be set each frame
     *
     * @param canvas The main canvas to draw to
     */
    public void refreshCanvas(Canvas canvas) {
        mCanvas = canvas;
        // Clear the left and right canvases
        mLeftCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        mRightCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    }

    /**
     * Add a bitmap to the left and right canvases at a given position and depth
     *
     * @param bitmap The bitmap to add
     * @param left   Left position of the bitmap
     * @param top    Top position of the bitmap
     * @param depth  How far away the bitmap should appear value should be between 0 and 1 where 0 is the closer
     */
    public void addBitmap(Bitmap bitmap, float left, float top, float depth) {
        float scaledDepth = ((1.0f - depth) * mDepthScale) / 2.0f;
        mLeftCanvas.drawBitmap(bitmap, left + scaledDepth, (getEyeCanvasHeight() - bitmap.getHeight()) - top, null);
        mRightCanvas.drawBitmap(bitmap, left - scaledDepth, (getEyeCanvasHeight() - bitmap.getHeight()) - top, null);
    }

    /**
     * Render the scene by drawing the left and right bitmaps
     */
    public void draw() {
        mCanvas.drawBitmap(mLeftEyeBitmap, 0, 0, null);
        mCanvas.drawBitmap(mRightEyeBitmap, getEyeCanvasWidth(), 0, null);
    }

    /**
     * Gets the width of one eyes view
     *
     * @return
     */
    public int getEyeCanvasWidth() {
        return mEyeWidth;
    }

    /**
     * Gets the height of one eyes view
     *
     * @return
     */
    public int getEyeCanvasHeight() {
        return mEyeHeight;
    }
}
