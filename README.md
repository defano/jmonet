# JMonet

[Getting Started](#getting-started) | [Tools](#paint-tools) | [Transforms](#image-transforms) | [Brushes](#creating-complex-brush-shapes) | [Cut, Copy and Paste](#cut-copy-and-paste) | [Observables](#observable-attributes-with-rxjava) | [FAQs](#frequently-asked-questions)

An easy-to-use toolkit for incorporating paint tools like those found in [MacPaint](https://en.wikipedia.org/wiki/MacPaint) or [Microsoft Paint](https://en.wikipedia.org/wiki/Microsoft_Paint) into a Java Swing or JavaFX application. Sorry, JMonet is not compatible with Android.

This project provides the paint capabilities found in [WyldCard](https://github.com/defano/wyldcard) (an open-sourced clone of Apple's HyperCard).

[![Build Status](https://travis-ci.org/defano/jmonet.svg?branch=master)](https://travis-ci.org/defano/jmonet)
[![Sonar Status](https://sonarcloud.io/api/project_badges/measure?project=com.defano.jmonet%3Ajmonet&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.defano.jmonet%3Ajmonet)

## Features

* Familiar suite of paint tools with powerful, RxJava-observable attributes for brush strokes, colors, patterns, line sizes, etc.
* Built-in image transform tools like scale, rotate, flip, shear, perspective and projection. Easy to incorporate third-party image filters (like emboss or trace edges) implemented as a `Kernel` or `BufferedImageOp`.
* Paint canvas can be scaled and displayed within a scrollable pane; tools can be snapped to a grid.
* Multi-operation undo and redo, with easy-to-implement cut, copy and paste integration.
* Paint and edit 24-bit, true-color images with alpha transparency; images are backed by a standard Java `BufferedImage` object making it easy to import and export graphics.
* Lightweight toolkit published to Maven Central integrates easily into Swing and JavaFX applications.

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

JMonet is published to Maven Central. Simply include the library in your Maven project's POM, like:

```
<dependency>
    <groupId>com.defano.jmonet</groupId>
    <artifactId>jmonet</artifactId>
    <version>0.4.0</version>
</dependency>
```

... or your Gradle build script:

```
repositories {
    mavenCentral()
}

dependencies {
    compile 'com.defano.jmonet:jmonet:0.4.0'
}
```

#### 2. Create a canvas

JMonet integrates easily into Java Swing and JavaFX applications. Create a canvas node or panel and add it to a window in your application:

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

Each of these transforms is implemented as a standalone class in the `com.defano.jmonet.transform` package hierarchy, making it easy to apply these programmatically (offline) to a `BufferedImage` object.

## Creating complex brush shapes

A `Stroke` represents the size and shape of the "pen" used to draw the outline of a shape or path.

JMonet's `StrokeBuilder` class can generate complex brush strokes of any arbitrary shape (even the shape of text). For example, to produce a stroke in the shape of a vertical line, 20 pixels tall:

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
    .rotated(-45)         // ... rotate it -45 degrees
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

JMonet makes it easy to integrate cut, copy and paste functions into an application that utilizes the operating system's clipboard. With this, you can copy and paste graphics from within your own application or between it and other applications.

A bit of integration is required to connect these functions to the UI elements in your app (like menu items or toolbar buttons) that a user will interact with.

#### 1. Create an `ActionListener` to route cut-copy-paste actions to the canvas

Consistent with Swing's `ActionListener` pattern, the JMonet canvas will not receive cut, copy or paste actions until we register a listener class that routes clipboard actions to it. The `CanvasClipboardActionListener` class will attempt to route actions to whichever canvas currently in focus. If you dislike this behavior you may pass an implementation of `CanvasFocusDelegate` into its constructor, or simply write your own `ActionListener` instead.

Add your `CanvasClipboardActionListener` to whichever user interface elements will generate cut, copy and paste events. Most commonly, this would include menu items in the menu bar, but could also be used with buttons on a toolbar. For example:

```
JMenuItem myCopyMenuItem = new JMenuItem(new DefaultEditorKit.CopyAction());
copyMenuItem.setName("Copy");
copyMenuItem.setActionCommand((String) TransferHandler.getCopyAction().getValue(Action.NAME))
copyMenuItem.addActionListener(new CanvasClipboardActionListener());

myEditMenu.add(myCopyMenuItem);
```

#### 2. Add the transfer handler

Next, your application needs to tell JMonet what it should do when it receives a cut, copy or paste action. This is accomplished by setting a `TransferHandler` on the `JMonetCanvas`. The JMonet library comes with a utility class, `CanvasTransferHandler`, that eliminates much of the boilerplate typically associated with integrating cut, copy and paste.

The code below provides an implementation that cuts, copies and pastes the active selection.

```
  JMonetCanvas myCanvas = new JMonetCanvas();

  myCanvas.setTransferHandler(new CanvasTransferHandler(myCanvas, new CanvasTransferDelegate() {
    @Override
    public BufferedImage copySelection() {
      if (myActiveTool instanceof SelectionTool) {
        return ((SelectionTool) myActiveTool).getSelectedImage();
      }

      // Nothing available to copy if active tool isn't a selection tool
      return null;
    }

    @Override
    public void deleteSelection() {
      // Nothing to do if active tool isn't a selection tool
      if (activeTool instanceof SelectionTool) {
        ((SelectionTool) activeTool).deleteSelection();
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

## Observable attributes with RxJava

JMonet uses RxJava to provide observable attributes. This makes it easy to keep application menus, tool bars and palettes in sync with the state of your paint tools. As one control modifies an attribute, other controls (and the tool itself) will be notified of the change.

Lets imagine we have a `JCheckBoxMenuItem` in our menu bar and a `JCheckBox` button on a tool palette, both of which can be used to enable or disable the draw centered paint tool attribute. Here's how to use RxJava to achieve that:

#### 1. Create an observable object

Since a single menu item often controls an attribute for all tools, you'll probably want to model this as a Singleton that's easily accessible from different areas of your program.

```
// BehaviorSubject is a simple kind of Observable, see JavaRx documentation for details
BehaviorSubject<Boolean> drawCenteredObservable = BehaviorSubject.createDefault(true);
```

#### 2. Wire the `Observable` to the menu item and checkbox button

```
JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem();
JCheckBox checkbox = new JCheckBox();

// onNext() sets the value seen by other observers
menuItem.addActionListener(e -> drawCenteredObservable.onNext(menuItem.isSelected()));
checkbox.addActionListener(a -> drawCenteredObservable.onNext(checkbox.isSelected());

// The subscribe() lambda fires each time the observed value changes
drawCenteredObservable.subscribe(drawCentered -> menuItem.setSelected(drawCentered));
drawCenteredObservable.subscribe(drawCentered -> checkbox.setSelected(drawCentered));
```

Note that the `.subscribe()` method returns a `Disposable` object. You should maintain a reference to this object and invoke `.dispose()` on it when you no longer wish to observe this attribute.

#### 3. Inject the `Observable` into the tool

The paint tool will react to changing values provided by the `Observable`. Clicking the checkbox or toggling the menu item will affect the behavior of this tool in realtime.

```
PaintToolBuilder.create(PaintToolType.RECTANGLE)
    .withDrawCenteredObservable(drawCenteredObservable)
    .makeActiveOnCanvas(myCanvas)
    .build();

```

## Frequently asked questions

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
currentTool.deactivate();             // currentTool is still active, fix that
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

#### I have multiple canvases open at the same time. How can I make a tool active on different canvases simultaneously.

Strictly speaking, a JMonet paint tool can only be active on one canvas at a time. But that needn't stand in your way. As focus changes from one canvas to the next, simply re-activate the tool on the newly focused canvas to achieve your desired behavior.

On each canvas that you want to share a tool, you might do something like:

```
thisCanvas.addFocusListener(new FocusListener() {
  @Override
  public void focusGained(FocusEvent e) {
    theActiveTool.activate(thisCanvas);  // implicitly deactivates from last canvas
  }

  @Override
  public void focusLost(FocusEvent e) {
    // Nothing to do
  }
});
```

#### I created a custom stroke using a line shape but it doesn't work. What gives?

The area defined by the stroke's shape is where the "pen" will produce ink. A line (or a curve) has no area and therefore produces no paint on the canvas. Use a thin `Rectangle2D` as your stroke shape instead.

#### Can I create my own tools?

Of course! Tools are typically subclassed from one of the base tool classes in the `com.defano.jmonet.tools.base` package. These classes handle UI events for the most common tool behaviors, delegating to tool-specific subclasses for rendering:

Tool Base         | Delegate Class          | Description
------------------|-------------------------|---------------------
`BasicTool`       |                         | Base class from which all paint tools are derived. Holds references to attribute providers and implements empty mouse and keyboard event handlers (_template pattern_; allows tools to override only those methods they care about).
`BoundsTool`      | `BoundsToolDelegate`    | Click-and-drag to define a rectangular boundary. Examples: Rectangle, Oval, Round Rectangle, Shape tools.
`LinearTool`      | `LinearToolDelegate`    | Click-and-drag to define a line between two points. Example: Line tool.
`PathTool`        | `PathToolDelegate`      | Click-and-drag to define a free-form path on the canvas. Examples: Paintbrush, pencil, eraser tools.
`PolylineTool`    | `PolylineToolDelegate`  | Click, click, click, double-click to define segments in a polygon or curve. Examples: Curve, Polygon tools.
`SelectionTool`   | `SelectionToolDelegate` | Most complex of the tool bases; click-and-drag to define a shape to be drawn with marching ants allowing the user to move or modify the underlying graphic. Examples: Selection, Lasso, Rotate tools.
`TransformTool`   | `TransformToolDelegate` | Click-and-drag to select a rectangular boundary drawn with drag handles at each corner which can moved by the user. Example: Slant, projection, perspective and rubber sheet tools.

#### My canvas isn't getting garbage collected. This library has a memory leak.

The `JMonetCanvas` registers itself as a listener to global event dispatchers so that you don't have to. Unfortunately, that means it'll never get cleaned up.

When you're done with a canvas, call `.dispose()` on the canvas object to allow the JVM to garbage collect it.

#### What about vector graphic tools (i.e., "draw" apps)?

Sorry, that's not the intent of this library. That said, many pieces of this library could be leveraged for such a tool...
