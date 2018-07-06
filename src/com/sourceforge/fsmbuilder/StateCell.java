package com.sourceforge.fsmbuilder;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * This is the state cell class
 */
final class StateCell extends DefaultGraphCell
{
    private static final long serialVersionUID = -6062923156200720075L;
    /**
     * Holds all state elements
     */
    private final StateData stateData = new StateData();

    /**
     * Generates a new state at position...
     * @param x X
     * @param y and Y
     */
    StateCell (int x, int y)
    {
        // Make vertex
        super();
        GraphConstants.setBounds(getAttributes(), new Rectangle2D.Double(x, y, 80, 80));
        GraphConstants.setEditable(getAttributes(), false);
        GraphConstants.setBackground(getAttributes(), new Color (180, 255, 180));
        GraphConstants.setOpaque(getAttributes(), true);
        GraphConstants.setBorder(getAttributes(), BorderFactory.createRaisedBevelBorder());
        makePorts();
        updateText();
    }

    /**
     * Updates the state vertex text
     */
    private void updateText()
    {
        String s = "<html>";
        s += "<u>";
        s += stateData.name;
        s += "</u>";
        if (stateData.entry != null)
            s += "<p align=\"center\" color=\"#2C2EFF\">Entry:" + stateData.entry;
        if (stateData.exit != null)
            s+= "<p align=\"center\" color=\"#6B6DFF\">Exit:" + stateData.exit;
        if (stateData.run != null)
            s +=  "<p align=\"center\" color=\"#5EA2FF\">Run:" + stateData.run;
        s += "</html>";
        setUserObject (s);
    }

    /**
     * Gets all data element of that state
     * @return Guess what?
     */
    StateData getData()
    {
        return stateData;
    }

    /**
     * Set a new name
     * @param n The state's name
     */
    void setName (String n)
    {
        stateData.name = StringChanger.makeCCompatible (n);
        updateText();
    }

    /**
     * Toggles if this state has an entry function
     */
    void toggleEntry (String name)
    {
        stateData.entry = name;
        updateText();
    }

    /**
     * Toggles this state's exit function
     */
    void toggleExit(String name)
    {
        stateData.exit = name;
        updateText();
    }

    /**
     * Toggles this state's run function
     */
    void toggleRun(String name)
    {
        stateData.run = name;
        updateText();
    }

    /**
     * Makes all ports for a StateCell
     */
    private void makePorts ()
    {
        DefaultPort p;
        int step = GraphConstants.PERMILLE / 16;
        int s;
        for (s = 0; s <= GraphConstants.PERMILLE; s += step)
        {
            p = new DefaultPort();
            GraphConstants.setOffset(p.getAttributes(), new Point2D.Double(s, 0));
            add(p);
        }
        for (s = 0; s <= GraphConstants.PERMILLE; s += step)
        {
            p = new DefaultPort();
            GraphConstants.setOffset(p.getAttributes(), new Point2D.Double(s, GraphConstants.PERMILLE));
            add(p);
        }
        for (s = 0; s <= GraphConstants.PERMILLE; s += step)
        {
            p = new DefaultPort();
            GraphConstants.setOffset(p.getAttributes(), new Point2D.Double(0, s));
            add(p);
        }
        for (s = 0; s <= GraphConstants.PERMILLE; s += step)
        {
            p = new DefaultPort();
            GraphConstants.setOffset(p.getAttributes(), new Point2D.Double(GraphConstants.PERMILLE, s));
            add(p);
        }
    }
}
