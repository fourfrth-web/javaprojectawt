package com.drawingstudio.brush;

import java.awt.*;

/**
 * Interface for different brush types
 * Demonstrates interface usage for polymorphism
 */
public interface BrushType {
    /**
     * Get the color for this brush type
     */
    Color getBrushColor(Color selectedColor);
    
    /**
     * Get the stroke for this brush type
     */
    BasicStroke getBrushStroke(int size);
    
    /**
     * Get the name of this brush type
     */
    String getBrushName();
}
