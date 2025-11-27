# Simple Drawing Application

A console-based drawing application written in Java without Swing dependencies. This is a simplified version of the advanced drawing studio that runs entirely in the terminal.

## Features

- **Text-based canvas**: Draw on a character-based 2D grid
- **Multiple drawing tools**: Points, lines, rectangles, circles, triangles
- **Color support**: 8 different colors with ANSI color codes
- **Multiple brush characters**: Various symbols for different drawing styles
- **Undo/Redo**: Up to 10 levels of undo/redo functionality
- **File operations**: Save and load drawings to/from text files
- **Flood fill**: Fill areas with specified character and color
- **Canvas resizing**: Dynamically resize the drawing canvas

## Files

1. **SimpleDrawingApp.java** - Main application with console interface and command processing
2. **DrawingCanvas.java** - Canvas implementation with drawing algorithms and state management
3. **SimpleShape.java** - Shape class for future extensibility and shape manipulation

## How to Compile and Run

```bash
# Compile all Java files
javac *.java

# Run the application
java SimpleDrawingApp
```

## Available Commands

### Drawing Commands
- `show` or `display` - Display the current canvas
- `draw <x> <y>` - Draw a point at coordinates (x, y)
- `line <x1> <y1> <x2> <y2>` - Draw a line from (x1, y1) to (x2, y2)
- `rectangle <x> <y> <width> <height>` - Draw a rectangle
- `circle <x> <y> <radius>` - Draw a circle centered at (x, y)
- `triangle <x1> <y1> <x2> <y2> <x3> <y3>` - Draw a triangle with three vertices
- `fill <x> <y>` - Flood fill area starting at (x, y)

### Canvas Commands
- `clear` - Clear the entire canvas
- `resize <height> <width>` - Resize the canvas
- `undo` - Undo the last action
- `redo` - Redo the last undone action

### Settings Commands
- `brush [character]` - Set brush character or show available options
- `color [colorname]` - Set drawing color or show available colors
- `tool [toolname]` - Set current tool or show available tools

### File Commands
- `save <filename>` - Save canvas to a text file
- `load <filename>` - Load canvas from a text file

### Other Commands
- `help` - Show all available commands
- `exit` or `quit` - Exit the application

## Examples

```
# Start drawing
Enter command: show
Enter command: brush #
Enter command: color red
Enter command: rectangle 5 10 20 8
Enter command: circle 15 20 5
Enter command: line 0 0 30 30
Enter command: show
Enter command: save my_drawing
```

## Available Colors

- WHITE (default)
- BLACK
- RED
- GREEN
- YELLOW
- BLUE
- PURPLE
- CYAN

## Available Brush Characters

`*` `#` `@` `o` `+` `.` `=` `-` `|` `/` and any other character you specify

## Coordinate System

- Origin (0, 0) is at the top-left corner
- X-axis goes down (rows)
- Y-axis goes right (columns)
- Default canvas size is 40 rows × 80 columns

## File Format

Saved files use a simple text format:
- Header: "SIMPLE_DRAWING_CANVAS"
- Dimensions: height width
- Canvas data: character|color pairs for each cell

## Key Differences from Original Swing Version

1. **No GUI**: Runs entirely in console/terminal
2. **Text-based graphics**: Uses characters instead of pixels
3. **Simplified interaction**: Command-line interface instead of mouse/buttons
4. **Limited colors**: 8 ANSI colors instead of full RGB
5. **Character brushes**: Different characters instead of variable brush sizes
6. **No layers**: Single drawing surface instead of multiple layers
7. **No real-time preview**: Commands execute immediately

## Tips for Use

1. Use `show` frequently to see your progress
2. Start with small coordinates to stay within bounds
3. Use different brush characters for variety
4. Save your work frequently
5. Experiment with flood fill for interesting effects
6. Use undo if you make mistakes

This simplified version maintains the core drawing functionality while being much more lightweight and dependency-free!