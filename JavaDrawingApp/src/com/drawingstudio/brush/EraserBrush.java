package com.drawingstudio.brush;

import java.awt.*;

/**
 * Eraser brush implementation
 * Demonstrates polymorphism - eraser is just a brush with white color
 */
public class EraserBrush implements BrushType {
    
    @Override
    public Color getBrushColor(Color selectedColor) {
        return Color.WHITE;  // Eraser always uses white
    }
    
    @Override
    public BasicStroke getBrushStroke(int size) {
        return new BasicStroke(size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    }
    
    @Override
    public String getBrushName() {
        return "ERASER";
    }
}
