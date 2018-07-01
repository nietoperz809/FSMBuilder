package com.sourceforge.fsmbuilder;

/**
 * Used to make Strings C compatible 
 */
final class StringChanger
{
    /**
     * Manipulates Strings so that they can be used as C-language identifiers
     * @param in Input String
     * @return Output String
     */
    static String makeCCompatible (String in)
    {
        if (in.equals (""))
            return "noname";
        //in = in.trim(); // Remove WS at beginning and end
        in = in.replaceAll ("\\s", "_");  // Change white spaces
        in = in.replaceAll ("\\W", "_");  // Change all special chars
        return in;
    }
}
