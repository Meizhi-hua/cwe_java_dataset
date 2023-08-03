
package testcases.CWE510_Trapdoor;
import testcasesupport.*;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.logging.Level;
public class CWE510_Trapdoor__hostname_based_logic_01 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        ServerSocket listener = null;
        Socket socket = null;
        OutputStream streamOutput = null;
        int port = 20000;
        try
        {
            listener = new ServerSocket(port);
            socket = listener.accept(); 
            if (socket.getInetAddress().getHostName().equals("admin.google.com"))
            {
                streamOutput = socket.getOutputStream();
                streamOutput.write("Welcome, admin!".getBytes("UTF-8"));
            }
            else
            {
                streamOutput = socket.getOutputStream();
                streamOutput.write("Welcome, user.".getBytes("UTF-8"));
            }
        }
        catch (IOException exceptIO)
        {
            IO.logger.log(Level.WARNING, "Could not connect to port " + Integer.toString(port), exceptIO);
        }
        finally
        {
            try
            {
                if (streamOutput != null)
                {
                    streamOutput.close();
                }
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error closing objects", exceptIO);
            }
            try
            {
                if (socket != null)
                {
                    socket.close();
                }
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error closing objects", exceptIO);
            }
            try
            {
                if (listener != null)
                {
                    listener.close();
                }
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error closing objects", exceptIO);
            }
        }
    }
    public void good() throws Throwable
    {
        good1();
    }
    private void good1() throws Throwable
    {
        ServerSocket listener = null;
        Socket socket = null;
        OutputStream streamOutput = null;
        int port = 20000;
        try
        {
            listener = new ServerSocket(port);
            socket = listener.accept();
            streamOutput = socket.getOutputStream();
            streamOutput.write(("Welcome, " + socket.getInetAddress().getHostName()).getBytes("UTF-8"));
        }
        catch (IOException exceptIO)
        {
            IO.logger.log(Level.WARNING, "Could not connect to port " + Integer.toString(port), exceptIO);
        }
        finally
        {
            try
            {
                if (streamOutput != null)
                {
                    streamOutput.close();
                }
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error closing objects", exceptIO);
            }
            try
            {
                if (socket != null)
                {
                    socket.close();
                }
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error closing objects", exceptIO);
            }
            try
            {
                if (listener != null)
                {
                    listener.close();
                }
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error closing objects", exceptIO);
            }
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
