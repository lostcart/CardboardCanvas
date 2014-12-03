package carboard.test.ustwo.carboardplayground;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

/**
 * Example view that makes use of the cardboard canvas, scrolls four different images
 * at different rates to create a parallax scrolling effect
 *
 * @author luke@ustwo.com
 */
public class TestView extends View {
    /* Scroll speeds for the moving parts of the scene */
    private int SCROLL_SPEED_FRONT_MOUNTAINS = 150;
    private int SCROLL_SPEED_BACK_MOUNTAINS = 100;
    private int SCROLL_SPEED_FRONT_CLOUDS = 70;
    private int SCROLL_SPEED_BACK_CLOUDS = 40;

    /* Images used in the scene*/
    private Bitmap mFrontMountains, mBackMountains, mFrontClouds, mBackClouds;

    /* The y position of the floor */
    private int mFloorYPosition;

    /* Current x positions of the moving parts of scenery */
    private float mFrontMountainsXPosition, mBackMountainsXPosition, mBackCloudsXPosition, mFrontCloudsXPosition;

    /* The cardboard canvas to handle drawing the left and right eye views */
    private CardboardCanvas mCardboardCanvas;

    /* To check if the scene has been setup or not */
    private boolean mSetup;

    /* A boolean to set whether the scene should be scrolling or not */
    private boolean mScrolling;

    /* Last time the scene was drawn */
    private long mLastUpdateTime;

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * Initialises the bitmaps and the canvas
     */
    public void init() {
        // Create the cardboard canvas
        mCardboardCanvas = new CardboardCanvas(CardboardCanvas.DEPTH_SCALE_MOST);

        // Initialise the bitmaps
        mFrontMountains = BitmapFactory.decodeResource(getResources(), R.drawable.mountains_front);
        mBackMountains = BitmapFactory.decodeResource(getResources(), R.drawable.mountains_back);
        mFrontClouds = BitmapFactory.decodeResource(getResources(), R.drawable.clouds_front);
        mBackClouds = BitmapFactory.decodeResource(getResources(), R.drawable.clouds_back);
    }

    /**
     * Sets up the scene values, done seperately from init as the scene size isn't
     * known at creation
     *
     * @param canvas
     */
    private void setup(Canvas canvas) {
        mCardboardCanvas.setupCanvas(canvas);
        mFloorYPosition = mCardboardCanvas.getEyeCanvasHeight() * 2 / 5;
        mFloorYPosition = 0;
        mSetup = true;
    }

    /**
     * Sets whether the scene should be scrolling or not
     */
    public void toggleScrolling() {
        mScrolling = !mScrolling;
    }

    @Override
    public void draw(Canvas canvas) {
        // Set the background to a nice sky blue colour
        canvas.drawColor(Color.argb(255, 41, 171, 226));
        if (!mSetup) {
            setup(canvas);
        }
        // Set the canvas each frame
        mCardboardCanvas.refreshCanvas(canvas);

        long currentTime = System.currentTimeMillis();
        if (mLastUpdateTime != 0 && mScrolling) {
            updateBitmapPositions(currentTime - mLastUpdateTime);
        }
        // Add the images to the cardboard canvas
        mCardboardCanvas.addBitmap(mBackClouds, mBackCloudsXPosition, mFloorYPosition, 1);
        mCardboardCanvas.addBitmap(mFrontClouds, mFrontCloudsXPosition, mFloorYPosition, 0.6f);
        mCardboardCanvas.addBitmap(mBackMountains, mBackMountainsXPosition, mFloorYPosition, 0.3f);
        mCardboardCanvas.addBitmap(mFrontMountains, mFrontMountainsXPosition, mFloorYPosition, 0);

        // Draw the constructed scene
        mCardboardCanvas.draw();


        mLastUpdateTime = currentTime;
        invalidate();
    }

    /**
     * Scroll the bitmaps
     */
    private void updateBitmapPositions(long diffTime) {
        float multiplyValue = diffTime / 1000.0f;
        mBackCloudsXPosition -= (multiplyValue * SCROLL_SPEED_BACK_CLOUDS);
        mFrontCloudsXPosition -= (multiplyValue * SCROLL_SPEED_FRONT_CLOUDS);
        mBackMountainsXPosition -= (multiplyValue * SCROLL_SPEED_BACK_MOUNTAINS);
        mFrontMountainsXPosition -= (multiplyValue * SCROLL_SPEED_FRONT_MOUNTAINS);

        if (mFrontMountainsXPosition < -mFrontMountains.getWidth() / 2) {
            mFrontMountainsXPosition += mFrontMountains.getWidth() / 2;
        }

        if (mBackMountainsXPosition < -mBackMountains.getWidth() / 2) {
            mBackMountainsXPosition += mBackMountains.getWidth() / 2;
        }

        if (mBackCloudsXPosition < -mBackClouds.getWidth() / 2) {
            mBackCloudsXPosition += mBackClouds.getWidth() / 2;
        }

        if (mFrontCloudsXPosition < -mFrontClouds.getWidth() / 2) {
            mFrontCloudsXPosition += mFrontClouds.getWidth() / 2;
        }
    }
}
