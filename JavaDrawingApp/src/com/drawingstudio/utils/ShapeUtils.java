package com.drawingstudio.utils;

import com.drawingstudio.shapes.*;
import java.awt.*;

/**
 * Utility class for shape-related operations
 * Demonstrates factory pattern for creating shapes
 */
public class ShapeUtils {
    
    /**
     * Create a shape based on tool type
     * Demonstrates factory method pattern
     */
    public static ShapeBase createShape(String toolType, Point start, Point end, Color color, int strokeWidth) {
        switch (toolType) {
            case "LINE":
                return new LineShape(start, end, color, strokeWidth);
            case "RECTANGLE":
                return new RectShape(start, end, color, strokeWidth);
            case "OVAL":
                return new OvalShape(start, end, color, strokeWidth);
            case "TRIANGLE":
                return new TriangleShape(start, end, color, strokeWidth);
            case "DIAMOND":
                return new DiamondShape(start, end, color, strokeWidth);
            default:
                return null;
        }
    }
    
    /**
     * Check if tool type is a shape tool
     */
    public static boolean isShapeTool(String toolType) {
        return toolType.equals("LINE") || toolType.equals("RECTANGLE") || 
               toolType.equals("OVAL") || toolType.equals("TRIANGLE") || 
               toolType.equals("DIAMOND");
    }
    
    /**
     * Check if tool type is a brush tool
     */
    public static boolean isBrushTool(String toolType) {
        return toolType.equals("BRUSH") || toolType.equals("ERASER");
    }
}
