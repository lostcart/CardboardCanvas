CardboardCanvas
===============

Simple test of drawing 2D items in a 3D space for the Google Cardboard.

This project makes use of the canvas by drawing the same image on the left and right side
of the screen slightly adjusted to give a 3D effect when used with the Google Cardboard.

## Usage

To setup the Cardboard canvas you want to do something like this.  The method takes in
an integer for setting how much of a 3D effect is applied.

```sh
mCardboardCanvas = new CardboardCanvas(CardboardCanvas.DEPTH_SCALE_MOST);
```

And then in your draw method you want to refresh the cardboard canvas, add images and
then draw the scene.
When you add a bitmap you can set the depth value with a float, 0 is a close as possible,
and 1 is the furthest possible

```sh
    @Override
    public void draw(Canvas canvas) {
   	    ...
        // Set the canvas each frame
        mCardboardCanvas.refreshCanvas(canvas);

        // Add the images to the cardboard canvas
        mCardboardCanvas.addBitmap(backBitmap, 0, 0, 1);
        mCardboardCanvas.addBitmap(closeBitmap, 0, 0, 0);

        // Draw the constructed scene
        mCardboardCanvas.draw();
        ...
    }
```

