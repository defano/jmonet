# JMonet

[Getting Started](#getting-started) | [Tools](#paint-tools) | [Transforms](#image-transforms) | [Brushes](#creating-complex-brush-shapes) | [Cut, Copy and Paste](#cut-copy-and-paste) | [FAQs](#frequently-asked-questions)

An easy-to-use toolkit for incorporating paint tools like those found in [MacPaint](https://en.wikipedia.org/wiki/MacPaint) or [Microsoft Paint](https://en.wikipedia.org/wiki/Microsoft_Paint) into a Java Swing or JavaFX application (does not support Android).

This project provides the paint capabilities found in [WyldCard](https://github.com/defano/wyldcard) (an open-sourced clone of Apple's HyperCard).

[![Build Status](https://travis-ci.org/defano/jmonet.svg?branch=master)](https://travis-ci.org/defano/jmonet)

## Features

* Common suite of paint tools with observable attributes for colors and patterns, line sizes, anti-aliasing modes, etc.
* Includes image transform tools like scale, rotate, flip, shear, perspective and projection, plus the ability to adjust color depth, transparency and brightness.
* Canvas can be scaled and displayed within a scrollable pane; tools can be snapped to a grid.
* Supports multi-operation undo and redo, plus cut, copy and paste integration with the system clipboard.
* Paint and edit 24-bit, true-color images with alpha transparency; images are backed by a standard Java `BufferedImage` object making it easy to import and export graphics.
* Lightweight toolkit integrates easily into Swing and JavaFX applications and utilizes [ReactiveX](https://github.com/ReactiveX/RxJava) for observables.

## Paint Tools

Icon                                  | Tool         | Description
--------------------------------------|--------------|--------------
![Airbrush](icons/spraypaint.png)     | Airbrush     | Paints translucent color or texture onto the canvas.
![Curve](icons/curve.png)             | Curve        | Draws quadratic (Bezier) curves by clicking to specify points on the curve.
![Eraser](icons/eraser.png)           | Eraser       | Removes paint from the canvas by restoring affected pixels to their fully-transparent state.
![Fill](icons/fill.png)               | Fill         | Shades an enclosed area with paint using a [flood-fill](https://en.wikipedia.org/wiki/Flood_fill) algorithm.
![Line](icons/line.png)               | Line         | Draws straight lines; hold `shift` to restrict lines to 15-degree angles.
![Oval](icons/oval.png)               | Oval         | Draws filled or outlined oval shapes; hold `shift` to constrain boundary to circle.
![Paintbrush](icons/paintbrush.png)   | Paintbrush   | Draws paint on the canvas (using configurable stroke and color/texture).
![Pencil](icons/pencil.png)           | Pencil       | Draws or erases a free-form, single-pixel path on the canvas.
![Polygon](icons/polygon.png)         | Polygon      | Draws filled or outlined irregular polygons by clicking to specify points. Double-click to close the polygon; press `esc` to keep only the lines visible; hold `shift` to restrict line angles to 15 degree multiples.
![Freeform](icons/freeform.png)       | Freeform     | Draws a closed, free-form shape on the canvas. Click and drag to draw a path; release the mouse to close the shape.
![Rectangle](icons/rectangle.png)     | Rectangle    | Draws filled or stroked rectangles on the canvas; hold `shift` to constrain boundary to a square.
![Round Rect](icons/roundrect.png)    | Round Rect   | Draws filled or stroked round-rectangles on the canvas.
![Shape](icons/shape.png)             | Shape        | Draws filled or stroked regular polygons (i.e., shapes--triangles, squares, polygons, hexagons, etc.)
![Text](icons/text.png)               | Text         | Draws rasterized text of configurable font, size and style on the canvas. Text remains editable until user clicks away.

### Selection tools

Icon                                  | Tool         | Description
--------------------------------------|--------------| -------------------------
![Lasso](icons/lasso.png)             | Lasso        | Define a free-form selection boundary ([marching ants](https://en.wikipedia.org/wiki/Marching_ants)) for clearing or moving paint.
![Selection](icons/selection.png)     | Selection    | Define a selection rectangle whose underlying graphic can be moved or cleared (press `delete`)

### Transform tools

Icon                                  | Tool         | Description
--------------------------------------|--------------| -------------------------
![Rotate](icons/rotate.png)           | Rotate       | Define a selection, then use the drag handle to free-rotate the selected graphic around its center. Hold shift to restrict rotation angle to 15-degree increments.
![Slant](icons/slant.png)             | Slant        | Define a selection, then use the drag handles to apply an affine shear transform to the selected graphic.
![Scale](icons/scale.png)             | Scale        | Define a selection, then expand or shrink the selected image by dragging a handle. Hold shift to maintain selection's original aspect ratio.
![Projection](icons/distort.png)      | Projection   | Define a selection, then use the drag handles to project the image onto the geometry of an arbitrary quadrilateral.
![Perspective](icons/perspective.png) | Perspective  | Define a selection, then use the drag handles to warp the image onto an isosceles trapezoid, providing the effect of the left or right side of the image appearing nearer or farther from the viewer.
![Rubber Sheet](icons/distort.png)    | Rubber Sheet | Similar to the projection transform, but utilizes a "rubber sheet" algorithm that preserves relative position over linearity.

### Canvas tools

Icon                                  | Tool         | Description
--------------------------------------|--------------| -------------------------
![Arrow](icons/arrow.png)             | Arrow        | A no-op tool (does not modify the canvas).
![Magnifier](icons/magnifier.png)     | Magnifier    | Zoom in (scale the canvas) at the location clicked; hold `shift` to zoom out or `ctrl` to restore normal zoom.

## Getting started

#### 1. Install the library

JMonet is published to Maven Central; include the library in your Maven project's POM, like:

```
<dependency>
    <groupId>com.defano.jmonet</groupId>
    <artifactId>jmonet</artifactId>
    <version>0.3.2</version>
</dependency>
```

... or your Gradle build script:

```
repositories {
	mavenCentral()
}

dependencies {
  compile 'com.defano.jmonet:jmonet:0.3.2'
}
```

#### 2. Create a canvas

JMonet integrates easily into Java Swing and JavaFX applications. Simply create a canvas node or panel and add it to a window in your application:

In Swing applications:

```
public static void main(String[] args) {
    javax.swing.SwingUtilities.invokeLater(() -> {

        // Create and show Swing frame
        JFrame frame = new JFrame("My Pretty Picture");
        frame.setPreferredSize(new Dimension(640, 480));
        frame.pack();
        frame.setVisible(true);

        // Create a JMonet canvas and add it to the window
        JMonetCanvas myCanvas = new JMonetCanvas(new Dimension(640, 480));
        frame.getContentPane().add(myCanvas);
    });
}
```

In JavaFX applications:

```
@Override
public void start(Stage stage) {

    // Create a JFX node for our paint canvas
    JFXPaintCanvasNode myCanvas = new JFXPaintCanvasNode(new JMonetCanvas(new Dimension(640, 480)));

    // Create a pane for it
    StackPane pane = new StackPane();
    pane.getChildren().add(myCanvas);

    // And add it to our stage
    stage.setScene(new Scene(pane, 640, 480));
    stage.show();
}

```

#### 3. Pick up a tool

Start painting by making a tool active on the canvas with the `PaintToolBuilder`:

```
PaintTool paintbrush = PaintToolBuilder.create(PaintToolType.PAINTBRUSH)
    .withStroke(StrokeBuilder.withShape().ofCircle(8).build())
    .withStrokePaint(Color.RED)
    .makeActiveOnCanvas(myCanvas)
    .build();
```

Voila! You're ready to start painting a round, 8-pixel, bright red brushstroke onto your canvas. Don't forget to deactivate this tool when you're done with it or wish to start using another tool:

```
// When you're done painting or ready for a different tool...
paintbrush.deactivate();
```

There's no technical limitation that prevents multiple tools from being active on the same canvas at the same time, but that's not usually desired behavior in a paint program.

## Image transforms

The following image transforms may be applied to an image selection (that is, a portion of the canvas selected by the lasso or selection rectangle tool). Once a selection has been made, invoke one of the following methods on the `PaintTool` object to transform its selection:

Transform Method        | Description
------------------------|-------------------------
`adjustBrightness`      | Changes the brightness/luminosity of all pixels in the selected image by adding `delta` to each red, green and blue color channel value (a value between 0..255, where 0 is completely dark, 255 is completely light).
`adjustTransparency`    | Changes the opacity of each pixel in the selected image by adding `delta` the alpha channel (a value between 0..255 where 0 is fully transparent and 255 is fully opaque).
`removeTranslucency`    | Makes translucent pixels either fully transparent of fully opaque.          
`applyTransform`        | Applies an `AffineTransform` or a `PixelTransform` to the selection.
`fill`                  | Fills any fully-transparent pixels in the selection with a given `Paint`.
`flipHorizontal`        | Flips the selection about a vertical axis drawn through the center of the image.
`flipVertical`          | Flips the selection about a horizontal axis drawn through the center of the image.
`invert`                | Inverts the color
`pickupSelection`       | "Picks up" the pixels on the canvas that are currently within the bounds of the selection and adds them to the selection. Useful when moving a selection over another region of the canvas.
`reduceColor`           | Performs a color quantization and dithers the result using a specified dithering algorithm. See the `com.defano.algo.dither` package for available dithering implementations (or implement your own).
`reduceGreyscale`       | Performs a luminosity quantization and dithers the result using a specified dithering algorithm.
`rotateLeft`            | Rotates the selection 90 degrees counter-clockwise.
`rotateRight`           | Rotates the selection 90 degrees clockwise.

Each of these transforms is implemented as a standalone class in the `com.defano.jmonet.algo.transform` package hierarchy, making it easy to apply these programmatically (offline) to a `BufferedImage` object.

## Creating complex brush shapes

A stroke represents the size and shape of the "pen" used to mark the outline of a shape or path.

JMonet's `ShapeStroke` class can produce a stroke of any arbitrary shape (even the shape of text). A builder class (`StrokeBuilder`) provides a convenient mechanism for creating both `BasicStroke` or `ShapeStroke` objects. Note that `StrokeBuilder` replaces the `BasicBrush` enumeration present in older versions of the library.

To produce a stroke in the shape of a vertical line, 20 pixels tall:

```
StrokeBuilder.withShape()
    .ofVerticalLine(20)
    .build();
```

Stroked shapes can be transformed, too. This produces a 20x10 parallelogram stroke:

```
StrokeBuilder.withShape()
    .ofRectangle(20, 10)
    .sheared(1, 0)
    .build();
```

Shapes can be combined together to create a composite stroke shape. This example produces a stroke in the shape of the international [no symbol](https://en.wikipedia.org/wiki/No_symbol):

```
StrokeBuilder.withShape()
    .ofCircle(48)         // draw circle
    .outlined(6)          // ... outlined with 6px border, not filled
    .ofRectangle(6, 60)   // draw slash (6px wide, 60px long)
    .rotated(-45)         // ... rotate it 45 degrees
    .build();
```

Instances of Java's `BasicStroke` can also be created with this builder. This example produces an 8-pixel stroke with a 10-pixel dashed pattern:

```
StrokeBuilder.withBasicStroke()
    .ofWidth(8)
    .withDash(10)
    .build();
```

## Cut, Copy and Paste

JMonet makes it easy to integrate cut, copy and paste functions into your app that utilize the operating system's clipboard so that you can copy and paste graphics from within your own application or between other applications on your system.

A bit of integration is required to connect these functions to the UI elements in your app (like menu items or toolbar buttons) that a user will interact with.

#### 1. Route actions to the canvas

The JMonet canvas will not receive cut, copy or paste actions until we register an `ActionListener` that routes those actions to it. Typically, only the user interface element that has focus receives such events, but because a canvas has no clear concept of focus, it's up to you to decide when the canvas should respond to cut, copy and paste actions.

Create an `ActionListener` to send actions to the canvas:

```
CanvasClipboardActionListener myActionListener = new CanvasClipboardActionListener(new CanvasFocusDelegate() {
    @Override
    public AbstractPaintCanvas getCanvasInFocus() {

        // Should our canvas handle cut, copy and paste commands right now?
        if (isMyCanvasInFocus()) {
            return myCanvas;
        }

        // If not, return null
        return null;
    }
});
```

Then, add this `ActionListener` to whichever user interface elements will generate cut, copy and paste events. Most commonly this would be added to menu items but could also be used with buttons on a toolbar, for example:

```
JMenuItem myCopyMenuItem = new JMenuItem(new DefaultEditorKit.CopyAction());
copyMenuItem.setName("Copy");
copyMenuItem.setActionCommand((String) TransferHandler.getCopyAction().getValue(Action.NAME))
copyMenuItem.addActionListener(myActionListener);

myEditMenu.add(myCopyMenuItem);
```

#### 2. Add the transfer handler

Your application needs to tell JMonet what to do when the user has invoked the cut, copy or paste action. Typically, this simply involves getting or setting the selection defined by a selection tool (i.e., any tool which subclasses `AbstractSelectionTool`). Of course, you're free to provide alternate behavior (like copying the entire canvas, instead of just the selection).

The code below provides an implementation that cuts, copies and pastes the active selection.

```
  JMonetCanvas myCanvas = new JMonetCanvas();

  myCanvas.setTransferHandler(new CanvasTransferHandler(myCanvas, new CanvasTransferDelegate() {
    @Override
    public BufferedImage copySelection() {
      if (myActiveTool instanceof AbstractSelectionTool) {
        return ((AbstractSelectionTool) myActiveTool).getSelectedImage();
      }

      // Nothing available to copy if active tool isn't a selection tool
      return null;
    }

    @Override
    public void deleteSelection() {
      // Nothing to do if active tool isn't a selection tool
      if (activeTool instanceof AbstractSelectionTool) {
        ((AbstractSelectionTool) activeTool).deleteSelection();
      }
    }

    @Override
    public void pasteSelection(BufferedImage image) {
      // Make selection tool active...
      SelectionTool tool = (SelectionTool) PaintToolBuilder
        .create(SelectionTool)
        .makeActiveOnCanvas(myCanvas)
        .build();

      // ... then create a new selection from the pasted image
      tool.createSelection(image, new Point(0, 0));
    }
})

```

## Frequently asked questions

#### I don't get it. Doesn't Java's `Graphics` already let me draw stuff?

If you're not building an app that lets users paint lines and shapes with the mouse, this probably isn't for you.

Java's `Graphics` context does indeed provide routines for stroking and filling primitive shapes, but there's a quite a bit of work involved to map mouse and keyboard events into these calls the way a "paint" app expects. Getting selections, scale, grids, and transforms to work correctly is a bit more complex than merely delegating to `AffineTransform`, too.

#### How do I save my artwork?

Get the image from the canvas via the `public BufferedImage getCanvasImage()` method. Or, grab just the active selection from a selection tool using `selectionTool.getSelectedImage()`.

Then, use Java's `ImageIO` class to save as `gif`, `png` or `jpg`. For more advanced file type support (like `wbmp`, `bmp`, `pcx`, `pnm`, `raw` or `tiff`), consider the [Java Advanced ImageIO](http://docs.oracle.com/javase/6/docs/technotes/guides/imageio/index.html) library.

#### How do I import images from files or other apps?

You'll need your image in the form of a Java `BufferedImage` object. Use Java's ImageIO or Advanced ImageIO to [read/deserialize existing files or data](https://docs.oracle.com/javase/tutorial/2d/images/loadimage.html).

Then, you have three options:

1. Create a new canvas from an existing `BufferedImage` object like: `new JMonetCanvas(myImage)`. Best option when opening a file for editing.
2. Use the selection tool to create a new selection containing your image: `selectionTool.createSelection(myImage, new Point(0,0))`. Best option when pasting an image into to an existing canvas (and you'd like to allow the user to move it into place).
3. Commit the image to an existing canvas directly, like: `myCanvas.commit(new ImageLayerSet(myImage))`. Best option when you want to programmatically overlay an image onto an existing canvas.

Note that in cases 1 and 3, if the imported image does not match the dimensions of the canvas, it will be drawn in the upper-left corner. Translate (and/or resize) the image first if you'd like it to appear elsewhere.

#### If I create a selection using `LassoTool`, can I modify it with a transform tool (like `ProjectionTool`) or do I have to create a new selection from scratch with the transform tool?

You can transfer a selection from one selection tool to another (including transform tools) using the `morphSelection` method:

```
LassoTool currentTool;
ProjectionTool newTool = PaintToolBuilder.create(...);

currentTool.morphSelection(newTool);  // newTool now has currentTool's selection  
currentTool.deactivate();             // ... but currentTool is still active.
```

When morphing a Lasso selection to a transform tool, the selection bounds will become rectangular, but only the pixels originally encircled by the Lasso will be affected by the transform.

#### Does this library support image filters like blur, emboss, or edge tracing?

Support? Absolutely! Provide? No.

Any selection on the canvas, or the entire canvas itself, can be filtered by invoking the `.operate()` method (to apply a `BufferedImageOp` on the canvas object or selection tool). The folks at [JH Labs](http://www.jhlabs.com) produce an outstanding library of image filters that integrate easily with JMonet if you'd rather not author your own. As an example, here's how to use their Solarize filter:

Include the JH Labs filter library (from Maven Central) as a dependency in your project:

```
dependencies {
  compile group: 'com.jhlabs', name: 'filters', version: '2.0.235'
}
```

Then, apply [one of their filters](http://www.jhlabs.com/ip/filters/index.html) to your canvas or selection (in this example, their `SolarizeFilter` class):

```
  myCanvas.operate(new SolarizeFilter());
```

#### Can I create my own tools?

Of course! Tools are typically subclassed from one of the abstract tool classes in the `paint.tools.base` package. These abstract classes handle UI events for the most common tool behaviors:

Tool Base                | Description
-------------------------|------------
`PaintTool`              | Base class from which all paint tools are derived. Holds references to attribute providers and implements empty mouse and keyboard event handlers (_template pattern_; allows tools to override only those methods they care about).
`AbstractBoundsTool`     | Click-and-drag to define a rectangular boundary. Examples: Rectangle, Oval, Round Rectangle, Shape tools.
`AbstractLineTool`       | Click-and-drag to define a line between two points. Example: Line tool.
`AbstractPathTool`       | Click-and-drag to define a free-form path on the canvas. Examples: Paintbrush, pencil, eraser tools.
`AbstractPolylineTool`   | Click, click, click, double-click to define segments in a polygon or curve. Examples: Curve, Polygon tools.
`AbstractSelectionTool`  | Most complex of the tool bases; click-and-drag to define a shape to be drawn with marching ants allowing the user to move or modify the underlying graphic. Examples: Selection, Lasso, Rotate tools.
`AbstractTransformTool`  | Click-and-drag to select a rectangular boundary drawn with drag handles at each corner which can moved by the user. Example: Slant, projection, perspective and rubber sheet tools.

#### My canvas isn't getting garbage collected. This library has a memory leak.

The `JMonetCanvas` registers itself as a listener to global event dispatchers so that you don't have to. Unfortunately, that means it'll never get cleaned up.

When you're done with a canvas, call `.dispose()` on the canvas object to allow the JVM to garbage collect it.

#### What about vector graphic tools (i.e., "draw" apps)?

Sorry, that's not the intent of this library. That said, many pieces of this library could be leveraged for such a tool...

#### I created a custom stroke using a line shape but it doesn't work. What gives?

The area defined by the stroke's shape is where the "pen" will produce ink. A line (or a curve) has no area and therefore produces no paint on the canvas. Use a thin `Rectangle2D` instead.
