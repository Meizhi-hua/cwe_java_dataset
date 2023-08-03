
package testcases.CWE197_Numeric_Truncation_Error.s03;
import testcasesupport.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
public class CWE197_Numeric_Truncation_Error__short_URLConnection_21 extends AbstractTestCase
{
    private boolean badPrivate = false;
    public void bad() throws Throwable
    {
        short data;
        badPrivate = true;
        data = bad_source();
        {
            IO.writeLine((byte)data);
        }
    }
    private short bad_source() throws Throwable
    {
        short data;
        if (badPrivate)
        {
            data = Short.MIN_VALUE; 
            {
                URLConnection urlConnection = (new URL("http:
                BufferedReader readerBuffered = null;
                InputStreamReader readerInputStream = null;
                try
                {
                    readerInputStream = new InputStreamReader(urlConnection.getInputStream(), "UTF-8");
                    readerBuffered = new BufferedReader(readerInputStream);
                    String stringNumber = readerBuffered.readLine();
                    if (stringNumber != null) 
                    {
                        try
                        {
                            data = Short.parseShort(stringNumber.trim());
                        }
                        catch (NumberFormatException exceptNumberFormat)
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
                }
            }
        }
        else
        {
            data = 0;
        }
        return data;
    }
    private boolean goodG2B1_private = false;
    private boolean goodG2B2_private = false;
    public void good() throws Throwable
    {
        goodG2B1();
        goodG2B2();
    }
    private void goodG2B1() throws Throwable
    {
        short data;
        goodG2B1_private = false;
        data = goodG2B1_source();
        {
            IO.writeLine((byte)data);
        }
    }
    private short goodG2B1_source() throws Throwable
    {
        short data = 0;
        if (goodG2B1_private)
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        return data;
    }
    private void goodG2B2() throws Throwable
    {
        short data;
        goodG2B2_private = true;
        data = goodG2B2_source();
        {
            IO.writeLine((byte)data);
        }
    }
    private short goodG2B2_source() throws Throwable
    {
        short data = 0;
        if (goodG2B2_private)
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        return data;
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
