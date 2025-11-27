package com.drawingstudio.utils;

import java.awt.*;

/**
 * Utility class for Point-related operations
 */
public class PointUtils {
    
    /**
     * Calculate distance between two points
     */
    public static double distance(Point p1, Point p2) {
        int dx = p2.x - p1.x;
        int dy = p2.y - p1.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    /**
     * Check if a point is within bounds
     */
    public static boolean isWithinBounds(Point p, int width, int height) {
        return p.x >= 0 && p.x < width && p.y >= 0 && p.y < height;
    }
    
    /**
     * Create a deep copy of a point
     */
    public static Point copyPoint(Point p) {
        return new Point(p);
    }
}
