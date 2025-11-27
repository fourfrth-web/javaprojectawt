# Simple Drawing Studio - Package Structure

## Overview
This is a complete Java drawing application demonstrating Object-Oriented Programming (OOP) concepts through proper package organization and design patterns.

## Project Structure

```
src/
└── com/
    └── drawingstudio/
        ├── app/                    # Main application
        │   └── SimpleDrawingApp.java
        ├── canvas/                 # Drawing canvas
        │   └── DrawingCanvas.java
        ├── shapes/                 # Shape implementations
        │   ├── ShapeBase.java     (Abstract base class)
        │   ├── LineShape.java
        │   ├── RectShape.java
        │   ├── OvalShape.java
        │   ├── TriangleShape.java
        │   └── DiamondShape.java
        ├── tools/                  # Tool implementations
        │   ├── ToolBase.java      (Abstract base class)
        │   ├── BrushTool.java
        │   ├── EraserTool.java
        │   ├── LineTool.java
        │   ├── RectangleTool.java
        │   ├── OvalTool.java
        │   ├── TriangleTool.java
        │   └── DiamondTool.java
        ├── brush/                  # Brush types
        │   ├── BrushType.java     (Interface)
        │   ├── NormalBrush.java
        │   └── EraserBrush.java
        ├── manager/                # Manager classes
        │   ├── HistoryManager.java
        │   └── FileManager.java
        ├── utils/                  # Utility classes
        │   ├── ColorUtils.java
        │   ├── PointUtils.java
        │   └── ShapeUtils.java
        └── events/                 # Event handlers
            ├── CanvasMouseHandler.java
            └── CanvasMotionHandler.java
```

## OOP Concepts Demonstrated

### 1. **Encapsulation**
- **Private Fields**: All classes use private fields with controlled access through getters/setters
- **Information Hiding**: Internal implementation details are hidden from external classes
- **Examples**: 
  - `ShapeBase` encapsulates shape properties (type, color, strokeWidth)
  - `HistoryManager` encapsulates undo/redo history

### 2. **Inheritance**
- **Abstract Base Classes**: `ShapeBase`, `ToolBase` provide common functionality
- **Class Hierarchy**: All shapes inherit from `ShapeBase`
- **Examples**:
  - `LineShape extends ShapeBase`
  - `RectShape extends ShapeBase`
  - `SimpleDrawingApp extends Frame`

### 3. **Polymorphism**
- **Method Overriding**: Each shape overrides `draw()` method with custom implementation
- **Interface Implementation**: `BrushType` interface with multiple implementations
- **Examples**:
  - `shape.draw(g2d)` calls appropriate draw method based on actual shape type
  - `BrushType` implemented by `NormalBrush` and `EraserBrush`

### 4. **Abstraction**
- **Abstract Classes**: `ShapeBase` defines common shape behavior
- **Interfaces**: `BrushType` defines brush contract
- **Examples**:
  - Cannot instantiate `ShapeBase` directly
  - Must implement all `BrushType` methods

### 5. **Composition**
- **Has-A Relationships**: Classes composed of other classes
- **Examples**:
  - `SimpleDrawingApp` has a `DrawingCanvas`
  - `DrawingCanvas` has a `HistoryManager`
  - `DrawingCanvas` has a List of `ShapeBase` objects

### 6. **Packages**
- **Organization**: Code organized into logical packages
- **Access Control**: Package-private vs public access
- **Import Statements**: Explicit imports show dependencies
- **Examples**:
  - `com.drawingstudio.shapes` - Shape-related classes
  - `com.drawingstudio.utils` - Utility classes
  - `com.drawingstudio.manager` - Manager classes

### 7. **Design Patterns**

#### Factory Pattern
- `ShapeUtils.createShape()` creates shapes based on type
```java
ShapeBase shape = ShapeUtils.createShape("LINE", start, end, color, width);
```

#### Delegation Pattern
- `SimpleDrawingApp` delegates mouse events to `DrawingCanvas`
- `DrawingCanvas` delegates file I/O to `FileManager`

#### Strategy Pattern
- `BrushType` interface allows different brush behaviors
- Can easily add new brush types without modifying existing code

## Running the Application

### Using the Batch File
Simply double-click `run.bat` to compile and run the application.

### Manual Compilation
```batch
# Create bin directory
mkdir bin

# Compile all packages
javac -d bin -sourcepath src src\com\drawingstudio\app\SimpleDrawingApp.java

# Run the application
java -cp bin com.drawingstudio.app.SimpleDrawingApp
```

## Features

- **Drawing Tools**: Brush, Eraser, Line, Rectangle, Oval, Triangle, Diamond
- **Color Selection**: 10 preset colors + custom color palette with HSB gradient
- **Color Picker**: Eyedropper tool to pick colors from canvas
- **Brush Sizes**: Adjustable from 1 to 10 pixels
- **Undo/Redo**: History management with up to 10 steps
- **File Operations**: Save and load drawings as PNG
- **Shape Eraser**: Click shapes with eraser to delete them
- **Double Buffering**: Flicker-free drawing experience

## Key Learning Points

### Package Organization
1. **Separation of Concerns**: Each package has a specific responsibility
2. **Modularity**: Easy to locate and modify related code
3. **Maintainability**: Changes in one package don't affect others
4. **Reusability**: Utility classes can be reused across packages

### Import Statements
```java
import com.drawingstudio.shapes.*;        // Import all shapes
import com.drawingstudio.utils.ColorUtils; // Import specific class
```

### Access Modifiers
- `public`: Accessible from any package
- `protected`: Accessible within package and subclasses
- `private`: Accessible only within the class
- (default): Accessible only within the package

### Package Declaration
Every class must declare its package:
```java
package com.drawingstudio.shapes;
```

## Educational Value

This project structure is ideal for:
- Understanding Java package system
- Learning OOP principles in practice
- Seeing design patterns in action
- College presentations on software architecture
- Demonstrating clean code organization

## Authors
Created as a college project to demonstrate OOP concepts and Java package structure.

## License
Educational use only.
