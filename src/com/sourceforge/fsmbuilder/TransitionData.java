package com.sourceforge.fsmbuilder;

import java.io.Serializable;
import java.io.PrintStream;

/**
 * Holds data for one transition
 */
final class TransitionData implements Serializable
{
    /**
     * Keep stream compatible
     */
    private static final long serialVersionUID = -2281547586315509641L;
    /**
     * Name of that transition
     * If name == null then this is the starting transition
     */
    String name = null;
    /**
     * Name of <b>action</b> event
     */
    String action = null;
    /**
     * Source of transition
     */
    String from = null;
    /**
     * Target of that transition
     */
    String to = null;

    /**
     * Debugging function: Prints all that stuff^^
     * @param out Where the output goes
     */
    void print(PrintStream out)
    {
        out.printf ("T: name:%s, action:%s, from:%s, to:%s\n", name, action, from, to);
    }
}
