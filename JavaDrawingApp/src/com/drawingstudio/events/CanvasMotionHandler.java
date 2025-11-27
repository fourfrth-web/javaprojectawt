package com.drawingstudio.events;

import com.drawingstudio.canvas.DrawingCanvas;
import java.awt.event.*;

/**
 * Mouse motion event handler for the canvas
 * Demonstrates event handling separation
 */
public class CanvasMotionHandler implements MouseMotionListener {
    private DrawingCanvas canvas;
    
    public CanvasMotionHandler(DrawingCanvas canvas) {
        this.canvas = canvas;
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        canvas.handleMouseDragged(e);
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
        canvas.handleMouseMoved(e);
    }
}
