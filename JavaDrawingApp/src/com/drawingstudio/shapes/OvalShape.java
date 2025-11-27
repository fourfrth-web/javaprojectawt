package com.drawingstudio.shapes;

import java.awt.*;

/**
 * Oval shape implementation
 * Demonstrates inheritance from ShapeBase
 */
public class OvalShape extends ShapeBase {
    
    public OvalShape(Point start, Point end, Color color, int strokeWidth) {
        super("OVAL", start, end, color, strokeWidth);
    }
    
    @Override
    public void draw(Graphics2D g2d) {
        setupGraphics(g2d);
        int x = Math.min(startPoint.x, endPoint.x);
        int y = Math.min(startPoint.y, endPoint.y);
        int width = Math.abs(endPoint.x - startPoint.x);
        int height = Math.abs(endPoint.y - startPoint.y);
        g2d.drawOval(x, y, width, height);
    }
}
