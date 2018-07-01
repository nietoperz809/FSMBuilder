/*
 * @(#)GraphCellEditor.java	1.0 03-JUL-04
 * 
 * Copyright (c) 2001-2004 Gaudenz Alder
 *  
 */
package org.jgraph.graph;

import org.jgraph.JGraph;

import javax.swing.*;
import java.awt.*;

/**
 * Adds to CellEditor the extensions necessary to configure an editor
 * in a graph.
 *
 * @author Gaudenz Alder
 * @version 1.0 1/1/02
 */

public interface GraphCellEditor extends CellEditor
{
    /**
     * Sets an initial <I>value</I> for the editor.  This will cause
     * the editor to stopEditing and lose any partially edited value
     * if the editor is editing when this method is called. <p>
     * <p/>
     * Returns the component that should be added to the client's
     * Component hierarchy.  Once installed in the client's hierarchy
     * this component will then be able to draw and receive user input.
     *
     * @param    graph        the JGraph that is asking the editor to edit
     * This parameter can be null.
     * @param    value        the value of the cell to be edited.
     * @param    isSelected    true if the cell is to be rendered with
     * selection highlighting
     * @return the component for editing
     */
    Component getGraphCellEditorComponent(
		JGraph graph,
		Object value,
		boolean isSelected);
}