package com.drawingstudio.app;

import com.drawingstudio.canvas.DrawingCanvas;
import com.drawingstudio.utils.ColorUtils;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.io.*;

/**
 * Swing-Based Drawing Application
 * Main application class - demonstrates OOP concepts:
 * - Encapsulation: Private fields with controlled access
 * - Inheritance: Extends JFrame, implements interfaces
 * - Polymorphism: ActionListener, MouseListener interfaces
 * - Composition: Uses DrawingCanvas, ColorPalettePanel
 */
public class SimpleDrawingApp extends JFrame implements ActionListener, MouseListener, MouseMotionListener {
    private DrawingCanvas canvas;
    private Color currentColor = Color.BLACK;
    private int brushSize = 3;
    private String currentTool = "BRUSH";
    private String currentBrushType = "NORMAL";
    
    // UI Components - demonstrates encapsulation
    private JButton lineBtn, rectBtn, ovalBtn, triangleBtn, diamondBtn;
    private JButton clearBtn, undoBtn, redoBtn, saveBtn, loadBtn, colorPickerBtn, customColorBtn;
    private Choice colorChoice, brushChoice, toolChoice;
    private JPanel toolPanel, canvasPanel;
    private Label statusLabel;
    
    // Drawing state
    private boolean isDrawing = false;
    private Point startPoint, lastPoint;
    
    public SimpleDrawingApp() {
        setTitle("Simple Drawing Studio - AWT Version (Package Structure)");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        // Handle window closing
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        
        setVisible(true);
    }
    
    private void initializeComponents() {
        canvas = new DrawingCanvas(this);
        
        // Create shape buttons
        lineBtn = new JButton("Line");
        rectBtn = new JButton("Rectangle");
        ovalBtn = new JButton("Oval");
        triangleBtn = new JButton("Triangle");
        diamondBtn = new JButton("Diamond");
        
        // Create action buttons
        clearBtn = new JButton("Clear");
        undoBtn = new JButton("Undo");
        redoBtn = new JButton("Redo");
        saveBtn = new JButton("Save");
        loadBtn = new JButton("Load");
        
        // Create color buttons
        colorPickerBtn = new JButton("Color Picker");
        customColorBtn = new JButton("Custom Color...");
        
        // Apply styling to all buttons
        styleShapeButtons();
        styleActionButtons();
        styleColorButtons();
        
        // Create choice components with better color labels
        colorChoice = new Choice();
        colorChoice.add("Black");
        colorChoice.add("Red");
        colorChoice.add("Green");
        colorChoice.add("Blue");
        colorChoice.add("Yellow");
        colorChoice.add("Orange");
        colorChoice.add("Pink");
        colorChoice.add("Cyan");
        colorChoice.add("Magenta");
        colorChoice.add("White");
        colorChoice.add("Custom...");
        
        brushChoice = new Choice();
        for (int i = 1; i <= 10; i++) {
            brushChoice.add(String.valueOf(i));
        }
        brushChoice.select("3");
        
        toolChoice = new Choice();
        toolChoice.add("Brush");
        toolChoice.add("Eraser");
        toolChoice.add("Color Picker");
        
        // Create panels
        toolPanel = new JPanel(new GridLayout(2, 1));
        canvasPanel = new JPanel(new BorderLayout());
        
        // Status label
        statusLabel = new Label("Tool: " + currentTool + " | Color: Black | Brush Size: " + brushSize);
    }
    
    private void styleShapeButtons() {
        JButton[] shapeButtons = {lineBtn, rectBtn, ovalBtn, triangleBtn, diamondBtn};
        Color shapeColor = new Color(52, 152, 219); // Blue
        
        for (JButton btn : shapeButtons) {
            btn.setFont(new Font("Arial", Font.BOLD, 12));
            btn.setBackground(shapeColor);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorder(new LineBorder(new Color(41, 128, 185), 2));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setPreferredSize(new Dimension(100, 35));
        }
    }
    
    private void styleActionButtons() {
        JButton[] actionButtons = {clearBtn, undoBtn, redoBtn, saveBtn, loadBtn};
        Color actionColor = new Color(231, 76, 60); // Red
        
        for (JButton btn : actionButtons) {
            btn.setFont(new Font("Arial", Font.BOLD, 12));
            btn.setBackground(actionColor);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorder(new LineBorder(new Color(192, 57, 43), 2));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setPreferredSize(new Dimension(90, 35));
        }
    }
    
    private void styleColorButtons() {
        JButton[] colorButtons = {colorPickerBtn, customColorBtn};
        Color colorButtonColor = new Color(46, 204, 113); // Green
        
        for (JButton btn : colorButtons) {
            btn.setFont(new Font("Arial", Font.BOLD, 12));
            btn.setBackground(colorButtonColor);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorder(new LineBorder(new Color(39, 174, 96), 2));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setPreferredSize(new Dimension(130, 35));
        }
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Create sub-panels for better organization
        JPanel propertiesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel shapesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        // Properties Panel
        propertiesPanel.add(new Label("Color:"));
        propertiesPanel.add(colorChoice);
        propertiesPanel.add(customColorBtn);
        propertiesPanel.add(colorPickerBtn);
        propertiesPanel.add(new Label("Brush Size:"));
        propertiesPanel.add(brushChoice);
        propertiesPanel.add(new Label("Tool:"));
        propertiesPanel.add(toolChoice);
        
        // Shapes Panel
        shapesPanel.add(new Label("Shapes:"));
        shapesPanel.add(lineBtn);
        shapesPanel.add(rectBtn);
        shapesPanel.add(ovalBtn);
        shapesPanel.add(triangleBtn);
        shapesPanel.add(diamondBtn);
        
        // Action Panel
        actionPanel.add(new Label("Actions:"));
        actionPanel.add(clearBtn);
        actionPanel.add(undoBtn);
        actionPanel.add(redoBtn);
        actionPanel.add(saveBtn);
        actionPanel.add(loadBtn);
        
        // Combine panels
        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topRow.add(propertiesPanel);
        
        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomRow.add(shapesPanel);
        bottomRow.add(actionPanel);
        
        toolPanel.add(topRow);
        toolPanel.add(bottomRow);
        
        // Add canvas to canvas panel
        canvasPanel.add(canvas, BorderLayout.CENTER);
        
        // Add panels to frame
        add(toolPanel, BorderLayout.NORTH);
        add(canvasPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
    }
    
    private void setupEventHandlers() {
        // Add action listeners
        lineBtn.addActionListener(this);
        rectBtn.addActionListener(this);
        ovalBtn.addActionListener(this);
        triangleBtn.addActionListener(this);
        diamondBtn.addActionListener(this);
        clearBtn.addActionListener(this);
        undoBtn.addActionListener(this);
        redoBtn.addActionListener(this);
        saveBtn.addActionListener(this);
        loadBtn.addActionListener(this);
        colorPickerBtn.addActionListener(this);
        customColorBtn.addActionListener(this);
        
        // Add item listeners for choices - demonstrates event handling
        colorChoice.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                String colorName = colorChoice.getSelectedItem();
                if (colorName.equals("Custom...")) {
                    openCustomColorDialog();
                } else {
                    currentColor = ColorUtils.getColorFromName(colorName);
                    canvas.setCurrentColor(currentColor);
                    updateStatusLabel();
                }
            }
        });
        
        brushChoice.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                brushSize = Integer.parseInt(brushChoice.getSelectedItem());
                canvas.setBrushSize(brushSize);
                updateStatusLabel();
            }
        });
        
        toolChoice.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                String selectedTool = toolChoice.getSelectedItem();
                if (selectedTool.equals("Brush")) {
                    currentTool = "BRUSH";
                } else if (selectedTool.equals("Eraser")) {
                    currentTool = "ERASER";
                } else if (selectedTool.equals("Color Picker")) {
                    currentTool = "COLOR_PICKER";
                }
                canvas.setCurrentTool(currentTool);
                updateStatusLabel();
            }
        });
        
        // Add mouse listeners to canvas
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        
        switch (command) {
            case "Line":
                currentTool = "LINE";
                toolChoice.select(0);
                canvas.setCurrentTool(currentTool);
                updateStatusLabel();
                break;
            case "Rectangle":
                currentTool = "RECTANGLE";
                toolChoice.select(0);
                canvas.setCurrentTool(currentTool);
                updateStatusLabel();
                break;
            case "Oval":
                currentTool = "OVAL";
                toolChoice.select(0);
                canvas.setCurrentTool(currentTool);
                updateStatusLabel();
                break;
            case "Triangle":
                currentTool = "TRIANGLE";
                toolChoice.select(0);
                canvas.setCurrentTool(currentTool);
                updateStatusLabel();
                break;
            case "Diamond":
                currentTool = "DIAMOND";
                toolChoice.select(0);
                canvas.setCurrentTool(currentTool);
                updateStatusLabel();
                break;
            case "Color Picker":
                currentTool = "COLOR_PICKER";
                toolChoice.select("Color Picker");
                canvas.setCurrentTool(currentTool);
                updateStatusLabel();
                statusLabel.setText("Color Picker: Click on canvas to pick a color | Tool: " + currentTool);
                break;
            case "Custom Color...":
                openCustomColorDialog();
                break;
            case "Clear":
                canvas.clearCanvas();
                break;
            case "Undo":
                canvas.undo();
                break;
            case "Redo":
                canvas.redo();
                break;
            case "Save":
                saveDrawing();
                break;
            case "Load":
                loadDrawing();
                break;
        }
    }
    
    // Mouse event implementations - delegates to canvas
    @Override
    public void mousePressed(MouseEvent e) {
        isDrawing = true;
        startPoint = e.getPoint();
        lastPoint = e.getPoint();
        canvas.handleMousePressed(e);
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        if (isDrawing) {
            lastPoint = e.getPoint();
            canvas.handleMouseDragged(e);
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        isDrawing = false;
        canvas.handleMouseReleased(e);
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        canvas.handleMouseClicked(e);
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {}
    
    @Override
    public void mouseExited(MouseEvent e) {}
    
    @Override
    public void mouseMoved(MouseEvent e) {
        canvas.handleMouseMoved(e);
    }
    
    private void updateStatusLabel() {
        statusLabel.setText("Tool: " + currentTool + " | Color: " + ColorUtils.getColorName(currentColor) + " | Brush Size: " + brushSize);
    }
    
    /**
     * Public method called by canvas when color is picked
     * Demonstrates inter-component communication
     */
    public void setPickedColor(Color color) {
        this.currentColor = color;
        canvas.setCurrentColor(color);
        
        // Update status to show picked color
        String colorInfo = ColorUtils.formatRGB(color);
        statusLabel.setText(colorInfo + " | Tool: " + currentTool + " | Brush Size: " + brushSize);
        
        // Try to find matching color in dropdown
        String colorName = ColorUtils.getColorName(color);
        for (int i = 0; i < colorChoice.getItemCount(); i++) {
            String item = colorChoice.getItem(i);
            if (item.contains(colorName)) {
                colorChoice.select(i);
                return;
            }
        }
        // If no match found, select Custom
        colorChoice.select(colorChoice.getItemCount() - 1);
    }
    
    private void openCustomColorDialog() {
        JDialog colorDialog = new JDialog(this, "Choose Custom Color", true);
        colorDialog.setLayout(new BorderLayout());
        colorDialog.setSize(450, 400);
        colorDialog.setLocationRelativeTo(this);
        
        // Create color palette panel - demonstrates composition
        ColorPalettePanel palettePanel = new ColorPalettePanel();
        colorDialog.add(palettePanel, BorderLayout.CENTER);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton okBtn = new JButton("OK");
        JButton cancelBtn = new JButton("Cancel");
        
        // Style dialog buttons
        Color dialogBtnColor = new Color(52, 152, 219);
        for (JButton btn : new JButton[]{okBtn, cancelBtn}) {
            btn.setFont(new Font("Arial", Font.BOLD, 11));
            btn.setBackground(dialogBtnColor);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorder(new LineBorder(new Color(41, 128, 185), 2));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setPreferredSize(new Dimension(80, 30));
        }
        
        okBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentColor = palettePanel.getSelectedColor();
                canvas.setCurrentColor(currentColor);
                updateStatusLabel();
                colorDialog.dispose();
            }
        });
        
        cancelBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                colorDialog.dispose();
            }
        });
        
        buttonPanel.add(okBtn);
        buttonPanel.add(cancelBtn);
        colorDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        colorDialog.setVisible(true);
    }
    
    private void saveDrawing() {
        FileDialog fileDialog = new FileDialog(this, "Save Drawing", FileDialog.SAVE);
        fileDialog.setFile("*.png");
        fileDialog.setVisible(true);
        
        String filename = fileDialog.getFile();
        if (filename != null) {
            String directory = fileDialog.getDirectory();
            File file = new File(directory, filename);
            canvas.saveToFile(file);
        }
    }
    
    private void loadDrawing() {
        FileDialog fileDialog = new FileDialog(this, "Load Drawing", FileDialog.LOAD);
        fileDialog.setFile("*.png");
        fileDialog.setVisible(true);
        
        String filename = fileDialog.getFile();
        if (filename != null) {
            String directory = fileDialog.getDirectory();
            File file = new File(directory, filename);
            canvas.loadFromFile(file);
        }
    }
    
    public static void main(String[] args) {
        new SimpleDrawingApp();
    }
    
    /**
     * Inner class for custom color palette
     * Demonstrates nested classes and encapsulation
     */
    class ColorPalettePanel extends JPanel {
        private Color selectedColor = Color.BLACK;
        private Canvas paletteCanvas;
        private Canvas previewCanvas;
        private JLabel rgbLabel;
        
        public ColorPalettePanel() {
            setLayout(new BorderLayout());
            
            // Create gradient palette canvas
            paletteCanvas = new Canvas() {
                @Override
                public void paint(Graphics g) {
                    int width = getWidth();
                    int height = getHeight();
                    
                    // Draw HSB gradient
                    for (int y = 0; y < height; y++) {
                        for (int x = 0; x < width; x++) {
                            float hue = (float) x / width;
                            float saturation = 1.0f;
                            float brightness = 1.0f - (float) y / height;
                            Color c = Color.getHSBColor(hue, saturation, brightness);
                            g.setColor(c);
                            g.fillRect(x, y, 1, 1);
                        }
                    }
                }
            };
            paletteCanvas.setSize(350, 250);
            
            // Add mouse listener to palette
            paletteCanvas.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    selectColorAt(e.getX(), e.getY());
                }
            });
            
            paletteCanvas.addMouseMotionListener(new MouseMotionAdapter() {
                public void mouseDragged(MouseEvent e) {
                    selectColorAt(e.getX(), e.getY());
                }
            });
            
            // Create preview panel
            JPanel previewPanel = new JPanel(new BorderLayout());
            previewCanvas = new Canvas() {
                @Override
                public void paint(Graphics g) {
                    g.setColor(selectedColor);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            previewCanvas.setSize(80, 80);
            
            rgbLabel = new JLabel("RGB: 0, 0, 0");
            rgbLabel.setFont(new Font("Arial", Font.PLAIN, 11));
            
            previewPanel.add(new JLabel("Selected Color:"), BorderLayout.NORTH);
            previewPanel.add(previewCanvas, BorderLayout.CENTER);
            previewPanel.add(rgbLabel, BorderLayout.SOUTH);
            
            add(paletteCanvas, BorderLayout.CENTER);
            add(previewPanel, BorderLayout.EAST);
        }
        
        private void selectColorAt(int x, int y) {
            int width = paletteCanvas.getWidth();
            int height = paletteCanvas.getHeight();
            
            if (x >= 0 && x < width && y >= 0 && y < height) {
                float hue = (float) x / width;
                float saturation = 1.0f;
                float brightness = 1.0f - (float) y / height;
                selectedColor = Color.getHSBColor(hue, saturation, brightness);
                
                previewCanvas.repaint();
                rgbLabel.setText("RGB: " + selectedColor.getRed() + ", " + 
                               selectedColor.getGreen() + ", " + selectedColor.getBlue());
            }
        }
        
        public Color getSelectedColor() {
            return selectedColor;
        }
    }
}
