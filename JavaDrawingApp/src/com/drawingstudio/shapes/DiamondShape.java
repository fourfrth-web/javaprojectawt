package com.drawingstudio.shapes;

import java.awt.*;

/**
 * Diamond (rhombus) shape implementation
 * Demonstrates inheritance and complex shape rendering
 */
public class DiamondShape extends ShapeBase {
    
    public DiamondShape(Point start, Point end, Color color, int strokeWidth) {
        super("DIAMOND", start, end, color, strokeWidth);
    }
    
    @Override
    public void draw(Graphics2D g2d) {
        setupGraphics(g2d);
        // Draw diamond (rhombus)
        int centerX = (startPoint.x + endPoint.x) / 2;
        int centerY = (startPoint.y + endPoint.y) / 2;
        int halfWidth = Math.abs(endPoint.x - startPoint.x) / 2;
        int halfHeight = Math.abs(endPoint.y - startPoint.y) / 2;
        int[] diamondX = {centerX, centerX + halfWidth, centerX, centerX - halfWidth};
        int[] diamondY = {centerY - halfHeight, centerY, centerY + halfHeight, centerY};
        g2d.drawPolygon(diamondX, diamondY, 4);
    }
}
