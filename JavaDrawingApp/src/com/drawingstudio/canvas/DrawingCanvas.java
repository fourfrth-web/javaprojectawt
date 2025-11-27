package com.drawingstudio.canvas;

import com.drawingstudio.app.SimpleDrawingApp;
import com.drawingstudio.shapes.*;
import com.drawingstudio.manager.*;
import com.drawingstudio.utils.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * AWT-based drawing canvas
 * Supports freehand drawing, shapes, and various drawing tools
 * Demonstrates composition, encapsulation, and delegation patterns
 */
public class DrawingCanvas extends Canvas {
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
    
    // Managers - demonstrates composition
    private HistoryManager historyManager;
    
    // Shape preview
    private boolean showPreview = false;
    
    // Shape storage
    private List<ShapeBase> shapes;
    
    public DrawingCanvas(SimpleDrawingApp parent) {
        this.parentApp = parent;
        setBackground(Color.WHITE);
        
        historyManager = new HistoryManager();
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
        
        // Draw all shapes on top - demonstrates polymorphism
        for (ShapeBase shape : shapes) {
            shape.draw(bufferG2d);
        }
        
        // Draw shape preview
        if (showPreview && isDrawing && startPoint != null && endPoint != null) {
            drawShapePreview(bufferG2d);
        }
        
        // Dispose buffer graphics and draw final result to screen
        bufferG2d.dispose();
        g.drawImage(offscreenBuffer, 0, 0, null);
    }
    
    private void drawShapePreview(Graphics2D g2d) {
        g2d.setColor(currentColor);
        g2d.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        switch (currentTool) {
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
                int[] xPoints = {startPoint.x, endPoint.x, startPoint.x + (startPoint.x - endPoint.x)};
                int[] yPoints = {startPoint.y, endPoint.y, endPoint.y};
                g2d.drawPolygon(xPoints, yPoints, 3);
                break;
            case "DIAMOND":
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
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }
    
    // Mouse event handlers - public methods called by event handlers
    public void handleMousePressed(MouseEvent e) {
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
                    return;
                }
            }
            // If not clicking on shape, use brush eraser
            saveStateForUndo();
            setupBrushGraphics();
        } else if (currentTool.equals("BRUSH")) {
            saveStateForUndo();
            setupBrushGraphics();
        }
    }
    
    public void handleMouseDragged(MouseEvent e) {
        if (!isDrawing) return;
        
        Point currentPoint = e.getPoint();
        
        if (ShapeUtils.isBrushTool(currentTool)) {
            drawBrushStroke(lastPoint, currentPoint);
            lastPoint = currentPoint;
            repaint();
        } else if (ShapeUtils.isShapeTool(currentTool)) {
            endPoint = currentPoint;
            updateShapePreview();
        }
    }
    
    public void handleMouseReleased(MouseEvent e) {
        if (!isDrawing) return;
        
        isDrawing = false;
        endPoint = e.getPoint();
        
        // Create shape using factory method - demonstrates factory pattern
        if (ShapeUtils.isShapeTool(currentTool)) {
            saveStateForUndo();
            ShapeBase newShape = ShapeUtils.createShape(currentTool, startPoint, endPoint, currentColor, brushSize);
            if (newShape != null) {
                shapes.add(newShape);
            }
        }
        
        clearPreview();
        repaint();
    }
    
    public void handleMouseClicked(MouseEvent e) {
        if (currentTool.equals("COLOR_PICKER")) {
            Point p = e.getPoint();
            if (PointUtils.isWithinBounds(p, drawingImage.getWidth(), drawingImage.getHeight())) {
                // Get color from the composite view (image + shapes)
                BufferedImage composite = createCompositeImage();
                int rgb = composite.getRGB(p.x, p.y);
                Color pickedColor = new Color(rgb);
                currentColor = pickedColor;
                parentApp.setPickedColor(pickedColor);
                repaint();
            }
        }
    }
    
    public void handleMouseMoved(MouseEvent e) {
        // Reserved for future use (e.g., color preview)
    }
    
    // Drawing methods
    private void setupBrushGraphics() {
        g2d.setColor(currentTool.equals("ERASER") ? Color.WHITE : currentColor);
        g2d.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    }
    
    private void drawBrushStroke(Point start, Point end) {
        g2d.drawLine(start.x, start.y, end.x, end.y);
    }
    
    private BufferedImage createCompositeImage() {
        BufferedImage composite = new BufferedImage(
            drawingImage.getWidth(), 
            drawingImage.getHeight(), 
            BufferedImage.TYPE_INT_RGB
        );
        Graphics2D g = composite.createGraphics();
        g.drawImage(drawingImage, 0, 0, null);
        for (ShapeBase shape : shapes) {
            shape.draw(g);
        }
        g.dispose();
        return composite;
    }
    
    // Preview methods for shapes
    private void updateShapePreview() {
        showPreview = true;
        repaint();
    }
    
    private void clearPreview() {
        showPreview = false;
    }
    
    // Undo/Redo functionality - delegates to HistoryManager
    private void saveStateForUndo() {
        historyManager.saveState(drawingImage, shapes);
    }
    
    public void undo() {
        HistoryManager.CanvasState lastState = historyManager.undo(drawingImage, shapes);
        if (lastState != null) {
            restoreState(lastState);
        }
    }
    
    public void redo() {
        HistoryManager.CanvasState nextState = historyManager.redo(drawingImage, shapes);
        if (nextState != null) {
            restoreState(nextState);
        }
    }
    
    private void restoreState(HistoryManager.CanvasState state) {
        g2d.dispose();
        drawingImage = new BufferedImage(
            state.getImage().getWidth(), 
            state.getImage().getHeight(), 
            BufferedImage.TYPE_INT_RGB
        );
        g2d = drawingImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(state.getImage(), 0, 0, null);
        
        shapes.clear();
        shapes.addAll(state.getShapes());
        
        repaint();
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
        FileManager.saveDrawing(file, drawingImage, shapes);
    }
    
    public void loadFromFile(File file) {
        BufferedImage loadedImage = FileManager.loadDrawing(file);
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
}
