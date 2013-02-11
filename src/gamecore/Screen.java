package gamecore;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.lang.reflect.InvocationTargetException;
import javax.swing.*;

public class Screen
{
    private GraphicsDevice device;
    private JFrame frame;

    public Screen()
    {
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        device = environment.getDefaultScreenDevice();
        Point centerpoint = environment.getCenterPoint();
        frame = new JFrame();
        frame.setBounds(centerpoint.x - 400 , centerpoint.y - 300, 800, 600);
    }

    public void setScreen ()
    {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setIgnoreRepaint(true);
        frame.setResizable(false);
        frame.setVisible(true);

        try
        {
            EventQueue.invokeAndWait(
            new Runnable()
            {
                public void run ()
                {
                    frame.createBufferStrategy(2);
                }
            }
                                    );
        }
        catch (InterruptedException ex ){}
        catch (InvocationTargetException ex ) {}
    }

    public Graphics2D getGraphics()
    {
        if (frame != null )
        {
            BufferStrategy strategy = frame.getBufferStrategy();
            return (Graphics2D)strategy.getDrawGraphics();
        }
        else return null;
    }

    public void update()
    {
        if (frame != null )
        {
            BufferStrategy strategy = frame.getBufferStrategy();
            if (!strategy.contentsLost()) strategy.show();
        }
        Toolkit.getDefaultToolkit().sync();
    }

    public JFrame getFrame()
    {
        return frame;
    }
}


