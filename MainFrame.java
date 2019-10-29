package Package4;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.io.*;
import java.io.BufferedReader;

@SuppressWarnings("serial")
public class MainFrame extends JFrame
{
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private JFileChooser fileChooser = null;
    private JCheckBoxMenuItem showAxisMenuItem;
    private JCheckBoxMenuItem showMarkersMenuItem;
    private JCheckBoxMenuItem showSomeMenuItem;
    private JCheckBoxMenuItem showExtraMenuItem;
    private JCheckBoxMenuItem showDegreedMenuItem;
    private GraphicsDisplay display = new GraphicsDisplay();
    private boolean fileLoaded = false;
    public MainFrame()
    {
        super("Построение графиков функций на основе заранее подготовленных файлов");
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH)/2, (kit.getScreenSize().height - HEIGHT)/2);
        setExtendedState(MAXIMIZED_BOTH);
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("Файл");
        menuBar.add(fileMenu);
        Action openGraphicsAction = new AbstractAction("Открыть файл с графиком")
        {
            public void actionPerformed(ActionEvent event)
            {
                if (fileChooser==null)
                {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                    if (fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION)
                    openGraphics(fileChooser.getSelectedFile());
            }
        };
        fileMenu.add(openGraphicsAction);
        Action openGraphicsAction1 = new AbstractAction("Открыть текстовый файл с графиком")
        {
            public void actionPerformed(ActionEvent event)
            {
                if (fileChooser==null)
                {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION)
                    openGraphics1(fileChooser.getSelectedFile());
            }
        };
        fileMenu.add(openGraphicsAction1);
        JMenu graphicsMenu = new JMenu("График");
        menuBar.add(graphicsMenu);
        Action showAxisAction = new AbstractAction("Показывать оси координат")
        {
            public void actionPerformed(ActionEvent event)
            {
                display.setShowAxis(showAxisMenuItem.isSelected());
            }
        };
        showAxisMenuItem = new JCheckBoxMenuItem(showAxisAction);
        graphicsMenu.add(showAxisMenuItem);
        showAxisMenuItem.setSelected(true);
        Action showMarkersAction = new AbstractAction("Показывать маркеры точек")
        {
            public void actionPerformed(ActionEvent event)
            {
                display.setShowMarkers(showMarkersMenuItem.isSelected());
            }
        };
        showMarkersMenuItem = new JCheckBoxMenuItem(showMarkersAction);
        graphicsMenu.add(showMarkersMenuItem);
        showMarkersMenuItem.setSelected(true);
        Action showSomeAction = new AbstractAction("Показывать маркеры некоторых точек")
        {
            public void actionPerformed(ActionEvent event)
            {
                display.setShowSome(showSomeMenuItem.isSelected());
            }
        };
        showSomeMenuItem = new JCheckBoxMenuItem(showSomeAction);
        graphicsMenu.add(showSomeMenuItem);
        showSomeMenuItem.setSelected(false);
        Action showExtraAction = new AbstractAction("Показывать замкнутые области")
        {
            public void actionPerformed(ActionEvent event)
            {
                display.setShowExtra(showExtraMenuItem.isSelected());
            }
        };
        showExtraMenuItem = new JCheckBoxMenuItem(showExtraAction);
        graphicsMenu.add(showExtraMenuItem);
        showExtraMenuItem.setSelected(false);
        Action showDegreeAction = new AbstractAction("Перевернуть график на 90о")
        {
            public void actionPerformed(ActionEvent event)
            {
                display.setShowDegree(showDegreedMenuItem.isSelected());
            }
        };
        showDegreedMenuItem = new JCheckBoxMenuItem(showDegreeAction);
        graphicsMenu.add(showDegreedMenuItem);
        showDegreedMenuItem.setSelected(false);
        graphicsMenu.addMenuListener(new GraphicsMenuListener());
        getContentPane().add(display, BorderLayout.CENTER);
    }
   protected void openGraphics(File selectedFile)
   {
       try
       {
           DataInputStream in = new DataInputStream(new FileInputStream(selectedFile));
           Double[][] graphicsData = new Double[in.available()/(Double.SIZE/8)/2][];
           int i = 0;
           while (in.available() > 0)
           {
               Double x = in.readDouble();
               Double y = in.readDouble();
               graphicsData[i++] = new Double[] {x, y};
           }
           if (graphicsData!=null && graphicsData.length > 0)
           {
               fileLoaded = true;
               display.showGraphics(graphicsData);
           }
           in.close();
       }
       catch (FileNotFoundException ex)
       {
           JOptionPane.showMessageDialog(MainFrame.this, "Указанный файл не найден", "Ошибка загрузки данных", JOptionPane.WARNING_MESSAGE);
           return;
       }
       catch (IOException ex)
       {
           JOptionPane.showMessageDialog(MainFrame.this, "Ошибка чтения координат точек из файла", "Ошибка загрузки данных", JOptionPane.WARNING_MESSAGE);
           return;
       }
   }
    protected void openGraphics1(File selectedFile)
    {
        try
        {
            BufferedReader in1 = new BufferedReader (new FileReader(selectedFile));
            int cnt = 0;
            while(true)
            {
                String s1 = in1.readLine();
                if (s1 == null)
                {
                    break;
                }
                cnt++;
            }
            Double[][] graphicsData = new Double[cnt / 2][];
            in1.close();
            BufferedReader in = new BufferedReader (new FileReader(selectedFile));
            for (int i = 0; i < cnt / 2;)
            {

                Double x = Double.parseDouble(in.readLine());
                Double y = Double.parseDouble(in.readLine());
                graphicsData[i++] = new Double[] {x, y};
            }
            if (graphicsData!=null && graphicsData.length > 0)
            {
                fileLoaded = true;
                display.showGraphics(graphicsData);
            }
        }
        catch (FileNotFoundException ex)
        {
            JOptionPane.showMessageDialog(MainFrame.this, "Указанный файл не найден", "Ошибка загрузки данных", JOptionPane.WARNING_MESSAGE);
            return;
        }
        catch (IOException ex)
        {
            JOptionPane.showMessageDialog(MainFrame.this, "Ошибка чтения координат точек из файла", "Ошибка загрузки данных", JOptionPane.WARNING_MESSAGE);
            return;
        }
    }

    public static void main(String[] args)
    {
        MainFrame frame = new MainFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    private class GraphicsMenuListener implements MenuListener
    {
        public void menuSelected(MenuEvent e)
        {
            showAxisMenuItem.setEnabled(fileLoaded);
            showMarkersMenuItem.setEnabled(fileLoaded);
            showSomeMenuItem.setEnabled(fileLoaded);
            showExtraMenuItem.setEnabled(fileLoaded);
            showDegreedMenuItem.setEnabled(fileLoaded);
        }
        public void menuDeselected(MenuEvent e)
        {

        }
        public void menuCanceled(MenuEvent e)
        {

        }
    }
}
