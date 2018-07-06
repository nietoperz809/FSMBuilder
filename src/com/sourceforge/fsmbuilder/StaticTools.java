package com.sourceforge.fsmbuilder;

import javax.swing.*;
import java.awt.*;

/**
 * This class contains the miscellanous tools
 */
final class StaticTools
{
    /**
     * Warning box if model isn't empty
     * @param f Reference to parent component
     * @return  1 if user clicked OK
     */
    static int NotEmptyWarning(Component f)
    {
        Object[] options = {"OK", "I don't want that"};
        return JOptionPane.showOptionDialog(f,
                                            "The program will end now ...",
                                            "Warning",
                                            JOptionPane.DEFAULT_OPTION,
                                            JOptionPane.WARNING_MESSAGE, null, options, options[1]
        );
    }

    /**
     * General Error Box
     * @param f Reference to parent component
     * @param text Text in the box
     * @param title Box title
     */
    static void ErrorBox (Component f, String text, String title)
    {
        JOptionPane.showMessageDialog(f, text, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * General Information Box
     * @param f Reference to parent component
     * @param text Text in the box
     * @param title Box title
     */
    static void InfoBox (Component f, String text, String title)
    {
        JOptionPane.showMessageDialog(f, text, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * General question box
     * @param f Reference to parent component
     * @param text Text in the box
     * @param title Box title
     * @return 1 if user clicked OK
     */
    static int QuestionBox (Component f, String text, String title)
    {
        Object[] options = {"OK", "NO, please don't!"};
        return JOptionPane.showOptionDialog (f, text, title, JOptionPane.DEFAULT_OPTION,
                                             JOptionPane.WARNING_MESSAGE, null, options, options[1]);
    }
}
