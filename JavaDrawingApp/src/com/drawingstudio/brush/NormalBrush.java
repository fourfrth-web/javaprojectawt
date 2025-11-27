package com.drawingstudio.brush;

import java.awt.*;

/**
 * Normal brush implementation
 * Demonstrates interface implementation
 */
public class NormalBrush implements BrushType {
    
    @Override
    public Color getBrushColor(Color selectedColor) {
        return selectedColor;
    }
    
    @Override
    public BasicStroke getBrushStroke(int size) {
        return new BasicStroke(size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    }
    
    @Override
    public String getBrushName() {
        return "NORMAL";
    }
}
