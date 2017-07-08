# JMonet

A rudimentary toolkit for incorporating [MacPaint](https://en.wikipedia.org/wiki/MacPaint) / [Microsoft Paint](https://en.wikipedia.org/wiki/Microsoft_Paint)-like tools into a Java Swing or JavaFX application.

## Features

* Offers a standard suite of paint tools with common modifier-key constraints (e.g., hold shift to snap lines to nearest 15-degree angle).
* Painting canvas supports undo and redo operations on all paint tool changes, plus cut, copy and paste integration with the system clipboard.
* Includes affine and non-affine transform tools including flip, rotate, shear, perspective and projection.
* Painted images are scalable (displayed within a scrollable pane) and tools can be snapped to a grid.
* Lightweight toolkit integrates easily into Swing and JavaFX applications.
* All operations are backed by a standard Java `BufferedImage`; easy to import existing images and save changes.

## Paint Tools

JMonet provides the following standard paint tools.

Icon | Tool            | Description
-----|-----------------|--------------
![Arrow](icons/arrow.png) | Arrow | A no-op tool that does not modify the canvas in any way.
![Magnifier](icons/magnifier.png) | Magnifier | Zoom in (scale the canvas) at the location clicked; hold `shift` to zoom out or `ctrl` to restore normal zoom.
![Airbrush](icons/spraypaint.png) | Airbrush | Paints translucent color or texture onto the canvas.
![Curve](icons/curve.png) | Curve | Draws quadratic (Bezier) curves by clicking to specify points on the curve.
![Eraser](icons/eraser.png) | Eraser | Removes paint from the canvas by restoring affected pixels to their fully-transparent state.
![Fill](icons/fill.png) | Fill | Shades an enclosed area with paint using a [flood-fill](https://en.wikipedia.org/wiki/Flood_fill) algorithm.
![Lasso](icons/lasso.png) | Lasso | Define a free-form selection boundary ([marching ants](https://en.wikipedia.org/wiki/Marching_ants)) for clearing or moving paint.
![Line](icons/line.png) | Line | Draws straight lines; hold `shift` to restrict lines to 15-degree angles.
![Oval](icons/oval.png) | Oval | Draws filled or outlined oval shapes; hold `shift` to constrain boundary to circle.
![Paintbrush](icons/paintbrush.png) | Paintbrush | Draws paint on the canvas (using configurable stroke and color/texture).
![Pencil](icons/pencil.png) | Pencil | Draws a free-form, narrow black path on the canvas.
![Polygon](icons/polygon.png) | Polygon | Draws filled or outlined irregular polygons by clicking to specify points. Double-click to complete the polygon; press `esc` to keep only the lines visible; hold `shift` to restrict line angles to 15 degree multiples.
![Freeform](icons/freeform.png) | Freeform | Draws a closed, free-form shape on the canvas. Click and drag to draw a path; release the mouse to draw a straight line connecting the final point on the path to the initial point.
![Rectangle](icons/rectangle.png) | Rectangle | Draws filled or outlined rectangles on the canvas; hold `shift` to constrain boundary to a square.
![Round Rect](icons/roundrect.png) | Round Rect | Draws filled or outlined round-rectangles on the canvas.
![Selection](icons/selection.png) | Selection | Define a selection rectangle ([marching ants](https://en.wikipedia.org/wiki/Marching_ants)) whose underlying graphic can be moved or cleared (press `delete`)
![Shape](icons/shape.png) | Shape | Draws filled or outlined regular polygons (i.e., shapes--triangles, squares, polygons, hexagons, etc.)
![Text](icons/text.png) | Text | Draws rasterized text (of a configurable font, size and style) on the canvas. Text remains editable until user clicks away.

## Transform tools

Icon | Tool            | Description
-----|----------| -------------
![Rotate](icons/rotate.png) | Rotate | Define a selection, then use the drag handle to free-rotate the selected graphic around its center.
![Slant](icons/slant.png) | Slant | Define a selection, then use the drag handles to apply an affine shear transform to the selected graphic.
![Scale](icons/scale.png) | Scale | Define a selection, then expand or shrink the selected image by dragging a handle.
![Projection](icons/distort.png) | Projection | Define a selection, then use the drag handles to project the image onto the geometry of an arbitrary quadrilateral.
![perspective](icons/perspective.png) | Perspective | Define a selection, then use the drag handles to warp the image onto an isosceles trapezoid, providing the effect of the left or right side of the image appearing nearer or farther from the viewer.
![Rubber Sheet](icons/distort.png) | Rubber Sheet | Similar to the projection transform, but utilizes a "rubber sheet" algorithm that preserves relative position over linearity.

#### Static transforms

Selected images can be flipped horizontally, vertically or rotated 90 degrees clockwise or counterclockwise via the Selection or Lasso tools. Adjustments to brightness, transparency and color are also available.

Once a selection has been made, invoke one of the following methods on the `SelectionTool` object:

```
void rotateLeft();
void rotateRight();
void flipHorizontal();
void flipVertical();

void adjustBrightness(int delta);
void adjustTransparency(int delta);
void invert();
```

## Getting started

#### 1. Install the library

JMonet is published to Maven Central; include the library in your Maven project's POM, like:

```
<dependency>
    <groupId>com.defano.jmonet</groupId>
    <artifactId>jmonet</artifactId>
    <version>0.0.6</version>
</dependency>
```

... or your Gradle build script:

```
repositories {
	mavenCentral()
}

dependencies {
  compile 'com.defano.jmonet:jmonet:0.0.6'
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
        UndoablePaintCanvas myCanvas = new UndoablePaintCanvas();
        frame.getContentPane().add(myCanvas);
    });
}
```

In JavaFX applications:

```
@Override
public void start(Stage stage) {

    // Create a JFX node for our paint canvas
    JFXPaintCanvasNode myCanvas = new JFXPaintCanvasNode(new UndoablePaintCanvas());

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
PaintTool activeTool = PaintToolBuilder.create(PaintToolType.PAINTBRUSH)
        .withStroke(BasicBrush.ROUND_8X8.stroke)
        .withFillPaint(Color.RED)
        .makeActiveOnCanvas(myCanvas)
        .build();
```

Voila! You're ready to start painting a round, 8-pixel, bright red brushstroke onto your canvas. Don't forget to deactivate this tool when you're done with it or wish to start using another tool:

```
// When you're done painting or ready for a different tool...
activeTool.deactivate();
```

There's no technical limitation that prevents multiple tools from being active on the same canvas at the same time, but that's not usually desired behavior in a paint program.

## Implement cut, copy and paste

JMonet makes it easy to integrate cut, copy and paste functions into your app that utilize the operating system's clipboard so that you can copy and paste graphics from within your own application or between other applications on your system.

A bit of integration is required to connect these functions to the UI elements in your app (like menu items or toolbar buttons) that a user will interact with.

#### 1. Route actions to the canvas

The JMonet canvas will not receive cut, copy or paste actions until we register an `ActionListener` that routes those actions to it. Typically, only the user interface element that has focus receives such events, but because a canvas has no clear concept of focus, its up to you to decide when the canvas should respond to cut, copy and paste actions.

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
  UndoablePaintCanvas myCanvas = new UndoablePaintCanvas();

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

## Tool and canvas attribute providers

When building your paint application, you'll likely need to observe and/or make changes to various canvas and tool attributes from several locations in your codebase (i.e., change and observe the paint color from the menu, palette, picker dialogs, etc).

To simplify this, attributes are wrapped in a `Provider` class. A Provider is an extension to Java's `Observable` with a bit of syntactical sugar to support one-liner lambda expressions and to derive observable attributes from other attributes.

For example, create a provider of paint and inject it into the tool (you might keep this provider around globally to be shared across all tools whose paint you want to change):

```
Provider<Paint> paintProvider = new Provider(Color.BLACK);
activeTool.setFillPaintProvider(paintProvider);
```

Now, you can observe changes to the tool's paint:

```
strokePaintProvider.addObserver((o, newValue) -> System.out.println("Got new paint: " + newValue));
```

More importantly, you can derive new Providers that listen for changes to an existing Provider and transform them into a different type. This is useful, for example, for checkmarking menu items or highlighting palette tools based on a selection.

```
ImmutableProvider<Boolean> isBlackColorSelected = ImmutableProvider.derivedFrom(strokePaintProvider, p -> p == Color.BLACK);
```

An `ImmutableProvider` is one whose underlying value cannot be changed. In this example, it wouldn't make sense to mutate `isBlackSelected` directly; its value follows the color provided by `strokePaintProvider`.

## Frequently asked questions

#### Why do I need this? Doesn't Java's `Graphics2D` already let me draw stuff?

Maybe you don't need this. If you're not building an app that lets users paint lines and shapes with the mouse, this probably isn't for you.

Java's Graphics context does indeed provide routines for drawing shapes, but there's a bit of work involved to map mouse and keyboard events into these calls the way a "paint" app expects. Getting scale, grids, and transforms to work correctly are a bit more complex than merely delegating to `AffineTransform`, too.

#### How do I save my artwork?

Get the image from the canvas via the `public BufferedImage getCanvasImage()` method. Then, use Java's `ImageIO` class to save as `gif`, `png` or `jpg`. For more advanced file type support (such as `wbmp`, `bmp`, `pcx`, `pnm`, `raw` or `tiff`), consider the [Java Advanced ImageIO](http://docs.oracle.com/javase/6/docs/technotes/guides/imageio/index.html) library.

#### How do I import images from files or elsewhere?

You'll need your image in the form of a Java `BufferedImage` object. Use Java's ImageIO or Advanced ImageIO to [read/deserialize existing files or data](https://docs.oracle.com/javase/tutorial/2d/images/loadimage.html).

Then, you have three options:

1. Create a new canvas from an existing `BufferedImage` object like: `new UndoableCanvas(myImage)`. Best option when opening a file for editing.
2. Use the selection tool to create a new selection containing your image: `selectionTool.createSelection(myImage, new Point(0,0))`. Best option when pasting an image into to an existing canvas (and you'd like to allow the user to move it into place).
3. Commit the image to an existing canvas by drawing it onto the scratch buffer and committing the change, like: `myCanvas.setScratchImage(myImage); myCanvas.commit()`. Best option when you want to programmatically overlay an image onto an existing canvas.

Note that in cases 1 and 3, if the imported image does not match the dimensions of the canvas, it will be drawn in the upper-left corner. Resize and translate the image you wish to import first if you'd like it to appear elsewhere.

#### If I create a selection using `SelectionTool`, can I modify it with a transform tool (like `ProjectionTool`) or do I have to create a new selection from scratch with the transform tool?

You can transfer a selection from one selection tool to another using a bit of code like this:

```
public void transferSelection(AbstractSelectionTool from, AbstractSelectionTool to) {
    to.createSelection(from.getSelectionOutline().getBounds());
}
```

Note that you cannot programmatically create a selection from an abstract shape, thus, when transferring selection from a tool providing a non-rectangular selection boundary the new selection will be expanded to a rectangle bounding the original selection. 

#### Can I create my own tools?

Of course! Tools are typically subclassed from one of the abstract tool classes in the `paint.tools.base` package. These abstract classes handle UI events for the most common tool behaviors:

Tool Base                | Description
-------------------------|------------
`PaintTool`              | Base class from which all paint tools are derived. Holds references to attribute providers and implements empty mouse and keyboard event handlers (_template pattern_; allows tools to override only those methods they care about).
`AbstractBoundsTool`     | Click-and-drag to define a rectangular boundary. Examples: Rectangle, Oval, Round Rectangle, Shape tools.
`AbstractLineTool`       | Click-and-drag to define a line between two points. Example: Line tool.
`AbstractPathTool`       | Click-and-drag to define a free-form path on the canvas. Examples: Paintbrush, pencil, eraser tools.
`AbstractPolylineTool`   | Click-click-click-double-click to define segments in a polygon or curve. Examples: Curve, Polygon tools.
`AbstractSelectionTool`  | Most complex of the tool bases; click-and-drag to define a shape to be drawn with marching ants allowing the user to move or modify the underlying graphic. Examples: Selection, Lasso, Rotate tools.
`AbstractTransformTool`  | Click-and-drag to select a rectangular boundary drawn with drag handles at each corner which can moved by the user. Example: Slant, projection, perspective and rubber sheet tools.

#### How can I layer canvases atop one another, or place other UI elements (like buttons and fields) on top of the painted graphics?

Place the canvas(es) and/or other UI components in a `LayeredPane`. Use the `LayeredPane` to control z-order.

#### What about vector graphic tools (i.e., "draw" apps)?

Sorry, that's not the intent of this library. That said, many pieces of this library could be leveraged for such a tool...
