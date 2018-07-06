package com.sourceforge.fsmbuilder;

import java.io.IOException;

/**
 * This is the ANSI-C Code Generator
 */
final class CCodeGenSwitchCase extends CCodeGenBase
{
    /**
     * Constructor: Generates the code
     *
     * @param the_fsm Data that describes the FSM
     * @param options Code generator options
     * @throws java.io.IOException If file(s) cannot be created
     */
    public CCodeGenSwitchCase (FSMData the_fsm, CGOptions options) throws IOException
    {
        super (the_fsm, options);

        // Make sets of unique transition and action  names
        for (TransitionData transition : the_fsm.transitions)
        {
            // Add all transition that are not the starting transition
            if (transition.name != null)
            {
                transitionNames.add (transition.name);
            }
            // Find all actions
            if (transition.action != null)
            {
                actionNames.add (transition.action);
            }
        }

        // Make set of unique entry, exit and run functions
        for (StateData state : the_fsm.states)
        {
            if (state.entry != null)
            {
                eventNames.add (state.entry);
            }
            if (state.exit != null)
            {
                eventNames.add (state.exit);
            }
            if (state.run != null)
            {
                eventNames.add (state.run);
            }
        }

        // Make the files
        generateH ();
        generateC ();
        if (options.generate_callbacks)
        {
            generateCallbacks ();
        }
    }

    /**
     * Generates the header file
     * @throws java.io.IOException If file cannot be created
     */
    private void generateH () throws IOException
    {
        startCode ();

        // Header
        line ("/* " + headerFileName + " */", 1);
        line ("/* This file contains all definitions for the FSM: " + fsm.name + " */", 1);
        line ("/* Machine generated file - BE CAREFUL WITH MODIFICATIONS - */", 1);
        if (fsm.comment != null)
        {
            line ("/* " + fsm.comment + " */", 2);
        }
        else
        {
            outputNewLines (1);
        }

        // The states
        line ("/* All the states */", 1);
        line ("typedef enum " + fsm.name + "_State", 1);
        line ("{", 1);
        tablevel = 1;
        line ("INVALID_STATE = 0, /* This state is undefined */", 1);
        for (StateData state : fsm.states)
        {
            line (makeStateName (state.name) + ",", 1);
        }
        removeLast (1 + newline.length ());
        outputNewLines (1);
        tablevel = 0;
        line ("} " + fsm.name + "_State;", 2);

        // The transitions
        line ("/* All the transitions */", 1);
        line ("typedef enum " + fsm.name + "_Transition", 1);
        line ("{", 1);
        tablevel = 1;
        line ("INVALID_TRANSITION = 0, /* Use this event if there is none */", 1);
        for (String transitionName : transitionNames)
        {
            line (makeTransName (transitionName) + ",", 1);
        }
        removeLast (1 + newline.length ());
        outputNewLines (1);
        tablevel = 0;
        line ("} " + fsm.name + "_Transition;", 2);

        // Action function prototypes
        if (!actionNames.isEmpty ())
        {
            line ("/* Transition actions */", 1);
            for (String actionName : actionNames)
            {
                line ("void " + makeActionName (actionName) + "(void);", 1);
            }
            outputNewLines (1);
        }

        // Event function prototypes
        if (!eventNames.isEmpty())
        {
            line ("/* Exit function prototypes */", 1);
            for (String name : eventNames)
            {
                line ("void " + makeEventName (name) + " (void);", 1);
            }
            outputNewLines (1);
        }

        // FSM API functions
        line ("/* Call this function before the FSM can be used */", 1);
        line ("void FSM_Init_" + fsm.name + " (void);", 2);
        line ("/* Call this function periodically to run the FSM */", 1);
        line ("/* If there's currently no event, use INVALID_TRANSITION */", 1);
        line ("void FSM_Tick_" + fsm.name + " (" + fsm.name + "_Transition);", 2);

        // Footer line
        line (makeFooter ("definitions"), 1);

        if (opt.show_results)
        {
            PreviewDialog dlg = new PreviewDialog (10, 10, headerFileName);
            dlg.printCodeAsHTML (sb.toString ());
        }

        saveFile (headerFileName);

        endCode ();
    }

    /**
     * Generates the callbacks file
     * @throws java.io.IOException If file cannot be created
     */
    private void generateCallbacks () throws IOException
    {
        startCode ();

        // Header
        line ("/* " + callbackFileName + " */", 1);
        line ("/* This file contains callback functions for the FSM: " + fsm.name + " */", 1);
        line ("/* Machine generated file - BE CAREFUL WITH MODIFICATIONS - */", 1);
        if (fsm.comment != null)
        {
            line ("/* " + fsm.comment + " */", 2);
        }
        else
        {
            outputNewLines (1);
        }

        // Include our generated header
        line ("#include \"" + headerFileName + "\"", 1);

        if (opt.debug_outputs)
        {
            line ("#include <stdio.h>", 1);
        }
        outputNewLines (1);

        if (!eventNames.isEmpty())
        {
            line ("/* Entry Functions */", 1);
            for (String name : eventNames)
            {
                line ("void " + makeEventName (name) + " (void)", 1);
                line ("{", 1);
                if (opt.debug_outputs)
                {
                    tablevel++;
                    line (makeDebugLine ("+ " + name), 1);
                    tablevel--;
                }
                line ("}", 2);
            }
        }

        for (String actionName : actionNames)
        {
            line ("/* Performing action: " + actionName + " */", 1);
            line ("void " + makeActionName (actionName) + "(void)", 1);
            line ("{", 1);
            if (opt.debug_outputs)
            {
                tablevel++;
                line (makeDebugLine ("ACTION: " + actionName), 1);
                tablevel--;
            }
            line ("}", 2);
        }

        // Footer line
        line (makeFooter ("callback functions"), 1);

        if (opt.show_results)
        {
            PreviewDialog dlg = new PreviewDialog (40, 40, callbackFileName);
            dlg.printCodeAsHTML (sb.toString ());
        }

        saveFile (callbackFileName);

        endCode ();
    }

    /**
     * Generates the main C file
     * @throws java.io.IOException If file cannot be created
     */
    private void generateC () throws IOException
    {
        startCode ();

        // Header
        line ("/* " + mainFileName + " */", 1);
        line ("/* This file contains implementation of the FSM: " + fsm.name + " */", 1);
        line ("/* Machine generated file - BE CAREFUL WITH MODIFICATIONS - */", 1);
        if (fsm.comment != null)
        {
            line ("/* " + fsm.comment + " */", 2);
        }
        else
        {
            outputNewLines (1);
        }

        // Include our generated header
        line ("#include \"" + headerFileName + "\"", 2);

        // State variable
        line ("/* Current State */", 1);
        line ("static " + fsm.name + "_State theState;", 2);

        // Init function
        line ("/* This function must be called first, before the FSM runs */", 1);
        line ("void FSM_Init_" + fsm.name + " (void)", 1);
        line ("{", 1);
        // Set starting state and call entry function
        tablevel = 1;
        for (StateData state : fsm.states)
        {
            if (isStartingState (state.name))
            {
                line ("theState = " + makeStateName (state.name) + ";", 1);
                if (state.entry != null)
                {
                    line (makeEventName (state.entry) + "();", 1);
                }
                break;
            }
        }
        // Call starting action
        for (TransitionData transition : fsm.transitions)
        {
            if (transition.name == null && transition.action != null)
            {
                line (makeActionName (transition.action) + "();", 1);
                break;
            }
        }
        tablevel = 0;
        line ("}", 2);

        // FSM main function
        line ("/* This is the FSM main function */", 1);
        line ("void FSM_Tick_" + fsm.name + " (" + fsm.name + "_Transition trans)", 1);
        line ("{", 1);
        tablevel++;
        line ("switch (theState)", 1);
        line ("{", 1);
        tablevel++;
        for (StateData state : fsm.states)
        {
            line ("case " + makeStateName (state.name) + ":", 1);
            line ("{", 1);
            tablevel++;
            if (state.run != null)
            {
                line (makeEventName (state.run) + "();", 1);
            }
            line ("switch (trans)", 1);
            line ("{", 1);
            tablevel++;
            for (String transitionName : transitionNames)
            {
                line ("case " + makeTransName (transitionName) + ":", 1);
                line ("{", 1);
                String next = findStateChange (state.name, transitionName);
                if (next != null)
                {
                    tablevel++;
                    if (!next.equals (state.name)) // Skip redundant code on self loops
                    {
                        line ("theState = " + makeStateName (next) + ";", 1);
                        if (state.exit != null)
                        {
                            line (makeEventName (state.exit) + "();", 1);
                        }
                        if (stateFromName (next).entry != null)
                        {
                            line (makeEventName (state.entry) + "();", 1);
                        }
                    }
                    String action = findAction (state.name, next, transitionName);
                    if (action != null)
                    {
                        line (makeActionName (action) + "();", 1);
                    }
                    tablevel--;
                }
                line ("}", 1);
                line ("break; /* End of case " + makeTransName (transitionName) + " */", 2);
            }
            removeLast (newline.length ());
            tablevel--;
            line ("}", 1);
            tablevel--;
            line ("}", 1);
            line ("break; /* End of case " + makeStateName (state.name) + " */", 2);
        }
        removeLast (newline.length ());
        tablevel--;
        line ("}", 1);
        tablevel--;
        line ("}", 2);

        // Footer line
        line (makeFooter ("implementation"), 1);

        if (opt.show_results)
        {
            PreviewDialog dlg = new PreviewDialog (30, 30, mainFileName);
            dlg.printCodeAsHTML (sb.toString ());
        }

        saveFile (mainFileName);

        endCode ();
    }
}
