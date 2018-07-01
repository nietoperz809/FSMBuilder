package com.sourceforge.fsmbuilder;

import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.io.OutputStream;

/**
 * This is the preview dialog for information and source
 */
public class PreviewDialog extends JDialog
{
    private JPanel contentPane;
    private JButton button1;
    private JTextPane textArea1;

    /**
     * Constructs that dialog at point x/y with the given title
     * @param x     Point X
     * @param y     Point Y
     * @param title Title
     */
    public PreviewDialog(int x, int y, String title)
    {
        setTitle(title);
        setContentPane(contentPane);
        getRootPane().setDefaultButton(button1);
        textArea1.setEditorKit(new HTMLEditorKit());
        button1.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                PreviewDialog.this.dispose();
            }
        }
        );
        pack();
        setLocation(x, y);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Prints an OutputStream into the TextField
     * @param ps Filled OutputStream object
     */
    public void printStream(OutputStream ps)
    {
        String s = ps.toString();
        s = s.replace("\n", "<br>");
        s = s.replace("\r", "");
        textArea1.setText("<p color = \"#ffffff\">" + s + "</p>");
    }

    /**
     * Prints source code into TextField and does HTML formatting
     * @param s String that contains the code
     */
    public void printCodeAsHTML(String s)
    {
        s = s.replace("<", "&lt;");
        s = s.replace(">", "&gt;");
        s = s.replace(System.getProperty("line.separator"), "<br>");
        s = s.replace(" ", "&nbsp;");
        s = s.replace("/*", "<font color = \"#A8A3B6\">/*");
        s = s.replace("*/", "*/</font>");
        s = "<p color = \"#57FF79\">" + s + "</p>";
        textArea1.setText(s);
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
        contentPane.setLayout(new BorderLayout(0, 0));
        contentPane.setPreferredSize(new Dimension(500, 400));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        panel1.setPreferredSize(new Dimension(67, 23));
        contentPane.add(panel1, BorderLayout.SOUTH);
        button1 = new JButton();
        button1.setOpaque(true);
        button1.setPreferredSize(new Dimension(67, 23));
        button1.setText("Dismiss");
        panel1.add(button1, BorderLayout.CENTER);
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setInheritsPopupMenu(false);
        contentPane.add(scrollPane1, BorderLayout.CENTER);
        textArea1 = new JTextPane();
        textArea1.setBackground(Color.black);
        textArea1.setCaretColor(Color.red);
        textArea1.setEditable(false);
        textArea1.setForeground(Color.green);
        scrollPane1.setViewportView(textArea1);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$()
    {
        return contentPane;
    }
}
