package com.sourceforge.fsmbuilder;

import java.io.Serializable;
import java.io.PrintStream;

/**
 * This is the state data class
 */
class StateData implements Serializable
{
    /**
     * Keep stream compatible
     */
    static final long serialVersionUID = -7896489241244448256L;
    /**
     * Does this state has an entry function?
     */
    String entry = null;
    /**
     * Does this state has an exit function?
     */
    String exit = null;
    /**
     * Does this state has a run function?
     */
    String run = null;
    /**
     * The state's name
     */
    String name = "New_State";

    /**
     * Prints state data
     * @param out Printstream where output goes
     */
    void print (PrintStream out)
    {
        out.printf("S: name:%s, entry:%s, exit:%s, run:%b\n", name, entry, exit, run);
    }
}
