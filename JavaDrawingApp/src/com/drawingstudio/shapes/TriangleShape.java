package com.drawingstudio.shapes;

import java.awt.*;

/**
 * Triangle shape implementation
 * Demonstrates inheritance and custom drawing logic
 */
public class TriangleShape extends ShapeBase {
    
    public TriangleShape(Point start, Point end, Color color, int strokeWidth) {
        super("TRIANGLE", start, end, color, strokeWidth);
    }
    
    @Override
    public void draw(Graphics2D g2d) {
        setupGraphics(g2d);
        // Draw triangle with three points
        int[] xPoints = {startPoint.x, endPoint.x, startPoint.x + (startPoint.x - endPoint.x)};
        int[] yPoints = {startPoint.y, endPoint.y, endPoint.y};
        g2d.drawPolygon(xPoints, yPoints, 3);
    }
}
