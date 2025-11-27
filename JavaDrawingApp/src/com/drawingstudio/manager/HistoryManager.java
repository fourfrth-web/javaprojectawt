package com.drawingstudio.manager;

import com.drawingstudio.shapes.ShapeBase;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Manages the history of canvas states for undo/redo functionality
 * Demonstrates encapsulation and state management
 */
public class HistoryManager {
    private List<CanvasState> undoHistory;
    private List<CanvasState> redoHistory;
    private static final int MAX_UNDO_STEPS = 10;
    
    public HistoryManager() {
        this.undoHistory = new ArrayList<>();
        this.redoHistory = new ArrayList<>();
    }
    
    /**
     * Save current state for undo functionality
     */
    public void saveState(BufferedImage image, List<ShapeBase> shapes) {
        if (undoHistory.size() >= MAX_UNDO_STEPS) {
            undoHistory.remove(0);
        }
        
        CanvasState currentState = new CanvasState(image, shapes);
        undoHistory.add(currentState);
        
        // Clear redo history when new action is performed
        redoHistory.clear();
    }
    
    /**
     * Undo the last action
     * @return The previous canvas state, or null if nothing to undo
     */
    public CanvasState undo(BufferedImage currentImage, List<ShapeBase> currentShapes) {
        if (undoHistory.isEmpty()) {
            return null;
        }
        
        // Save current state to redo history
        CanvasState currentState = new CanvasState(currentImage, currentShapes);
        redoHistory.add(currentState);
        
        // Return previous state
        return undoHistory.remove(undoHistory.size() - 1);
    }
    
    /**
     * Redo the last undone action
     * @return The next canvas state, or null if nothing to redo
     */
    public CanvasState redo(BufferedImage currentImage, List<ShapeBase> currentShapes) {
        if (redoHistory.isEmpty()) {
            return null;
        }
        
        // Save current state to undo history
        CanvasState currentState = new CanvasState(currentImage, currentShapes);
        undoHistory.add(currentState);
        
        // Return next state
        return redoHistory.remove(redoHistory.size() - 1);
    }
    
    public boolean canUndo() {
        return !undoHistory.isEmpty();
    }
    
    public boolean canRedo() {
        return !redoHistory.isEmpty();
    }
    
    /**
     * Inner class representing a canvas state snapshot
     * Demonstrates composition and encapsulation
     */
    public static class CanvasState {
        private BufferedImage image;
        private List<ShapeBase> shapes;
        
        public CanvasState(BufferedImage img, List<ShapeBase> shapeList) {
            // Deep copy the image
            this.image = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
            Graphics2D g = this.image.createGraphics();
            g.drawImage(img, 0, 0, null);
            g.dispose();
            
            // Deep copy the shapes list
            this.shapes = new ArrayList<>(shapeList);
        }
        
        public BufferedImage getImage() {
            return image;
        }
        
        public List<ShapeBase> getShapes() {
            return new ArrayList<>(shapes);
        }
    }
}
