package com.sourceforge.fsmbuilder;

import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.GraphConstants;

import java.awt.geom.Point2D;
import java.awt.*;
import java.util.ArrayList;

/**
 * This is a transition
 */
class TransitionCell extends DefaultEdge
{
    private static final long serialVersionUID = 8624832849796970071L;

    private final TransitionData transitionData = new TransitionData();

    TransitionCell (int x, int y)
    {
        super();
        makeVisualStyle (x,y);
    }

    void makeVisualStyle (int x, int y)
    {
        GraphConstants.setEditable(getAttributes(), false);
        GraphConstants.setLineStyle(getAttributes(), GraphConstants.STYLE_BEZIER);
        GraphConstants.setLineEnd(getAttributes(), GraphConstants.ARROW_CLASSIC);
        GraphConstants.setEndFill(getAttributes(), true);
        GraphConstants.setOpaque(getAttributes(), true);
        GraphConstants.setBackground(getAttributes(), Color.YELLOW);
        ArrayList<Point2D.Double> list = new ArrayList<Point2D.Double>();
        list.add(new Point2D.Double(x-50, y));
        list.add(new Point2D.Double(x+100,y));
        GraphConstants.setPoints(getAttributes(), list);
        transitionData.name = "New_Transition";
        setUserObject (transitionData.name);
    }

    TransitionData getData()
    {
        return transitionData;
    }

    void setAction (String act)
    {
        Object[] o = new Object[1];
        if (act.equals (""))
        {
            transitionData.action = null;
            o[0] = "";
        }
        else
        {
            transitionData.action = StringChanger.makeCCompatible (act);
            o[0] = transitionData.action;
        }
        Point2D.Double[] p = {new Point2D.Double(GraphConstants.PERMILLE*7/8, -10),};
        GraphConstants.setExtraLabels(getAttributes(), o);
        GraphConstants.setExtraLabelPositions(getAttributes(), p);
    }

    void setName (String n)
    {
        transitionData.name = StringChanger.makeCCompatible (n);
        setUserObject (transitionData.name);
    }
}
