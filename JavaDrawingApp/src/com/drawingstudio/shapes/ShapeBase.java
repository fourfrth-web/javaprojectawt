package com.drawingstudio.shapes;

import java.awt.*;

/**
 * Abstract base class for all drawable shapes
 * Demonstrates OOP concepts: Abstraction, Encapsulation
 */
public abstract class ShapeBase {
    protected String type;
    protected Point startPoint;
    protected Point endPoint;
    protected Color color;
    protected int strokeWidth;
    
    public ShapeBase(String type, Point start, Point end, Color color, int strokeWidth) {
        this.type = type;
        this.startPoint = new Point(start);
        this.endPoint = new Point(end);
        this.color = color;
        this.strokeWidth = strokeWidth;
    }
    
    /**
     * Abstract method to draw the shape - demonstrates polymorphism
     * Each subclass provides its own implementation
     */
    public abstract void draw(Graphics2D g2d);
    
    /**
     * Check if a point is within this shape's bounds (for eraser/selection)
     */
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
    
    // Getters - demonstrates encapsulation with controlled access
    public String getType() { return type; }
    public Point getStartPoint() { return new Point(startPoint); }
    public Point getEndPoint() { return new Point(endPoint); }
    public Color getColor() { return color; }
    public int getStrokeWidth() { return strokeWidth; }
    
    /**
     * Helper method to setup graphics context with shape properties
     */
    protected void setupGraphics(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    }
}
