/*
 * @(#)DefaultCellEditor.java 1.0 03-JUL-04
 * 
 * Copyright (c) 2001-2004 Gaudenz Alder
 *  
 */
package org.jgraph.graph;

import org.jgraph.JGraph;

import javax.swing.*;
import java.awt.*;

/**
 * The default editor for graph cells.
 *
 * @author Gaudenz Alder
 * @version 1.0 1/1/02
 */
public class DefaultRealEditor extends DefaultCellEditor implements GraphCellEditor
{

    //
    //  Constructors
    //
    /**
     * Constructs a DefaultCellEditor that uses a text field.
     *
     * @param textField a JTextField object used as the editor
     */
    public DefaultRealEditor(final JTextField textField)
    {
        super(textField);
        setClickCountToStart(1);
    }

    /**
     * Constructs a DefaultCellEditor object that uses a check box.
     *
     * @param checkBox a JCheckBox object
     */
    public DefaultRealEditor(final JCheckBox checkBox)
    {
        super(checkBox);
    }

    /**
     * Constructs a DefaultCellEditor object that uses a combo box.
     *
     * @param comboBox a JComboBox object
     */
    public DefaultRealEditor(final JComboBox comboBox)
    {
        super(comboBox);
    }

    //
    //  GraphCellEditor Interface
    //
    public Component getGraphCellEditorComponent(JGraph graph, Object value, boolean isSelected)
    {
        String stringValue = graph.convertValueToString(value);
        delegate.setValue(stringValue);
        if (editorComponent instanceof JTextField)
        {
            ((JTextField) editorComponent).selectAll();
        }
        return editorComponent;
	}
} // End of class JCellEditor
