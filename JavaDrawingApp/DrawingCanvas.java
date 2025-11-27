import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * AWT-based drawing canvas
 * Supports freehand drawing, shapes, and various drawing tools
 */
class DrawingCanvas extends Canvas {
    private BufferedImage drawingImage;
    private BufferedImage offscreenBuffer; // For double buffering
    private Graphics2D g2d;
    private Color currentColor = Color.BLACK;
    private int brushSize = 3;
    private String currentTool = "BRUSH";
    private SimpleDrawingApp parentApp;
    
    // Drawing state
    private boolean isDrawing = false;
    private Point startPoint, endPoint;
    private Point lastPoint;
    
    // Undo/Redo functionality
    private static class CanvasState {
        BufferedImage image;
        List<DrawableShape> shapes;
        
        CanvasState(BufferedImage img, List<DrawableShape> shapeList) {
            // Deep copy the image
            this.image = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
            Graphics2D g = this.image.createGraphics();
            g.drawImage(img, 0, 0, null);
            g.dispose();
            
            // Deep copy the shapes list
            this.shapes = new ArrayList<>();
            for (DrawableShape shape : shapeList) {
                this.shapes.add(new DrawableShape(shape.getType(), shape.getStartPoint(), 
                    shape.getEndPoint(), shape.getColor(), shape.getStrokeWidth()));
            }
        }
    }
    
    private List<CanvasState> undoHistory;
    private List<CanvasState> redoHistory;
    private static final int MAX_UNDO_STEPS = 10;
    
    // Shape preview
    private boolean showPreview = false;
    
    // Shape storage
    private List<DrawableShape> shapes;
    
    public DrawingCanvas(SimpleDrawingApp parent) {
        this.parentApp = parent;
        setBackground(Color.WHITE);
        
        undoHistory = new ArrayList<>();
        redoHistory = new ArrayList<>();
        shapes = new ArrayList<>();
        
        // Initialize drawing surface
        initializeDrawingSurface();
    }
    
    private void initializeDrawingSurface() {
        int width = 800;
        int height = 600;
        
        drawingImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        offscreenBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        g2d = drawingImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(currentColor);
        g2d.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        setSize(width, height);
    }
    
    @Override
    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public void paint(Graphics g) {
        // Use double buffering to eliminate flicker
        Graphics2D bufferG2d = offscreenBuffer.createGraphics();
        bufferG2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw the buffered image to offscreen buffer
        bufferG2d.drawImage(drawingImage, 0, 0, null);
        
        // Draw all shapes on top
        for (DrawableShape shape : shapes) {
            shape.draw(bufferG2d);
        }
        
        // Draw shape preview
        if (showPreview && isDrawing && startPoint != null && endPoint != null) {
            bufferG2d.setColor(currentColor);
            bufferG2d.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            
            switch (currentTool) {
                case "LINE":
                    bufferG2d.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
                    break;
                case "RECTANGLE":
                    int x = Math.min(startPoint.x, endPoint.x);
                    int y = Math.min(startPoint.y, endPoint.y);
                    int width = Math.abs(endPoint.x - startPoint.x);
                    int height = Math.abs(endPoint.y - startPoint.y);
                    bufferG2d.drawRect(x, y, width, height);
                    break;
                case "OVAL":
                    x = Math.min(startPoint.x, endPoint.x);
                    y = Math.min(startPoint.y, endPoint.y);
                    width = Math.abs(endPoint.x - startPoint.x);
                    height = Math.abs(endPoint.y - startPoint.y);
                    bufferG2d.drawOval(x, y, width, height);
                    break;
                case "TRIANGLE":
                    // Draw triangle with three points
                    int[] xPoints = {startPoint.x, endPoint.x, startPoint.x + (startPoint.x - endPoint.x)};
                    int[] yPoints = {startPoint.y, endPoint.y, endPoint.y};
                    bufferG2d.drawPolygon(xPoints, yPoints, 3);
                    break;
                case "DIAMOND":
                    // Draw diamond (rhombus)
                    int centerX = (startPoint.x + endPoint.x) / 2;
                    int centerY = (startPoint.y + endPoint.y) / 2;
                    int halfWidth = Math.abs(endPoint.x - startPoint.x) / 2;
                    int halfHeight = Math.abs(endPoint.y - startPoint.y) / 2;
                    int[] diamondX = {centerX, centerX + halfWidth, centerX, centerX - halfWidth};
                    int[] diamondY = {centerY - halfHeight, centerY, centerY + halfHeight, centerY};
                    bufferG2d.drawPolygon(diamondX, diamondY, 4);
                    break;
            }
        }
        
        // Dispose buffer graphics and draw final result to screen
        bufferG2d.dispose();
        g.drawImage(offscreenBuffer, 0, 0, null);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }
    
    // Mouse event handlers
    public void mousePressed(MouseEvent e) {
        isDrawing = true;
        startPoint = e.getPoint();
        lastPoint = e.getPoint();
        
        // Handle eraser for shapes
        if (currentTool.equals("ERASER")) {
            // Check if clicking on a shape to delete it
            for (int i = shapes.size() - 1; i >= 0; i--) {
                if (shapes.get(i).contains(startPoint)) {
                    saveStateForUndo();
                    shapes.remove(i);
                    repaint();
                    return; // Only delete one shape at a time
                }
            }
            // If not clicking on shape, use brush eraser
            saveStateForUndo();
            setupBrushGraphics();
        } else if (currentTool.equals("BRUSH")) {
            saveStateForUndo();
            setupBrushGraphics();
        } else if (currentTool.equals("COLOR_PICKER")) {
            // Color picker will be handled in mouseClicked
        }
    }
    
    public void mouseDragged(MouseEvent e) {
        if (!isDrawing) return;
        
        Point currentPoint = e.getPoint();
        
        switch (currentTool) {
            case "BRUSH":
            case "ERASER":
                drawBrushStroke(lastPoint, currentPoint);
                lastPoint = currentPoint;
                repaint();
                break;
                
            case "LINE":
            case "RECTANGLE":
            case "OVAL":
            case "TRIANGLE":
            case "DIAMOND":
                endPoint = currentPoint;
                updateShapePreview();
                break;
        }
    }
    
    public void mouseReleased(MouseEvent e) {
        if (!isDrawing) return;
        
        isDrawing = false;
        endPoint = e.getPoint();
        
        switch (currentTool) {
            case "LINE":
                saveStateForUndo();
                DrawableShape lineShape = new DrawableShape("LINE", startPoint, endPoint, currentColor, brushSize);
                shapes.add(lineShape);
                break;
            case "RECTANGLE":
                saveStateForUndo();
                DrawableShape rectShape = new DrawableShape("RECTANGLE", startPoint, endPoint, currentColor, brushSize);
                shapes.add(rectShape);
                break;
            case "OVAL":
                saveStateForUndo();
                DrawableShape ovalShape = new DrawableShape("OVAL", startPoint, endPoint, currentColor, brushSize);
                shapes.add(ovalShape);
                break;
            case "TRIANGLE":
                saveStateForUndo();
                DrawableShape triangleShape = new DrawableShape("TRIANGLE", startPoint, endPoint, currentColor, brushSize);
                shapes.add(triangleShape);
                break;
            case "DIAMOND":
                saveStateForUndo();
                DrawableShape diamondShape = new DrawableShape("DIAMOND", startPoint, endPoint, currentColor, brushSize);
                shapes.add(diamondShape);
                break;
        }
        
        clearPreview();
        repaint();
    }
    
    public void mouseClicked(MouseEvent e) {
        if (currentTool.equals("COLOR_PICKER")) {
            Point p = e.getPoint();
            if (p.x >= 0 && p.x < drawingImage.getWidth() && p.y >= 0 && p.y < drawingImage.getHeight()) {
                // Get color from the composite view (image + shapes)
                BufferedImage composite = new BufferedImage(drawingImage.getWidth(), drawingImage.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D g = composite.createGraphics();
                g.drawImage(drawingImage, 0, 0, null);
                for (DrawableShape shape : shapes) {
                    shape.draw(g);
                }
                g.dispose();
                
                int rgb = composite.getRGB(p.x, p.y);
                Color pickedColor = new Color(rgb);
                currentColor = pickedColor;
                parentApp.setPickedColor(pickedColor);
                repaint();
            }
        }
    }
    
    public void mouseMoved(MouseEvent e) {
        if (currentTool.equals("COLOR_PICKER")) {
            Point p = e.getPoint();
            if (p.x >= 0 && p.x < drawingImage.getWidth() && p.y >= 0 && p.y < drawingImage.getHeight()) {
                int rgb = drawingImage.getRGB(p.x, p.y);
                Color hoverColor = new Color(rgb);
                // Show color info in status (parent app would need to handle this)
            }
        }
    }
    
    // Drawing methods
    private void setupBrushGraphics() {
        g2d.setColor(currentTool.equals("ERASER") ? Color.WHITE : currentColor);
        g2d.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    }
    
    private void drawBrushStroke(Point start, Point end) {
        g2d.drawLine(start.x, start.y, end.x, end.y);
    }
    
    // Preview methods for shapes
    private void updateShapePreview() {
        showPreview = true;
        repaint();
    }
    
    private void clearPreview() {
        showPreview = false;
    }
    
    // Undo functionality
    private void saveStateForUndo() {
        if (undoHistory.size() >= MAX_UNDO_STEPS) {
            undoHistory.remove(0);
        }
        
        // Save current state (both image and shapes)
        CanvasState currentState = new CanvasState(drawingImage, shapes);
        undoHistory.add(currentState);
        
        // Clear redo history when new action is performed
        redoHistory.clear();
    }
    
    public void undo() {
        if (!undoHistory.isEmpty()) {
            // Save current state to redo history
            CanvasState currentState = new CanvasState(drawingImage, shapes);
            redoHistory.add(currentState);
            
            // Restore previous state
            CanvasState lastState = undoHistory.remove(undoHistory.size() - 1);
            
            g2d.dispose();
            drawingImage = new BufferedImage(lastState.image.getWidth(), lastState.image.getHeight(), BufferedImage.TYPE_INT_RGB);
            g2d = drawingImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.drawImage(lastState.image, 0, 0, null);
            
            // Restore shapes
            shapes.clear();
            shapes.addAll(lastState.shapes);
            
            repaint();
        }
    }
    
    public void redo() {
        if (!redoHistory.isEmpty()) {
            // Save current state to undo history
            CanvasState currentState = new CanvasState(drawingImage, shapes);
            undoHistory.add(currentState);
            
            // Restore next state
            CanvasState nextState = redoHistory.remove(redoHistory.size() - 1);
            
            g2d.dispose();
            drawingImage = new BufferedImage(nextState.image.getWidth(), nextState.image.getHeight(), BufferedImage.TYPE_INT_RGB);
            g2d = drawingImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.drawImage(nextState.image, 0, 0, null);
            
            // Restore shapes
            shapes.clear();
            shapes.addAll(nextState.shapes);
            
            repaint();
        }
    }
    
    // Canvas management
    public void clearCanvas() {
        saveStateForUndo();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, drawingImage.getWidth(), drawingImage.getHeight());
        g2d.setColor(currentColor);
        shapes.clear();
        repaint();
    }
    
    public void saveToFile(File file) {
        try {
            // Create composite image with shapes
            BufferedImage composite = new BufferedImage(drawingImage.getWidth(), drawingImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D compG2d = composite.createGraphics();
            compG2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw background
            compG2d.drawImage(drawingImage, 0, 0, null);
            
            // Draw shapes on top
            for (DrawableShape shape : shapes) {
                shape.draw(compG2d);
            }
            
            compG2d.dispose();
            
            // Save as PNG
            String filename = file.getName().toLowerCase();
            if (!filename.endsWith(".png")) {
                file = new File(file.getParent(), file.getName() + ".png");
            }
            
            ImageIO.write(composite, "png", file);
            System.out.println("Drawing saved to: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }
    
    public void loadFromFile(File file) {
        try {
            BufferedImage loadedImage = ImageIO.read(file);
            if (loadedImage != null) {
                saveStateForUndo();
                
                // Clear current drawing
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, drawingImage.getWidth(), drawingImage.getHeight());
                shapes.clear();
                
                // Draw loaded image
                g2d.drawImage(loadedImage, 0, 0, null);
                g2d.setColor(currentColor);
                
                repaint();
                System.out.println("Image loaded from: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }
    
    // Setters for drawing properties
    public void setCurrentColor(Color color) {
        this.currentColor = color;
        if (g2d != null) {
            g2d.setColor(color);
        }
    }
    
    public void setBrushSize(int size) {
        this.brushSize = size;
        if (g2d != null) {
            g2d.setStroke(new BasicStroke(size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        }
    }
    
    public void setCurrentTool(String tool) {
        this.currentTool = tool;
        clearPreview();
        repaint();
    }
    
    // Inner class for drawable shapes (simplified version)
    private static class DrawableShape {
        private String type;
        private Point startPoint;
        private Point endPoint;
        private Color color;
        private int strokeWidth;
        
        public DrawableShape(String type, Point start, Point end, Color color, int strokeWidth) {
            this.type = type;
            this.startPoint = new Point(start);
            this.endPoint = new Point(end);
            this.color = color;
            this.strokeWidth = strokeWidth;
        }
        
        public void draw(Graphics2D g2d) {
            g2d.setColor(color);
            g2d.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            
            switch (type) {
                case "LINE":
                    g2d.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
                    break;
                case "RECTANGLE":
                    int x = Math.min(startPoint.x, endPoint.x);
                    int y = Math.min(startPoint.y, endPoint.y);
                    int width = Math.abs(endPoint.x - startPoint.x);
                    int height = Math.abs(endPoint.y - startPoint.y);
                    g2d.drawRect(x, y, width, height);
                    break;
                case "OVAL":
                    x = Math.min(startPoint.x, endPoint.x);
                    y = Math.min(startPoint.y, endPoint.y);
                    width = Math.abs(endPoint.x - startPoint.x);
                    height = Math.abs(endPoint.y - startPoint.y);
                    g2d.drawOval(x, y, width, height);
                    break;
                case "TRIANGLE":
                    // Draw triangle with three points
                    int[] xPoints = {startPoint.x, endPoint.x, startPoint.x + (startPoint.x - endPoint.x)};
                    int[] yPoints = {startPoint.y, endPoint.y, endPoint.y};
                    g2d.drawPolygon(xPoints, yPoints, 3);
                    break;
                case "DIAMOND":
                    // Draw diamond (rhombus)
                    int centerX = (startPoint.x + endPoint.x) / 2;
                    int centerY = (startPoint.y + endPoint.y) / 2;
                    int halfWidth = Math.abs(endPoint.x - startPoint.x) / 2;
                    int halfHeight = Math.abs(endPoint.y - startPoint.y) / 2;
                    int[] diamondX = {centerX, centerX + halfWidth, centerX, centerX - halfWidth};
                    int[] diamondY = {centerY - halfHeight, centerY, centerY + halfHeight, centerY};
                    g2d.drawPolygon(diamondX, diamondY, 4);
                    break;
            }
        }
        
        // Getters
        public String getType() { return type; }
        public Point getStartPoint() { return new Point(startPoint); }
        public Point getEndPoint() { return new Point(endPoint); }
        public Color getColor() { return color; }
        public int getStrokeWidth() { return strokeWidth; }
        
        // Check if a point is within this shape's bounds (for eraser)
        public boolean contains(Point p) {
            int x = Math.min(startPoint.x, endPoint.x);
            int y = Math.min(startPoint.y, endPoint.y);
            int width = Math.abs(endPoint.x - startPoint.x);
            int height = Math.abs(endPoint.y - startPoint.y);
            
            // Add some padding for easier selection
            int padding = strokeWidth + 5;
            return p.x >= x - padding && p.x <= x + width + padding &&
                   p.y >= y - padding && p.y <= y + height + padding;
        }
    }
}