package com.sourceforge.fsmbuilder;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

/**
 * This is the popup menu for states
 */
class StateMenu extends InnerPopupMenu
{
    StateMenu(final MainFrame mainframe, MouseEvent ev, final StateCell cell)
    {
        /**
         * Sets new state name
         */
        add(new TextMenuItem("Set name", cell.getData().name)
        {
            public void actionPerformed(ActionEvent e)
            {
                cell.setName(text.getText());
                mainframe.forceRepaint(cell);
            }
        });

        addSeparator();

        add(new TextMenuItem("Entry", cell.getData().entry)
        {
            public void actionPerformed(ActionEvent e)
            {
                cell.toggleEntry(text.getText());
                mainframe.forceRepaint(cell);
            }
        });

        add(new TextMenuItem("Exit", cell.getData().exit)
        {
            public void actionPerformed(ActionEvent e)
            {
                cell.toggleExit(text.getText());
                mainframe.forceRepaint(cell);
            }
        });

        add(new TextMenuItem("Run", cell.getData().run)
        {
            public void actionPerformed(ActionEvent e)
            {
                cell.toggleRun(text.getText());
                mainframe.forceRepaint(cell);
            }
        });

        addMenuTail (mainframe, cell);

        /**
         * Displays the menu on mouse point
         */
        positionAndShow (mainframe, ev);
    }
}
