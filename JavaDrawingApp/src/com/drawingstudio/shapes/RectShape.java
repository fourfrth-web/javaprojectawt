package com.drawingstudio.shapes;

import java.awt.*;

/**
 * Rectangle shape implementation
 * Demonstrates inheritance and method overriding
 */
public class RectShape extends ShapeBase {
    
    public RectShape(Point start, Point end, Color color, int strokeWidth) {
        super("RECTANGLE", start, end, color, strokeWidth);
    }
    
    @Override
    public void draw(Graphics2D g2d) {
        setupGraphics(g2d);
        int x = Math.min(startPoint.x, endPoint.x);
        int y = Math.min(startPoint.y, endPoint.y);
        int width = Math.abs(endPoint.x - startPoint.x);
        int height = Math.abs(endPoint.y - startPoint.y);
        g2d.drawRect(x, y, width, height);
    }
}
