import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

class RoundedButton extends JButton {
    private static final int ARC = 20;
    protected Color borderColor = Color.BLACK;
    
    public RoundedButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);
        setBorderPainted(false);
    }
    
    public RoundedButton(String text, Color borderColor) {
        super(text);
        this.borderColor = borderColor;
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);
        setBorderPainted(false);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Fill rounded rectangle
        if (getModel().isArmed()) {
            g2.setColor(getBackground().darker());
        } else {
            g2.setColor(getBackground());
        }
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), ARC, ARC);
        
        // Draw border with custom color
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, ARC, ARC);
        
        g2.dispose();
        super.paintComponent(g);
    }
    
    @Override
    protected void paintBorder(Graphics g) {
        // Do nothing to prevent default border painting
    }
}

public class SimpleDrawingApp extends JFrame implements ActionListener, MouseListener, MouseMotionListener {
    private DrawingCanvas canvas;
    private Color currentColor = Color.BLACK;
    private int brushSize = 3;
    private String currentTool = "BRUSH";
    
    // UI Components
    private JButton lineBtn, rectBtn, ovalBtn, triangleBtn, diamondBtn;
    private JButton clearBtn, undoBtn, redoBtn, saveBtn, loadBtn, colorPickerBtn, customColorBtn;
    private Choice colorChoice, brushChoice, toolChoice;
    private JPanel toolPanel;
    private Label statusLabel;
    private Canvas colorPreviewBox;
    
    public SimpleDrawingApp() {
        setTitle("Simple Drawing Studio - AWT Version");
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
        
        // Create shape buttons with rounded corners and blue borders
        lineBtn = new RoundedButton("Line", new Color(41, 128, 185));
        rectBtn = new RoundedButton("Rectangle", new Color(41, 128, 185));
        ovalBtn = new RoundedButton("Oval", new Color(41, 128, 185));
        triangleBtn = new RoundedButton("Triangle", new Color(41, 128, 185));
        diamondBtn = new RoundedButton("Diamond", new Color(41, 128, 185));
        
        // Create action buttons with rounded corners and red borders
        clearBtn = new RoundedButton("Clear", new Color(192, 57, 43));
        undoBtn = new RoundedButton("Undo", new Color(192, 57, 43));
        redoBtn = new RoundedButton("Redo", new Color(192, 57, 43));
        saveBtn = new RoundedButton("Save", new Color(192, 57, 43));
        loadBtn = new RoundedButton("Load", new Color(192, 57, 43));
        
        // Create color buttons with rounded corners and green borders
        colorPickerBtn = new RoundedButton("Color Picker", new Color(39, 174, 96));
        customColorBtn = new RoundedButton("Custom Color...", new Color(39, 174, 96));
        
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
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setPreferredSize(new Dimension(80, 28));
        }
    }
    
    private Label createStyledLabel(String text) {
        Label label = new Label(text);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(Color.WHITE);
        label.setBackground(new Color(40, 40, 40));
        return label;
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        Color darkGray = new Color(40, 40, 40);
        
        toolPanel = new JPanel(new GridLayout(1, 3, 10, 5));
        toolPanel.setPreferredSize(new Dimension(1000, 120));
        toolPanel.setBackground(darkGray);
        
        JPanel propertiesPanel = new JPanel(new BorderLayout());
        propertiesPanel.setBackground(darkGray);
        JPanel propertiesHeader = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
        propertiesHeader.setBackground(darkGray);
        propertiesHeader.add(createStyledLabel("Properties:"));
        JPanel propertiesContent = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
        propertiesContent.setBackground(darkGray);
        
        JPanel colorSelectionPanel = new JPanel();
        colorSelectionPanel.setLayout(new BoxLayout(colorSelectionPanel, BoxLayout.Y_AXIS));
        colorSelectionPanel.setBackground(darkGray);
        colorSelectionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel colorChoiceRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 0));
        colorChoiceRow.setBackground(darkGray);
        colorChoiceRow.add(createStyledLabel("Color:"));
        colorChoiceRow.add(colorChoice);
        colorSelectionPanel.add(colorChoiceRow);
        
        colorPreviewBox = new Canvas() {
            @Override
            public void paint(Graphics g) {
                g.setColor(currentColor);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.GRAY);
                g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            }
        };
        colorPreviewBox.setSize(50, 20);
        JPanel previewRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        previewRow.setBackground(darkGray);
        previewRow.add(colorPreviewBox);
        colorSelectionPanel.add(previewRow);
        
        propertiesContent.add(colorSelectionPanel);
        propertiesContent.add(customColorBtn);
        propertiesContent.add(colorPickerBtn);
        propertiesContent.add(createStyledLabel("Brush Size:"));
        propertiesContent.add(brushChoice);
        propertiesContent.add(createStyledLabel("Tool:"));
        propertiesContent.add(toolChoice);
        propertiesPanel.add(propertiesHeader, BorderLayout.NORTH);
        propertiesPanel.add(propertiesContent, BorderLayout.CENTER);
        
        JPanel shapesPanel = new JPanel(new BorderLayout());
        shapesPanel.setBackground(darkGray);
        JPanel shapesHeader = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
        shapesHeader.setBackground(darkGray);
        shapesHeader.add(createStyledLabel("Shapes:"));
        JPanel shapesContent = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
        shapesContent.setBackground(darkGray);
        shapesContent.add(lineBtn);
        shapesContent.add(rectBtn);
        shapesContent.add(ovalBtn);
        shapesContent.add(triangleBtn);
        shapesContent.add(diamondBtn);
        shapesPanel.add(shapesHeader, BorderLayout.NORTH);
        shapesPanel.add(shapesContent, BorderLayout.CENTER);
        
        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setBackground(darkGray);
        JPanel actionHeader = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
        actionHeader.setBackground(darkGray);
        actionHeader.add(createStyledLabel("Actions:"));
        JPanel actionContent = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
        actionContent.setBackground(darkGray);
        actionContent.add(clearBtn);
        actionContent.add(undoBtn);
        actionContent.add(redoBtn);
        actionContent.add(saveBtn);
        actionContent.add(loadBtn);
        actionPanel.add(actionHeader, BorderLayout.NORTH);
        actionPanel.add(actionContent, BorderLayout.CENTER);
        
        toolPanel.add(propertiesPanel);
        toolPanel.add(shapesPanel);
        toolPanel.add(actionPanel);
        
        add(toolPanel, BorderLayout.NORTH);
        add(canvas, BorderLayout.CENTER);
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
        
        // Add item listeners for choices
        colorChoice.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                String colorName = colorChoice.getSelectedItem();
                if (colorName.equals("Custom...")) {
                    openCustomColorDialog();
                } else {
                    currentColor = getColorFromName(colorName);
                    canvas.setCurrentColor(currentColor);
                    colorPreviewBox.repaint();
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
                toolChoice.select(0); // Reset to Brush
                canvas.setCurrentTool(currentTool);
                updateStatusLabel();
                break;
            case "Rectangle":
                currentTool = "RECTANGLE";
                toolChoice.select(0); // Reset to Brush
                canvas.setCurrentTool(currentTool);
                updateStatusLabel();
                break;
            case "Oval":
                currentTool = "OVAL";
                toolChoice.select(0); // Reset to Brush
                canvas.setCurrentTool(currentTool);
                updateStatusLabel();
                break;
            case "Triangle":
                currentTool = "TRIANGLE";
                toolChoice.select(0); // Reset to Brush
                canvas.setCurrentTool(currentTool);
                updateStatusLabel();
                break;
            case "Diamond":
                currentTool = "DIAMOND";
                toolChoice.select(0); // Reset to Brush
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
    
    @Override
    public void mousePressed(MouseEvent e) { canvas.mousePressed(e); }
    
    @Override
    public void mouseDragged(MouseEvent e) { canvas.mouseDragged(e); }
    
    @Override
    public void mouseReleased(MouseEvent e) { canvas.mouseReleased(e); }
    
    @Override
    public void mouseClicked(MouseEvent e) { canvas.mouseClicked(e); }
    
    @Override
    public void mouseEntered(MouseEvent e) {}
    
    @Override
    public void mouseExited(MouseEvent e) {}
    
    @Override
    public void mouseMoved(MouseEvent e) { canvas.mouseMoved(e); }
    
    private Color getColorFromName(String name) {
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
    
    private String getColorName(Color color) {
        Color[] colors = {Color.BLACK, Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.ORANGE, Color.PINK, Color.CYAN, Color.MAGENTA, Color.WHITE};
        String[] names = {"Black", "Red", "Green", "Blue", "Yellow", "Orange", "Pink", "Cyan", "Magenta", "White"};
        for (int i = 0; i < colors.length; i++) {
            if (color.equals(colors[i])) return names[i];
        }
        return "Black";
    }
    
    private void updateStatusLabel() {
        statusLabel.setText("Tool: " + currentTool + " | Color: " + getColorName(currentColor) + " | Brush Size: " + brushSize);
    }
    
    public void setPickedColor(Color color) {
        this.currentColor = color;
        canvas.setCurrentColor(color);
        colorPreviewBox.repaint();
        
        // Update status to show picked color
        String colorInfo = String.format("Picked Color - RGB(%d, %d, %d)", 
            color.getRed(), color.getGreen(), color.getBlue());
        statusLabel.setText(colorInfo + " | Tool: " + currentTool + " | Brush Size: " + brushSize);
        
        // Try to find matching color in dropdown
        String colorName = getColorName(color);
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
        
        // Create color palette panel
        ColorPalettePanel palettePanel = new ColorPalettePanel();
        colorDialog.add(palettePanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton okBtn = new JButton("OK");
        JButton cancelBtn = new JButton("Cancel");
        Color dialogBtnColor = new Color(52, 152, 219);
        
        for (JButton btn : new JButton[]{okBtn, cancelBtn}) {
            btn.setFont(new Font("Arial", Font.BOLD, 11));
            btn.setBackground(dialogBtnColor);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setPreferredSize(new Dimension(80, 30));
        }
        
        okBtn.addActionListener(e -> {
            currentColor = palettePanel.getSelectedColor();
            canvas.setCurrentColor(currentColor);
            colorPreviewBox.repaint();
            updateStatusLabel();
            colorDialog.dispose();
        });
        
        cancelBtn.addActionListener(e -> colorDialog.dispose());
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