package com.drawingstudio.utils;

import java.awt.*;

/**
 * Utility class for color-related operations
 * Demonstrates utility class pattern and static methods
 */
public class ColorUtils {
    
    /**
     * Get Color object from color name string
     */
    public static Color getColorFromName(String name) {
        switch (name.toLowerCase()) {
            case "black": return Color.BLACK;
            case "red": return Color.RED;
            case "green": return Color.GREEN;
            case "blue": return Color.BLUE;
            case "yellow": return Color.YELLOW;
            case "orange": return Color.ORANGE;
            case "pink": return Color.PINK;
            case "cyan": return Color.CYAN;
            case "magenta": return Color.MAGENTA;
            case "white": return Color.WHITE;
            default: return Color.BLACK;
        }
    }
    
    /**
     * Get color name from Color object
     */
    public static String getColorName(Color color) {
        if (color.equals(Color.BLACK)) return "Black";
        if (color.equals(Color.RED)) return "Red";
        if (color.equals(Color.GREEN)) return "Green";
        if (color.equals(Color.BLUE)) return "Blue";
        if (color.equals(Color.YELLOW)) return "Yellow";
        if (color.equals(Color.ORANGE)) return "Orange";
        if (color.equals(Color.PINK)) return "Pink";
        if (color.equals(Color.CYAN)) return "Cyan";
        if (color.equals(Color.MAGENTA)) return "Magenta";
        if (color.equals(Color.WHITE)) return "White";
        return "Black";
    }
    
    /**
     * Format color as RGB string
     */
    public static String formatRGB(Color color) {
        return String.format("RGB(%d, %d, %d)", 
            color.getRed(), color.getGreen(), color.getBlue());
    }
}
