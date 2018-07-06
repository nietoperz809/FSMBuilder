package com.sourceforge.fsmbuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;

/**
 * This is the superclass for C Code Generators
 */
public class CCodeGenBase
{
    /**
     * Stores data that describes the FSM
     */
    final FSMData fsm;
    /**
     * Code generator options
     */
    final CGOptions opt;
    /**
     * Used to build the source files
     */
    StringBuilder sb;
    /**
     * System dependent newline character(s)
     */
    final String newline = System.getProperty ("line.separator");
    /**
     * Number of tabs that are actually printed
     */
    int tablevel = 0;
    /**
     * Holds all unique transition names
     */
    final HashSet<String> transitionNames = new HashSet<String> ();
    /**
     * Holds all unique action names
     */
    final HashSet<String> actionNames = new HashSet<String> ();

    final HashSet<String> eventNames = new HashSet<String> ();

    /**
     * Name of the main header file
     */
    final String headerFileName;
    /**
     * Name of the FSM implementation file
     */
    final String mainFileName;
    /**
     * Name of the file that contains callback functions
     */
    final String callbackFileName;

    CCodeGenBase (FSMData fsmData, CGOptions cgOptions)
    {
        fsm = fsmData;
        mainFileName = "FSM_" + fsmData.name + ".c";
        opt = cgOptions;
        callbackFileName = "FSM_" + fsmData.name + "_callbacks.c";
        headerFileName = "FSM_" + fsmData.name + ".h";
    }

    /**
     * Makes state name as used in C code
     *
     * @param name the real name
     * @return converted name
     */
    String makeStateName (final String name)
    {
        return fsm.name + "_S_" + name;
    }

    /**
     * Writes generated file to disk
     *
     * @param filename Name of the generated file
     * @throws java.io.IOException If file cannot be created
     */
    void saveFile (String filename) throws IOException
    {
        try (PrintWriter out = new PrintWriter(opt.path + filename))
        {
            out.println(sb.toString ());
        }
    }

    /**
     * Makes transition name as used in C code
     *
     * @param name the real name
     * @return converted name
     */
    String makeTransName (final String name)
    {
        return fsm.name + "_T_" + name;
    }

    /**
     * Makes exit function name as used in C code
     *
     * @param name the real name
     * @return converted name
     */
    String makeEventName (final String name)
    {
        return "Event_" + fsm.name + "_" + name;
    }

    /**
     * Makes action name as used in C code
     *
     * @param name the real name
     * @return converted name
     */
    String makeActionName (final String name)
    {
        return "Action_" + fsm.name + "_" + name;
    }

    /**
     * Inserts debug output into code
     *
     * @param s Content of debug output
     * @return A code line that is the debug output statement
     */
    String makeDebugLine (final String s)
    {
        return "puts (\"" + s + "\");";
    }

    /**
     * Makes the footer line (last line of code file)
     *
     * @param s main argument in footer line
     * @return The generated footer line
     */
    String makeFooter (final String s)
    {
        return "/* End of FSM: " +
                fsm.name + " " +
                s +
                "  */" +
                newline +
                newline;
    }

    /**
     * Outputs some spaces
     */
    private void outputTabs ()
    {
        for (int s = 0; s < tablevel; s++)
        {
            /*
      One tab - some spaces here
     */ /**
         * One tab - some spaces here
         */String tab = "    ";
            sb.append (tab);
        }
    }

    /**
     * Outputs new line characters
     *
     * @param num Number of NL to print
     */
    void outputNewLines (int num)
    {
        for (int s = 0; s < num; s++)
        {
            sb.append (newline);
        }
    }

    /**
     * Outputs a complete line
     *
     * @param s        The line
     * @param newlines Number of newlines after the line was printed
     */
    void line (String s, int newlines)
    {
        outputTabs ();
        sb.append (s);
        outputNewLines (newlines);
    }

    /**
     * Removes the last characters from the Stringbuilder
     *
     * @param num Number of chars to remove
     */
    void removeLast (int num)
    {
        int len = sb.length ();
        sb.delete (len - num, len);
    }

    /**
     * Initializes code generation for a single file
     */
    void startCode ()
    {
        tablevel = 0;
        sb = new StringBuilder ();
    }

    /**
     * Finalizes code generation for a single file
     */
    void endCode ()
    {
        sb = null;
    }

    boolean isStartingState (String name)
    {
        for (TransitionData transition : fsm.transitions)
        {
            if (transition.name == null && transition.to.equals (name))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds next state for transition
     *
     * @param stateName      Current state
     * @param transitionName Name of transition
     * @return The name of the next state or <b>null</b> if there is none
     */
    String findStateChange (String stateName, String transitionName)
    {
        if (transitionName == null)
        {
            return null;
        }
        for (TransitionData transition : fsm.transitions)
        {
            if (transition.name == null)
            {
                continue;
            }
            if (transition.from.equals (stateName) && transitionName.equals (transition.name))
            {
                return transition.to;
            }
        }
        return null;
    }

    /**
     * Finds action when state changes on a given transition
     *
     * @param stateFrom  Current State
     * @param stateTo    Next State
     * @param transition Transition name
     * @return found action or <b>null</b> if not found
     */
    String findAction (String stateFrom, String stateTo, String transition)
    {
        for (TransitionData trans : fsm.transitions)
        {
            // Skip starting transition
            if (trans.name == null)
            {
                continue;
            }
            if (trans.name.equals (transition) && trans.from.equals (stateFrom) && trans.to.equals (stateTo))
            {
                if (trans.action != null)
                {
                    return trans.action;
                }
                else
                {
                    break;
                }
            }
        }
        return null;
    }

    /**
     * Finds state data object given a state name
     *
     * @param name The state name
     * @return The state data object or <b>null</b> if not found
     */
    StateData stateFromName (String name)
    {
        for (StateData state : fsm.states)
        {
            if (state.name.equals (name))
            {
                return state;
            }
        }
        return null;
    }
}
