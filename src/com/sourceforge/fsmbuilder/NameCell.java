package com.sourceforge.fsmbuilder;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * This is the name label
 */
final class NameCell extends DefaultGraphCell
{
    private static final long serialVersionUID = 1338639345844855672L;
    /**
     * Name of the FSM
     */
    private String name;

    /**
     * Global comment
     */
    private String comment = null;

    /**
     * Constructs the name cell
     * @param initialName This is the initial name
     */
    NameCell(String initialName)
    {
        // Make vertex
        super();
        GraphConstants.setBounds(getAttributes(), new Rectangle2D.Double(3, 3, 100, 20));
        GraphConstants.setOpaque(getAttributes(), true);
        GraphConstants.setBorderColor(getAttributes(), Color.black);
        GraphConstants.setAutoSize(getAttributes(), true);
        //GraphConstants.setSelectable(getAttributes(), false);
        GraphConstants.setEditable(getAttributes(), false);
        GraphConstants.setInset(getAttributes(), 3);
        setName(initialName);
    }

    /**
     * Updates visual representation
     */
    private void updateText()
    {
        StringBuilder n = new StringBuilder()
                .append("<html><p align=\"center\">FSM: <u>")
                .append(name)
                .append("</u>");
        if (comment != null)
        {
            n.append ("<hr><p><i>");
            n.append (comment);
            n.append ("<i>");
        }
        n.append("</html>");
        setUserObject(n.toString());
    }

    /**
     * Sets a new name
     * @param n The new name
     */
    void setName(String n)
    {
        name = StringChanger.makeCCompatible (n);
        updateText();
    }

    /**
     * Retrieves the current name
     * @return Guess what?
     */
    String getName()
    {
        return name;
    }

    /**
     * Gets the current comment
     * @return Guess what?
     */
    public String getComment()
    {
        return comment;
    }

    /**
     * Sets a new comment
     * @param comment Is the comment string
     */
    public void setComment(String comment)
    {
        if (comment.equals(""))
            comment = null;
        this.comment = comment;
        updateText();
    }

}
