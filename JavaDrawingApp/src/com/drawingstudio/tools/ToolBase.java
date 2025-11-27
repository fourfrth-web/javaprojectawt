package com.drawingstudio.tools;

/**
 * Base class for all drawing tools
 * Demonstrates abstraction and tool design pattern
 */
public abstract class ToolBase {
    protected String toolName;
    protected String toolType;
    
    public ToolBase(String name, String type) {
        this.toolName = name;
        this.toolType = type;
    }
    
    public String getToolName() {
        return toolName;
    }
    
    public String getToolType() {
        return toolType;
    }
    
    /**
     * Check if this tool is a shape tool
     */
    public boolean isShapeTool() {
        return toolType.equals("SHAPE");
    }
    
    /**
     * Check if this tool is a brush tool
     */
    public boolean isBrushTool() {
        return toolType.equals("BRUSH") || toolType.equals("ERASER");
    }
}
