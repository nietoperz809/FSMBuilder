package com.sourceforge.fsmbuilder;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.geom.*;
import java.util.List;

/**
 * Super class for popup menu.
 * Keeps the menu always between the bounds of the main frame
 */
class InnerPopupMenu extends JPopupMenu
{
    /**
     * Adds menu items common to all menus
     * @param mainFrame Ref. to the main frame
     * @param cell The cell under the mouse
     */
    void addMenuTail (final MainFrame mainFrame, final DefaultGraphCell cell)
    {
        /**
         * Rotate transitions and starting transition
         * but only if not connected to a state
         */
        if (cell instanceof TransitionCell)
        {
            final TransitionCell trans = (TransitionCell)cell;
            if (trans.getSource() == null && trans.getTarget() == null)
            {
                add(new AbstractAction("Rotate")
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        List<Point2D.Double> pointList = GraphConstants.getPoints(trans.getAttributes());
                        Path2D path = new GeneralPath();

                        // Make Path from points
                        path.moveTo(pointList.get(0).x, pointList.get(0).y);
                        for (int s = 1; s < pointList.size(); s++)
                        {
                            path.lineTo(pointList.get(s).x, pointList.get(s).y);
                        }

                        // Make rotation anchor
                        double centerX = (pointList.get(0).x + pointList.get(pointList.size() - 1).x) / 2;
                        double centerY = (pointList.get(0).y + pointList.get(pointList.size() - 1).y) / 2;

                        // Get rotated point iterator
                        AffineTransform af = AffineTransform.getRotateInstance(Math.toRadians(90), centerX, centerY);
                        PathIterator pathIterator = path.getPathIterator(af);

                        // Make new points
                        pointList.clear();
                        double[] arr = new double[6];
                        while (!pathIterator.isDone())
                        {
                            pathIterator.currentSegment(arr);
                            pointList.add(new Point2D.Double(arr[0], arr[1]));
                            pathIterator.next();
                        }

                        // and repaint
                        mainFrame.forceRepaint(trans);
                    }
                });
            }
        }

        addSeparator();

        /**
         * Clone that transition
         */
        add(new AbstractAction("Clone")
        {
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    mainFrame.cloneObject (cell);
                }
                catch (Exception e1)
                {
                    StaticTools.ErrorBox(mainFrame, "Could not clone", e1.toString());
                }
            }
        });

        addSeparator();

        /**
         * Remove that transition
         */
        add(new AbstractAction("Remove")
        {
            public void actionPerformed(ActionEvent e)
            {
                mainFrame.eraseObject(cell);
            }
        });
    }

    /**
     * Positions an shows the menu
     * @param mainFrame Ref. to the main frame
     * @param ev Mouse click that causes menu to open
     */
    void positionAndShow (final MainFrame mainFrame, final MouseEvent ev)
    {
        /**
         * Displays the menu on mouse point
         */
        show (mainFrame.getComponent(0), ev.getX(), ev.getY());

        int x = ev.getX();
        int y = ev.getY();
        double framex = mainFrame.getComponent(0).getBounds().width;
        double menux = getBounds().width + mainFrame.getComponent(0).getLocation().x + ev.getX();
        double diffx = menux - framex;
        // need X adjustment
        if (diffx > 0.0)
        {
            x -= (int)diffx;
        }

        double framey = mainFrame.getComponent(0).getBounds().height + 20;
        double menuy = getBounds().height + mainFrame.getComponent(0).getLocation().y + ev.getY();
        double diffy = menuy - framey;
        // need Y adjustment
        if (diffy > 0.0)
        {
            y -= (int)diffy;
        }

        show (mainFrame.getComponent(0), x, y);
    }
}
