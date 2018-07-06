package com.sourceforge.fsmbuilder;

import org.jgraph.graph.BasicMarqueeHandler;
import org.jgraph.graph.CellView;

import javax.swing.*;
import java.awt.event.MouseEvent;

/**
 * Handle mouse events and other stuff
 */
class MouseInputHandler extends BasicMarqueeHandler
{
    /**
     * Ref to the main frame
     */
    private final MainFrame mainFrame;

    /**
     * Constructor: takes the main frame
     * @param f References the main frame
     */
    MouseInputHandler(MainFrame f)
    {
        mainFrame = f;
    }

    /**
     * Override to Gain Control (for PopupMenu and ConnectMode)
     */
    @Override
    public boolean isForceMarqueeEvent(MouseEvent e)
    {
        // If Right Mouse Button we want to Display the PopupMenu
        if (SwingUtilities.isRightMouseButton(e))
        {
            return true;
        }
        return super.isForceMarqueeEvent(e);
    }

    /**
     * Display PopupMenu or Remember Start Location and First Port
     */
    @Override
    public void mousePressed(final MouseEvent e)
    {
        //System.out.println (e.toString());

        Object cell = mainFrame.graph.getFirstCellForLocation(e.getX(), e.getY());

        if (SwingUtilities.isRightMouseButton(e))
        {
            if (cell instanceof StateCell)
            {
                new StateMenu (mainFrame, e, (StateCell)cell);
            }
            else if (cell instanceof StartingTransitionCell)
            {
                new StartingTransitionMenu (mainFrame, e, (StartingTransitionCell)cell);
            }
            else if (cell instanceof TransitionCell)
            {
                new TransitionMenu (mainFrame, e, (TransitionCell)cell);
            }
            else if (cell instanceof NameCell)
            {
                // do nothing
            }
            else
            {
                new MainMenu(mainFrame, e);
            }
        }
        else
        {
            super.mousePressed(e);
        }
    }

/*
    // Find Port under Mouse and Repaint Connector
    public void mouseDragged(MouseEvent e)
    {
        //System.out.println (e.toString());
        super.mouseDragged(e);
    }

    // Connect the First Port and the Current Port in the Graph or Repaint
    public void mouseReleased(MouseEvent e)
    {
        //System.out.println (e.toString());
        super.mouseReleased(e);
    }
*/

    // Show Special Cursor if Over Port
    @Override
    public void mouseMoved(MouseEvent e)
    {
        //System.gc();
        super.mouseMoved(e);
        Object cell = mainFrame.graph.getFirstCellForLocation(e.getX(), e.getY());
        if (cell != null)
        {
            CellView view = mainFrame.graph.getGraphLayoutCache().getMapping(cell, false);
            if (view != null)
            {
                JComponent c = (JComponent)view.getRendererComponent(mainFrame.graph, false, false, false);

                if (cell instanceof StateCell)
                {
                    c.setToolTipText ("State: Right mouse button to Change attributes of: "+
                                      ((StateCell)cell).getData().name);
                }
                else if (cell instanceof NameCell)
                {
                    c.setToolTipText ("Name Label: Right mouse button in free area to change name and comment");
                }
                else if (cell instanceof StartingTransitionCell)
                {
                    c.setToolTipText ("Starting Transition: Right mouse button to change attributes");
                }
                else if (cell instanceof TransitionCell)
                {
                    c.setToolTipText ("Transition: Right mouse button to Change attributes of: "+
                                      ((TransitionCell)cell).getData().name);
                }
            }
        }
    }
}
