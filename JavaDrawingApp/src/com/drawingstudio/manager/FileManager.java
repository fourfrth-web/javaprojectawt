package com.drawingstudio.manager;

import com.drawingstudio.shapes.ShapeBase;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;

/**
 * Manages file I/O operations for saving and loading drawings
 * Demonstrates separation of concerns and file handling
 */
public class FileManager {
    
    /**
     * Save the drawing to a file
     * @param file The file to save to
     * @param image The drawing image
     * @param shapes List of shapes to render on top
     * @return true if successful, false otherwise
     */
    public static boolean saveDrawing(File file, BufferedImage image, java.util.List<ShapeBase> shapes) {
        try {
            // Create composite image with shapes
            BufferedImage composite = new BufferedImage(
                image.getWidth(), 
                image.getHeight(), 
                BufferedImage.TYPE_INT_RGB
            );
            Graphics2D g2d = composite.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw background
            g2d.drawImage(image, 0, 0, null);
            
            // Draw shapes on top
            for (ShapeBase shape : shapes) {
                shape.draw(g2d);
            }
            
            g2d.dispose();
            
            // Ensure .png extension
            String filename = file.getName().toLowerCase();
            if (!filename.endsWith(".png")) {
                file = new File(file.getParent(), file.getName() + ".png");
            }
            
            // Save as PNG
            ImageIO.write(composite, "png", file);
            System.out.println("Drawing saved to: " + file.getAbsolutePath());
            return true;
            
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Load a drawing from a file
     * @param file The file to load from
     * @return The loaded image, or null if failed
     */
    public static BufferedImage loadDrawing(File file) {
        try {
            BufferedImage loadedImage = ImageIO.read(file);
            if (loadedImage != null) {
                System.out.println("Image loaded from: " + file.getAbsolutePath());
            }
            return loadedImage;
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
            return null;
        }
    }
}
