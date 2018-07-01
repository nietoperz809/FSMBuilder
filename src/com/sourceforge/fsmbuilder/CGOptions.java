package com.sourceforge.fsmbuilder;

import java.io.Serializable;

/**
 * Code generator options data class
 */
class CGOptions implements Serializable
{
    /**
     * Should we create debug outputs?
     */
    boolean debug_outputs = true;
    /**
     * Should we do code generation?
     */
    boolean ok_clicked = false;
    /**
     * Shall the output go to the screen?
     */
    boolean show_results = true;
    /**
     * Should a callbacks file be created?
     */
    boolean generate_callbacks = true;
    /**
     * This is the path where the files are created
     */
    String path;
}
