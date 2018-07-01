package com.sourceforge.fsmbuilder;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;

/**
 * This is the menu when mouse is over the starting transition
 */
class StartingTransitionMenu extends InnerPopupMenu
{
    StartingTransitionMenu (final MainFrame mainFrame, final MouseEvent ev, final StartingTransitionCell cell)
    {
        /**
         * Sets new action name
         */
        add (new TextMenuItem ("Define action", cell.getData().action)
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
                mainFrame.graph.getSelectionModel().removeSelectionCell(cell);
                mainFrame.graph.getSelectionModel().addSelectionCell(cell);
                mainFrame.graph.getUI().getHandle().mousePressed(ev);
                mainFrame.graph.getUI().getHandle().mouseReleased(ev);
            }
        });

        addMenuTail (mainFrame, cell);
        
        /**
         * Displays the menu on mouse point
         */
        positionAndShow (mainFrame, ev);
    }
}
