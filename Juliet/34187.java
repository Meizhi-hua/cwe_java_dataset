
package testcases.CWE190_Integer_Overflow.s01;
import testcasesupport.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
public class CWE190_Integer_Overflow__int_connect_tcp_multiply_42 extends AbstractTestCase
{
    private int badSource() throws Throwable
    {
        int data;
        data = Integer.MIN_VALUE; 
        {
            Socket socket = null;
            BufferedReader readerBuffered = null;
            InputStreamReader readerInputStream = null;
            try
            {
                socket = new Socket("host.example.org", 39544);
                readerInputStream = new InputStreamReader(socket.getInputStream(), "UTF-8");
                readerBuffered = new BufferedReader(readerInputStream);
                String stringNumber = readerBuffered.readLine();
                if (stringNumber != null) 
                {
                    try
                    {
                        data = Integer.parseInt(stringNumber.trim());
                    }
                    catch(NumberFormatException exceptNumberFormat)
                    {
                        IO.logger.log(Level.WARNING, "Number format exception parsing data from string", exceptNumberFormat);
                    }
                }
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error with stream reading", exceptIO);
            }
            finally
            {
                try
                {
                    if (readerBuffered != null)
                    {
                        readerBuffered.close();
                    }
                }
                catch (IOException exceptIO)
                {
                    IO.logger.log(Level.WARNING, "Error closing BufferedReader", exceptIO);
                }
                try
                {
                    if (readerInputStream != null)
                    {
                        readerInputStream.close();
                    }
                }
                catch (IOException exceptIO)
                {
                    IO.logger.log(Level.WARNING, "Error closing InputStreamReader", exceptIO);
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
                    IO.logger.log(Level.WARNING, "Error closing Socket", exceptIO);
                }
            }
        }
        return data;
    }
    public void bad() throws Throwable
    {
        int data = badSource();
        if(data > 0) 
        {
            int result = (int)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    private int goodG2BSource() throws Throwable
    {
        int data;
        data = 2;
        return data;
    }
    private void goodG2B() throws Throwable
    {
        int data = goodG2BSource();
        if(data > 0) 
        {
            int result = (int)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    private int goodB2GSource() throws Throwable
    {
        int data;
        data = Integer.MIN_VALUE; 
        {
            Socket socket = null;
            BufferedReader readerBuffered = null;
            InputStreamReader readerInputStream = null;
            try
            {
                socket = new Socket("host.example.org", 39544);
                readerInputStream = new InputStreamReader(socket.getInputStream(), "UTF-8");
                readerBuffered = new BufferedReader(readerInputStream);
                String stringNumber = readerBuffered.readLine();
                if (stringNumber != null) 
                {
                    try
                    {
                        data = Integer.parseInt(stringNumber.trim());
                    }
                    catch(NumberFormatException exceptNumberFormat)
                    {
                        IO.logger.log(Level.WARNING, "Number format exception parsing data from string", exceptNumberFormat);
                    }
                }
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error with stream reading", exceptIO);
            }
            finally
            {
                try
                {
                    if (readerBuffered != null)
                    {
                        readerBuffered.close();
                    }
                }
                catch (IOException exceptIO)
                {
                    IO.logger.log(Level.WARNING, "Error closing BufferedReader", exceptIO);
                }
                try
                {
                    if (readerInputStream != null)
                    {
                        readerInputStream.close();
                    }
                }
                catch (IOException exceptIO)
                {
                    IO.logger.log(Level.WARNING, "Error closing InputStreamReader", exceptIO);
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
                    IO.logger.log(Level.WARNING, "Error closing Socket", exceptIO);
                }
            }
        }
        return data;
    }
    private void goodB2G() throws Throwable
    {
        int data = goodB2GSource();
        if(data > 0) 
        {
            if (data < (Integer.MAX_VALUE/2))
            {
                int result = (int)(data * 2);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too large to perform multiplication.");
            }
        }
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
