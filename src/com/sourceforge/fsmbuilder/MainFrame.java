package com.sourceforge.fsmbuilder;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import static org.jgraph.graph.DefaultGraphModel.getRoots;
import org.jgraph.graph.GraphConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.ArrayList;

/**
 * This is the main frame
 */
final class MainFrame extends JFrame
{
    /**
     * Count number of active builder frames
     */
    private static int instances = 0;
    /**
     * The JGraph model
     */
    private final DefaultGraphModel model = new DefaultGraphModel();
    /**
     * The JGraph component
     */
    final JGraph graph = new JGraph(model);

    /**
     * Ref. to memory monitor
     */
    private MemoryMonitor memoryMonitor;

    /**
     * Helper flag for toggling Memory Monitor
     */
    private boolean monIsRunning;

    /**
     * Just toggle the memMon
     */
    public void toggleMonitor()
    {
        if (monIsRunning)
        {
            memoryMonitor.surf.stop();
            remove(memoryMonitor);
            memoryMonitor = null;
        }
        else
        {
            memoryMonitor = new MemoryMonitor();
            add(memoryMonitor, BorderLayout.NORTH);
            memoryMonitor.surf.start();
            memoryMonitor.surf.setToolTipText("Click border to change settings");
        }
        validate();
        repaint();

        monIsRunning = !monIsRunning;
    }

    /**
     * Used to close the window
     */
    private final class WinCloser extends WindowAdapter
    {
        /**
         * Just call dispose()
         * @param e The Window Event
         */
        @Override
        public void windowClosing(WindowEvent e)
        {
            if (MainFrame.this.isModelEmpty() || (StaticTools.NotEmptyWarning(MainFrame.this) == 0))
            {
                eraseModel();
                MainFrame.this.dispose();
                instances--;
            }
        }
    }

    /**
     * Used to handle key events for the graph
     */
    private final class KeyHandler extends KeyAdapter
    {
        /**
         * Delete selected cells
         * @param e is the key event
         */
        @Override
        public void keyTyped(KeyEvent e)
        {
            if (e.getKeyChar() == KeyEvent.VK_DELETE)
            {
                for (Object d : graph.getSelectionCells())
                {
                    eraseObject((DefaultGraphCell) d);
                }
            }
        }
    }

    /**
     * Moves the main frame into center of screen
     */
    private void center()
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getSize();
        int x = (screenSize.width - frameSize.width) / 2;
        int y = (screenSize.height - frameSize.height) / 2;
        setLocation(x + instances * 10, y + instances * 10);
    }

    /**
     * Initializes and shows the main JFrame
     */
    private MainFrame()
    {
        super();
        graph.setInvokesStopCellEditing(true);
        graph.setGridVisible(true);
        graph.setGridEnabled(true);
        graph.setAntiAliased(true);
        graph.setXorEnabled(false);
        graph.setPreferredSize(new Dimension(500, 500));
        graph.setMarqueeHandler(new MouseInputHandler(this));
        graph.addKeyListener(new KeyHandler());
        graph.setToolTipText("Hit right mouse button to open the menu!");

        setLayout(new BorderLayout());
        add(new JScrollPane(graph), BorderLayout.CENTER);

        setTitle("Peter's FSM Builder");
        addWindowListener(new WinCloser());
        try
        {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (Exception e)
        {
            // Doesn't matter if that fails
        }
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        pack();
        center();
        instances++;

//        File f = new File ("C:\\Users\\peter\\Desktop\\T2.gif");
//        URI uri = f.toURI();
//        URL url = null;
//        try
//        {
//            url = uri.toURL();
//        }
//        catch (MalformedURLException e)
//        {
//
//        }
//        SplashScreen splash = SplashScreen.getSplashScreen();
//        try
//        {
//            splash.setImageURL(url);
//        }
//        catch (IOException e)
//        {
//
//        }
    }

    /**
     * Seeks the model for the name cell
     * @return Reference to name cell or <b>null</b> if not found
     */
    NameCell getNameCell()
    {
        Object[] items = graph.getDescendants(graph.getRoots());
        for (Object item : items)
        {
            if (item instanceof NameCell)
            {
                return (NameCell) item;
            }
        }
        return null;
    }

    /**
     * Retrieves all staes
     * @return All the states as an array
     */
    private StateData[] getAllStateData()
    {
        ArrayList<StateData> arr = new ArrayList<StateData>();
        Object[] items = graph.getDescendants(graph.getRoots());
        for (Object item : items)
        {
            if (item instanceof StateCell)
            {
                arr.add(((StateCell) item).getData());
            }
        }
        return arr.toArray(new StateData[arr.size()]);
    }

    /**
     * Retrieves all transitions
     * Also fills <b>from</b> and <b>to</b> members of TransitionData
     * @return All transitions as array
     */
    private TransitionData[] getAllTransitionData()
    {
        ArrayList<TransitionData> arr = new ArrayList<TransitionData>();
        Object[] items = graph.getDescendants(graph.getRoots());
        for (Object item : items)
        {
            TransitionData trdata = null;
            if (item instanceof TransitionCell)
            {
                trdata = ((TransitionCell) item).getData();
            }
            if (item instanceof StartingTransitionCell)
            {
                trdata = ((StartingTransitionCell) item).getData();
            }
            if (trdata != null)
            {
                StateCell source = (StateCell) model.getParent(model.getSource(item));
                StateCell target = (StateCell) model.getParent(model.getTarget(item));
                if (source != null)
                {
                    trdata.from = source.getData().name;
                }
                else
                {
                    trdata.from = null;
                }
                if (target != null)
                {
                    trdata.to = target.getData().name;
                }
                else
                {
                    trdata.to = null;
                }
                arr.add(trdata);
            }
        }
        return arr.toArray(new TransitionData[arr.size()]);
    }

    /**
     * Gets all data fields from all objects in the model
     * @return Guess what?
     */
    FSMData getFSMData()
    {
        FSMData fsm = new FSMData();
        NameCell n = getNameCell();
        if (n == null)
        {
            fsm.name = null;
        }
        else
        {
            fsm.name = n.getName();
            fsm.comment = n.getComment();
        }
        fsm.transitions = getAllTransitionData();
        fsm.states = getAllStateData();
        return fsm;
    }

    /**
     * Forces repaint of a single graph object
     * @param x Object to repaint
     */
    void forceRepaint(DefaultGraphCell x)
    {
        Object[] o = {x};
        graph.getGraphLayoutCache().toFront(o);  // causes repaint
    }

    /**
     * Removes single Object and its children from model
     * @param x Object to be removed
     */
    void eraseObject(DefaultGraphCell x)
    {
        if (x.getChildCount() != 0)
        {
            graph.getModel().remove(x.getChildren().toArray());
        }
        Object[] o = {x};
        graph.getModel().remove(o);
    }

    /**
     * Duplicates an object and inserts it into the model
     * @param x Object to be cloned
     * @throws Exception If something went wrong
     */
    void cloneObject(DefaultGraphCell x) throws Exception
    {
        // Copy object
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream o_out = new ObjectOutputStream(out);
        o_out.writeObject(x);
        ObjectInputStream o_in = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
        DefaultGraphCell cell = (DefaultGraphCell) o_in.readObject();
        // Shift points
        if (cell instanceof TransitionCell)
        {
            ((TransitionCell) cell).setSource(null);
            ((TransitionCell) cell).setTarget(null);
            ArrayList<Point2D.Double> list =
                    (ArrayList<Point2D.Double>) GraphConstants.getPoints(cell.getAttributes());
            for (Point2D.Double p : list)
            {
                p.x += 10;
                p.y -= 10;
            }
        }
        else if (cell instanceof StateCell)
        {
            Rectangle2D r = GraphConstants.getBounds(cell.getAttributes());
            GraphConstants.setBounds(cell.getAttributes(),
                                     new Rectangle2D.Double(r.getX() + 10, r.getY() + 10, r.getWidth(), r.getHeight()));
        }
        // Insert into model
        graph.getGraphLayoutCache().insert(cell);
    }

    /**
     * Checks if model is empty
     * Not counting the name cell
     * @return true if model is empty
     */
    boolean isModelEmpty()
    {
        Object[] o = graph.getDescendants(graph.getRoots());
        return o.length == 0 || o.length == 1 && o[0] instanceof NameCell;
    }

    /**
     * Saves the model to disk
     * @param path The path where the model will be saved
     * @throws java.io.IOException is thrown if something fails
     */
    void saveModel(String path) throws Exception
    {
        ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(path));
        o.writeObject(getRoots(model));
        o.close();
    }

    /**
     * Loads entire model from disk
     * @param path The path where the model file is stored
     * @throws Exception is thrown if something fails
     */
    void loadModel(String path) throws Exception
    {
        ObjectInputStream o = new ObjectInputStream(new FileInputStream(path));
        Object roots[] = (Object[]) o.readObject();
        graph.getGraphLayoutCache().insert(roots);
        graph.repaint();
        o.close();
    }

    /**
     * Deletes all elements from model
     */
    public void eraseModel()
    {
        Object[] roots = graph.getDescendants(graph.getRoots());
        for (Object item : roots)
        {
            eraseObject((DefaultGraphCell) item);
        }
        graph.repaint();
    }

    /**
     * Entry function
     * @param args Entry function arguments (not used)
     */
    public static void main(String... args)
    {
        MainFrame m = new MainFrame();
        m.setVisible(true);
    }
}
