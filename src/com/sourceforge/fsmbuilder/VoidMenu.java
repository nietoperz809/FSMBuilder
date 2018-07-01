package com.sourceforge.fsmbuilder;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * This class is the popup menu for empty rooms
 */
final class VoidMenu extends InnerPopupMenu
{
    /**
     * Creates and displays the menu when user clicks on empty space on the work sheet
     * @param mainFrame Reference to main window that holds the worksheet
     * @param ev        Mouse event that causes the menu to be displayed
     */
    VoidMenu(final MainFrame mainFrame, final MouseEvent ev)
    {
        /**
         * Get reference to the one and only name cell
         */
        final NameCell nameCell = mainFrame.getNameCell();

        /**
         * new transition
         */
        add(new AbstractAction("New transition here")
        {
            public void actionPerformed(ActionEvent e)
            {
                TransitionCell edge = new TransitionCell(ev.getX(), ev.getY());
                mainFrame.graph.getGraphLayoutCache().insert(edge);
            }
        }
        );

        /**
         * new transition
         */
        add(new AbstractAction("New starting transition here")
        {
            public void actionPerformed(ActionEvent e)
            {
                StartingTransitionCell edge = new StartingTransitionCell(ev.getX(), ev.getY());
                mainFrame.graph.getGraphLayoutCache().insert(edge);
            }
        }
        );

        /**
         * new state
         */
        add(new AbstractAction("New state here")
        {
            public void actionPerformed(ActionEvent e)
            {
                StateCell cell = new StateCell(ev.getX(), ev.getY());
                mainFrame.graph.getGraphLayoutCache().insert(cell);
            }
        }
        );

        addSeparator();

        /**
         * Adds a new or modifies the current name cell
         */
        add(new TextMenuItem("Set Model name", nameCell == null ? "" : nameCell.getName())
        {
            public void actionPerformed(ActionEvent e)
            {
                // Set new name if already given
                if (nameCell != null)
                {
                    nameCell.setName(text.getText());
                    mainFrame.forceRepaint(nameCell);
                }
                else
                {
                    // Make new nameCell and show it
                    mainFrame.graph.getGraphLayoutCache().insert(new NameCell(text.getText()));
                }
            }
        }
        );

        /**
         * Let the user enter a comment line into the name cell
         */
        if (nameCell != null)
        {
            add(new TextMenuItem("Model comment", nameCell.getComment())
            {
                public void actionPerformed(ActionEvent e)
                {
                    nameCell.setComment(text.getText());
                    mainFrame.forceRepaint(nameCell);
                }
            }
            );
        }

        /**
         * Zoom in
         */
        if (mainFrame.graph.getScale() < 1)
        {
            add(new AbstractAction("Zoom in")
            {
                public void actionPerformed(ActionEvent e)
                {
                    mainFrame.graph.setScale(mainFrame.graph.getScale() * 1.2);
                }
            }
            );
        }

        /**
         * Zoom out
         */
        add(new AbstractAction("Zoom out")
        {
            public void actionPerformed(ActionEvent e)
            {
                mainFrame.graph.setScale(mainFrame.graph.getScale() / 1.2);
            }
        }
        );

        addSeparator();

        /**
         * Saves model to disk
         */
        add(new AbstractAction("Save Model")
        {
            public void actionPerformed(ActionEvent e)
            {
                if (mainFrame.isModelEmpty())
                {
                    StaticTools.ErrorBox(mainFrame, "The model is empty", "Nothing to do");
                }
                else
                {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setApproveButtonText("Save now!");
                    chooser.setFileFilter(new FileNameExtensionFilter("FSMBuilder files", "fsmb"));
                    if (chooser.showSaveDialog(mainFrame) == JFileChooser.APPROVE_OPTION)
                    {
                        try
                        {
                            mainFrame.saveModel(chooser.getSelectedFile().getAbsolutePath());
                        }
                        catch (Exception ex)
                        {
                            StaticTools.ErrorBox(mainFrame, ex.getMessage(), "Could not save");
                        }
                    }
                }
            }
        }
        );

        /**
         * Loads model from disk
         */
        add(new AbstractAction("Load Model")
        {
            public void actionPerformed(ActionEvent e)
            {
                if (!mainFrame.isModelEmpty())
                {
                    if (0 == StaticTools.QuestionBox(mainFrame, "This will overwrite your existing model. Click OK to continue", "Warning"))
                    {
                        mainFrame.eraseModel();
                    }
                    else
                    {
                        return;
                    }
                }

                JFileChooser chooser = new JFileChooser();
                chooser.setApproveButtonText("Get it!");
                chooser.setFileFilter(new FileNameExtensionFilter("FSMBuilder files", "fsmb"));
                if (chooser.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION)
                {
                    try
                    {
                        mainFrame.loadModel(chooser.getSelectedFile().getAbsolutePath());
                    }
                    catch (Exception ex)
                    {
                        StaticTools.ErrorBox(mainFrame, ex.getMessage(), "Could not load");
                    }
                }
            }
        }
        );

        addSeparator();

        /**
         * Validates the model
         */
        add(new AbstractAction("Validate Model")
        {
            public void actionPerformed(ActionEvent e)
            {
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(bs);
                PreviewDialog pv = new PreviewDialog(ev.getX(), ev.getY(), "Validation Results");
                try
                {
                    FSMData fsm = mainFrame.getFSMData();
                    fsm.validate(ps);
                }
                catch (ValidationException ve)
                {
                    ps.print("<h1 align=\"center\" color=\"#ff0000\">Validation failed!</h1>");
                    ps.print("<h3 color = \"#ffff00\">" + ve.getMessage() + "</h3");
                    pv.printStream(bs);
                    return;
                }
                ps.print("<h1 align=\"center\" color=\"#00ff00\">The model is valid!");
                ps.print("<h3 align=\"center\" color=\"#ffff00\">Well done!</h3");
                pv.printStream(bs);
            }
        }
        );

        /**
         * Makes C code from model
         */
        add(new AbstractAction("Generate C Code")
        {
            public void actionPerformed(ActionEvent e)
            {
                FSMData fsm = mainFrame.getFSMData();
                try
                {
                    fsm.validate(null);
                }
                catch (ValidationException ve)
                {
                    StaticTools.ErrorBox(mainFrame, "The model is not valid", "Sorry");
                    return;
                }
                CGSettingsDialog settingsDialog = CGSettingsDialog.create(ev.getPoint());
                CGOptions opt = settingsDialog.getOptions();
                if (opt.ok_clicked)
                {
                    try
                    {
                        new CCodeGenSwitchCase(fsm, opt);
                        if (!opt.show_results)
                        {
                            StaticTools.InfoBox(mainFrame, "Done!", "Code Generation");
                        }
                    }
                    catch (IOException ioe)
                    {
                        StaticTools.ErrorBox(mainFrame, ioe.getMessage(), "Can't create file(s)");
                    }
                }
            }
        }
        );

        addSeparator();

        /**
         * Clear the work sheet
         */
        if (!mainFrame.isModelEmpty())
        {
            add(new AbstractAction("Delete entire Model")
            {
                public void actionPerformed(ActionEvent e)
                {
                    if (StaticTools.NotEmptyWarning(mainFrame) == 0)
                    {
                        mainFrame.eraseModel();
                    }
                }
            }
            );
            addSeparator();
        }

        /**
         * Run other builder
         */
        add(new AbstractAction("New FSM Builder")
        {
            public void actionPerformed(ActionEvent e)
            {
                MainFrame.main();
            }
        }
        );

        /**
         * Run other builder
         */
        add(new AbstractAction("Toggle Monitor")
        {
            public void actionPerformed(ActionEvent e)
            {
                mainFrame.toggleMonitor();
            }
        }
        );

        /**
         * Displays the menu on mouse point
         */
        positionAndShow(mainFrame, ev);
    }
}
