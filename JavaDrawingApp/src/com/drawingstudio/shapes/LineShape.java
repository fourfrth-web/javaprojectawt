package com.drawingstudio.shapes;

import java.awt.*;

/**
 * Line shape implementation
 * Demonstrates inheritance from ShapeBase
 */
public class LineShape extends ShapeBase {
    
    public LineShape(Point start, Point end, Color color, int strokeWidth) {
        super("LINE", start, end, color, strokeWidth);
    }
    
    @Override
    public void draw(Graphics2D g2d) {
        setupGraphics(g2d);
        g2d.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
    }
}
