package com.sourceforge.fsmbuilder;

import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.GraphConstants;

import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * This is a transition
 */
class StartingTransitionCell extends TransitionCell
{
    static final long serialVersionUID = 3710206212184678632L;

    StartingTransitionCell (int x, int y)
    {
        super (x,y);
        makeVisualStyle (x,y);
    }

    void makeVisualStyle (int x, int y)
    {
        GraphConstants.setEditable(getAttributes(), false);
        GraphConstants.setLineStyle(getAttributes(), GraphConstants.STYLE_BEZIER);
        GraphConstants.setLineEnd(getAttributes(), GraphConstants.ARROW_CLASSIC);
        GraphConstants.setLineBegin(getAttributes(), GraphConstants.ARROW_CIRCLE);
        GraphConstants.setEndFill(getAttributes(), true);
        GraphConstants.setBeginFill (getAttributes(), true);
        GraphConstants.setOpaque(getAttributes(), true);
        ArrayList<Point2D.Double> list = new ArrayList<Point2D.Double>();
        list.add(new Point2D.Double(x-50, y));
        list.add(new Point2D.Double(x+100,y));
        GraphConstants.setPoints(getAttributes(), list);
    }
}
