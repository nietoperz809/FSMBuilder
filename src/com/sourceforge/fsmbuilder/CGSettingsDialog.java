package com.sourceforge.fsmbuilder;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;

/**
 * This class implements the Code generator settings DLG
 */
public class CGSettingsDialog extends JDialog
{
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JCheckBox CheckBoxPopulate;
    private JCheckBox CheckBoxShowResults;
    private JCheckBox CheckBoxCallbacks;
    private JTextField textField1;
    private JButton pButton;

    /**
     * This is the options data object
     */
    private CGOptions cgOptions = new CGOptions();

    /**
     * Get the options data object
     * @return ^^
     */
    public CGOptions getOptions()
    {
        return cgOptions;
    }

    /**
     * Creates a dialog at specified point
     * @param pt Position of DLG
     * @return The new DLG object
     */
    static CGSettingsDialog create(Point pt)
    {
        CGSettingsDialog settingsDialog = new CGSettingsDialog();
        settingsDialog.load();
        settingsDialog.setTitle("Please enter CG options");
        settingsDialog.setBounds(0, 0, 300, 300);
        settingsDialog.setLocation(pt);
        settingsDialog.setResizable(false);
        settingsDialog.setVisible(true);
        return settingsDialog;
    }

    /**
     * Loads existing options
     */
    private void load()
    {
        try
        {
            InputStream fis = new FileInputStream(".ccgoptions");
            ObjectInputStream o = new ObjectInputStream(fis);
            cgOptions = (CGOptions) o.readObject();
            fis.close();

            CheckBoxPopulate.setSelected(cgOptions.debug_outputs);
            CheckBoxShowResults.setSelected(cgOptions.show_results);
            CheckBoxCallbacks.setSelected(cgOptions.generate_callbacks);
            textField1.setText(cgOptions.path);
        }
        catch (Exception ex)
        {
            // do nuthin
        }
    }

    /**
     * Saves options to disk
     */
    private void save()
    {
        try
        {
            OutputStream fos = new FileOutputStream(".ccgoptions");
            ObjectOutputStream o = new ObjectOutputStream(fos);
            o.writeObject(cgOptions);
            fos.close();
        }
        catch (Exception ex)
        {
            // do nuthin
        }
    }

    private CGSettingsDialog()
    {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onOK();
            }
        }
        );

        buttonCancel.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onCancel();
            }
        }
        );

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                onCancel();
            }
        }
        );

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        );
        CheckBoxPopulate.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                cgOptions.debug_outputs = CheckBoxPopulate.isSelected();
            }
        }
        );
        CheckBoxShowResults.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                cgOptions.show_results = CheckBoxShowResults.isSelected();
            }
        }
        );
        CheckBoxCallbacks.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                cgOptions.generate_callbacks = CheckBoxCallbacks.isSelected();
            }
        }
        );
        pButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (chooser.showDialog(CGSettingsDialog.this, "Use this directory") == JFileChooser.APPROVE_OPTION)
                {
                    String path = chooser.getSelectedFile().getAbsolutePath() + System.getProperty("file.separator");
                    textField1.setText(path);
                    cgOptions.path = path;
                }
            }
        }
        );
        textField1.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("textfield1");
            }
        }
        );
    }

    private void onOK()
    {
        // Skip if no path selected
        if (cgOptions.path == null)
        {
            StaticTools.ErrorBox(this, "You must select an output directory", "Sorry");
            return;
        }
        save();
        cgOptions.ok_clicked = true;
        dispose();
    }

    private void onCancel()
    {
// add your code here if necessary
        dispose();
    }

    private void createUIComponents()
    {
        // TODO: place custom component creation code here
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     * @noinspection ALL
     */
    private void $$$setupUI$$$()
    {
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(5, 5));
        contentPane.setMinimumSize(new Dimension(40, 65));
        contentPane.setPreferredSize(new Dimension(240, 200));
        contentPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel1.setMinimumSize(new Dimension(10, 20));
        panel1.setPreferredSize(new Dimension(10, 20));
        contentPane.add(panel1, BorderLayout.CENTER);
        CheckBoxShowResults = new JCheckBox();
        CheckBoxShowResults.setHorizontalAlignment(2);
        CheckBoxShowResults.setHorizontalTextPosition(2);
        CheckBoxShowResults.setMaximumSize(new Dimension(10, 20));
        CheckBoxShowResults.setMinimumSize(new Dimension(10, 20));
        CheckBoxShowResults.setPreferredSize(new Dimension(180, 20));
        CheckBoxShowResults.setSelected(true);
        CheckBoxShowResults.setText("Show results on screen");
        panel1.add(CheckBoxShowResults);
        CheckBoxCallbacks = new JCheckBox();
        CheckBoxCallbacks.setHorizontalAlignment(2);
        CheckBoxCallbacks.setHorizontalTextPosition(2);
        CheckBoxCallbacks.setMaximumSize(new Dimension(10, 20));
        CheckBoxCallbacks.setMinimumSize(new Dimension(10, 20));
        CheckBoxCallbacks.setPreferredSize(new Dimension(180, 20));
        CheckBoxCallbacks.setSelected(true);
        CheckBoxCallbacks.setText("Generate Callbacks");
        panel1.add(CheckBoxCallbacks);
        CheckBoxPopulate = new JCheckBox();
        CheckBoxPopulate.setHorizontalAlignment(2);
        CheckBoxPopulate.setHorizontalTextPosition(2);
        CheckBoxPopulate.setMaximumSize(new Dimension(10, 20));
        CheckBoxPopulate.setMinimumSize(new Dimension(10, 20));
        CheckBoxPopulate.setPreferredSize(new Dimension(180, 20));
        CheckBoxPopulate.setSelected(true);
        CheckBoxPopulate.setText("Insert debug outputs");
        panel1.add(CheckBoxPopulate);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout(0, 0));
        panel2.setMinimumSize(new Dimension(0, 0));
        panel2.setOpaque(false);
        panel2.setPreferredSize(new Dimension(230, 20));
        panel1.add(panel2);
        final JLabel label1 = new JLabel();
        label1.setHorizontalAlignment(2);
        label1.setPreferredSize(new Dimension(40, 14));
        label1.setRequestFocusEnabled(false);
        label1.setText("OutDir:");
        panel2.add(label1, BorderLayout.WEST);
        textField1 = new JTextField();
        textField1.setFocusCycleRoot(true);
        textField1.setFocusTraversalPolicyProvider(true);
        textField1.setMargin(new Insets(0, 0, 0, 0));
        textField1.setPreferredSize(new Dimension(30, 14));
        textField1.setRequestFocusEnabled(true);
        panel2.add(textField1, BorderLayout.CENTER);
        pButton = new JButton();
        pButton.setHorizontalAlignment(0);
        pButton.setLabel("<");
        pButton.setMargin(new Insets(0, 0, 0, 0));
        pButton.setMaximumSize(new Dimension(60, 23));
        pButton.setPreferredSize(new Dimension(40, 14));
        pButton.setRequestFocusEnabled(false);
        pButton.setText("<");
        panel2.add(pButton, BorderLayout.EAST);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        panel3.setFocusCycleRoot(false);
        panel3.setMinimumSize(new Dimension(40, 23));
        panel3.setOpaque(true);
        panel3.setPreferredSize(new Dimension(10, 20));
        contentPane.add(panel3, BorderLayout.SOUTH);
        buttonOK = new JButton();
        buttonOK.setMaximumSize(new Dimension(0, 0));
        buttonOK.setMinimumSize(new Dimension(0, 0));
        buttonOK.setPreferredSize(new Dimension(10, 20));
        buttonOK.setText("OK");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 10.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(buttonOK, gbc);
        buttonCancel = new JButton();
        buttonCancel.setPreferredSize(new Dimension(10, 20));
        buttonCancel.setText("Cancel");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 10.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(buttonCancel, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$()
    {
        return contentPane;
    }
}
