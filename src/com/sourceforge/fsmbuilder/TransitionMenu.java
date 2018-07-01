package com.sourceforge.fsmbuilder;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

/**
 * This the menu when mouse is over a transition.
 */
final class TransitionMenu extends InnerPopupMenu
{
    /**
     * Constructor. Builds all the menu items
     * @param mainFrame Ref. to main frame
     * @param ev The mouse event which causes the menu to open
     * @param cell The transition cell under the mouse
     */
    TransitionMenu(final MainFrame mainFrame, final MouseEvent ev, final TransitionCell cell)
    {
        /**
         * Sets new state name
         */
        add(new TextMenuItem("Set name", cell.getData().name)
        {
            public void actionPerformed(ActionEvent e)
            {
                cell.setName (text.getText());
                mainFrame.forceRepaint(cell);
            }
        });

        /**
         * Sets new action name
         */
        add(new TextMenuItem("Define Action", cell.getData().action)
        {
            public void actionPerformed(ActionEvent e)
            {
                cell.setAction(text.getText());
                mainFrame.forceRepaint(cell);
            }
        });

        addSeparator();

        /**
         * Toggle new control point
         */
        add(new AbstractAction("Toggle control point")
        {
            public void actionPerformed(ActionEvent e)
            {
                // Fake mouse press
                mainFrame.graph.getSelectionModel().removeSelectionCell(cell);
                mainFrame.graph.getSelectionModel().addSelectionCell(cell);
                mainFrame.graph.getUI().getHandle().mousePressed(ev);
                mainFrame.graph.getUI().getHandle().mouseReleased(ev);
            }
        });

        addMenuTail(mainFrame, cell);

        /**
         * Displays the menu on mouse point
         */
        positionAndShow(mainFrame, ev);
    }

}

