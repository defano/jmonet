package com.defano.jmonet.tools;

//public class PencilToolTest extends ToolTest<PencilTool> {
//
//    @Mock Stroke stroke;
//    @Mock Paint fillPaint;
//
//    @BeforeEach
//    public void setUp() {
//        initialize(Mockito.spy(new PencilTool()));
//    }
//
//    @Test
//    public void testBeginPathDrawing() {
//        // Setup
//        final Point initialPoint = new Point(1, 2);
//        Mockito.when(mockCanvasImage.getRGB(1, 2)).thenReturn(new Color(0, 0, 0, 0).getRGB());
//
//        // Run the test
//        uut.startPath(mockScratch, stroke, fillPaint, initialPoint);
//
//        // Verify the results
//        Mockito.verify(mockAddScratchGraphics).draw(argThat(new LineMatcher(new Line2D.Float(initialPoint, initialPoint))));
//        Mockito.verify(mockAddScratchGraphics).setPaint(fillPaint);
//        Mockito.verify(mockAddScratchGraphics).setStroke(new BasicStroke(1));
//    }
//
//    @Test
//    public void testBeginPathErasing() {
//        // Setup
//        final Point initialPoint = new Point(1, 2);
//        Mockito.when(mockCanvasImage.getRGB(1, 2)).thenReturn(new Color(0, 0, 0, 255).getRGB());
//
//        // Run the test
//        uut.startPath(mockScratch, stroke, fillPaint, initialPoint);
//
//        // Verify the results
//        Mockito.verify(mockAddScratchGraphics).draw(argThat(new LineMatcher(new Line2D.Float(initialPoint, initialPoint))));
//        Mockito.verify(mockAddScratchGraphics).setPaint(fillPaint);
//        Mockito.verify(mockAddScratchGraphics).setStroke(new BasicStroke(1));
//    }
//
//
//    @Test
//    public void testAddPoint() {
//        // Setup
//        final Scratch scratch = null;
//        final Stroke stroke = null;
//        final Paint fillPaint = null;
//        final Point lastPoint = new Point(1, 2);
//        final Point thisPoint = new Point(3, 4);
//
//        // Run the test
//        uut.addPoint(mockScratch, stroke, fillPaint, lastPoint, thisPoint);
//
//        // Verify the results
//    }
//
//    public static class LineMatcher extends ArgumentMatcher<Line2D> {
//
//        private final Line2D lhs;
//
//        public LineMatcher(Line2D lhs) {
//            this.lhs = lhs;
//        }
//
//        @Override
//        public boolean matches(Object rhs) {
//            if (rhs instanceof Line2D) {
//                return lhs.getP1().equals(((Line2D) rhs).getP1()) && lhs.getP2().equals(((Line2D) rhs).getP2());
//            } else {
//                return false;
//            }
//        }
//    }
//}
