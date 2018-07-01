package com.sourceforge.fsmbuilder;

import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;

/**
 * This is the superclass for C Code Generators
 */
public class CCodeGenBase
{
    /**
     * Stores data that describes the FSM
     */
    protected final FSMData fsm;
    /**
     * Code generator options
     */
    protected final CGOptions opt;
    /**
     * Used to build the source files
     */
    protected StringBuilder sb;
    /**
     * System dependent newline character(s)
     */
    protected final String newline = System.getProperty ("line.separator");
    /**
     * One tab - some spaces here
     */
    private final String tab = "    ";
    /**
     * Number of tabs that are actually printed
     */
    protected int tablevel = 0;
    /**
     * Holds all unique transition names
     */
    protected final HashSet<String> transitionNames = new HashSet<String> ();
    /**
     * Holds all unique action names
     */
    protected final HashSet<String> actionNames = new HashSet<String> ();

    protected final HashSet<String> eventNames = new HashSet<String> ();

    /**
     * Name of the main header file
     */
    protected final String headerFileName;
    /**
     * Name of the FSM implementation file
     */
    protected final String mainFileName;
    /**
     * Name of the file that contains callback functions
     */
    protected final String callbackFileName;

    public CCodeGenBase (FSMData fsmData, CGOptions cgOptions)
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
    protected String makeStateName (final String name)
    {
        return fsm.name + "_S_" + name;
    }

    /**
     * Writes generated file to disk
     *
     * @param filename Name of the generated file
     * @throws java.io.IOException If file cannot be created
     */
    protected void saveFile (String filename) throws IOException
    {
        File f = new File (opt.path + filename);
        FileWriter fw = new FileWriter (f);
        fw.write (sb.toString ());
        fw.flush ();
        fw.close ();
    }

    /**
     * Makes transition name as used in C code
     *
     * @param name the real name
     * @return converted name
     */
    protected String makeTransName (final String name)
    {
        return fsm.name + "_T_" + name;
    }

    /**
     * Makes exit function name as used in C code
     *
     * @param name the real name
     * @return converted name
     */
    protected String makeEventName (final String name)
    {
        return "Event_" + fsm.name + "_" + name;
    }

    /**
     * Makes action name as used in C code
     *
     * @param name the real name
     * @return converted name
     */
    protected String makeActionName (final String name)
    {
        return "Action_" + fsm.name + "_" + name;
    }

    /**
     * Inserts debug output into code
     *
     * @param s Content of debug output
     * @return A code line that is the debug output statement
     */
    protected String makeDebugLine (final String s)
    {
        return "puts (\"" + s + "\");";
    }

    /**
     * Makes the footer line (last line of code file)
     *
     * @param s main argument in footer line
     * @return The generated footer line
     */
    protected String makeFooter (final String s)
    {
        return new StringBuilder ()
                .append ("/* End of FSM: ")
                .append (fsm.name).append (" ")
                .append (s)
                .append ("  */")
                .append (newline)
                .append (newline).toString ();
    }

    /**
     * Outputs some spaces
     */
    private void outputTabs ()
    {
        for (int s = 0; s < tablevel; s++)
        {
            sb.append (tab);
        }
    }

    /**
     * Outputs new line characters
     *
     * @param num Number of NL to print
     */
    protected void outputNewLines (int num)
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
    protected void line (String s, int newlines)
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
    protected void removeLast (int num)
    {
        int len = sb.length ();
        sb.delete (len - num, len);
    }

    /**
     * Initializes code generation for a single file
     */
    protected void startCode ()
    {
        tablevel = 0;
        sb = new StringBuilder ();
    }

    /**
     * Finalizes code generation for a single file
     */
    protected void endCode ()
    {
        sb = null;
    }

    protected boolean isStartingState (String name)
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
    protected String findStateChange (String stateName, String transitionName)
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
    protected String findAction (String stateFrom, String stateTo, String transition)
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
    protected StateData stateFromName (String name)
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
