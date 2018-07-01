package com.sourceforge.fsmbuilder;

import java.io.PrintStream;

/**
 * This class holds all data from the FSM
 */
final class FSMData
{
    /**
     * The FSM's name
     */
    String name;
    /**
     * Purpose of this FSM
     */
    String comment = null;
    /**
     * All transitions
     */
    TransitionData[] transitions;
    /**
     * All states
     */
    StateData[] states;

    /**
     * Debugging function that prints the FSM content
     * @param out Is the printstream where output goes
     */
    void print (PrintStream out)
    {
        out.println ("N:" + name);
        for (TransitionData tr : transitions)
        {
            tr.print (out);
        }
        for (StateData st : states)
        {
            st.print (out);
        }
    }

    /**
     * Validates the model
     *
     * @param ps Printstream where output is printed
     *           If ps is <b>null</b>, no output will be generated
     * @throws ValidationException if validation fails
     */
    void validate (PrintStream ps) throws ValidationException
    {
        if (ps != null)
        {
            print (ps);
        }
        // Is there a name?
        if (name == null)
        {
            throw new ValidationException ("Model has no name");
        }
        // There must be at least one state
        if (states.length < 2)
        {
            throw new ValidationException ("There must be at least two states");
        }
        // There must be at least one transition in the FSM
        if (transitions.length == 0)
        {
            throw new ValidationException ("There's no transition");
        }
        // All transitions must be connected and named
        // One state must have only one transition with the same name as source
        int starting = 0;
        for (TransitionData trd : transitions)
        {
            if (trd.name == null)
            {
                starting++;
                if (trd.from != null)
                {
                    throw new ValidationException ("The stating transition must have no source!");
                }
            }
            else
            {
                if (trd.from == null)
                {
                    throw new ValidationException ("Transition \"" + trd.name + "\" has no source");
                }
            }
            if (trd.to == null)
            {
                if (trd.name == null)
                {
                    throw new ValidationException ("The starting transition has no target");
                }
                else
                {
                    throw new ValidationException ("Transition \"" + trd.name + "\" has no target");
                }
            }
        }
        if (starting != 1)
        {
            throw new ValidationException ("There must be one and ONLY one starting transition");
        }
        for (TransitionData trd : transitions)
        {
            for (TransitionData trd2 : transitions)
            {
                if (trd.name == null || trd2.name == null)
                {
                    continue;
                }
                if (trd != trd2 && trd.name.equals (trd2.name) && trd2.from.equals (trd.from))
                {
                    throw new ValidationException ("State \"" + trd.from
                            + "\" must only have one source transition with the same name \"" + trd.name + "\""
                    );
                }
            }
        }
        // Check starting state, state names, uniqueness and connections
        for (StateData std : states)
        {
            for (StateData std2 : states)
            {
                if (std2 != std && std2.name.equals (std.name))
                {
                    throw new ValidationException (
                            "There are two or more states with the same name \"" + std.name + "\""
                    );
                }
            }
            int trg = 0;
            for (TransitionData trd : transitions)
            {
                //if (trd.name == null) // Don't count the starting transition
                //    continue;
                if (trd.to.equals (std.name))
                {
                    trg++;
                }
            }
            if (trg == 0)
            {
                throw new ValidationException ("\"" + std.name + "\" is unreachable");
            }
        }
    }
}
