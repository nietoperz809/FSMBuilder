package com.sourceforge.fsmbuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This is the menu item with a TextField on its right side
 */
abstract class TextMenuItem extends JMenuItem implements ActionListener
{
    /**
     * The text field component
     */
    final JTextField text;

    /**
     * Consructs the text menu item
     * @param title Label on left side
     * @param txt Textfield initialiser on left side
     */
    TextMenuItem (String title, String txt)
    {
        setLayout (new BorderLayout());
        JLabel lab = new JLabel (title);
        lab.setPreferredSize (new Dimension (100, 25));
        if (txt == null)
            txt = "";
        text = new JTextField (txt);

        text.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                TextMenuItem.this.getParent().setVisible(false);
                TextMenuItem.this.actionPerformed(e);
            }
        });

        text.setPreferredSize (new Dimension (100, 25));
        add (lab, BorderLayout.WEST);
        add (text, BorderLayout.EAST);
        setPreferredSize (new Dimension (210, 25));
        addActionListener (this);
    }
}
