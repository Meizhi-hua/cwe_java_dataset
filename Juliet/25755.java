
package testcases.CWE113_HTTP_Response_Splitting.s01;
import testcasesupport.*;
import java.util.HashMap;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
public class CWE113_HTTP_Response_Splitting__File_addCookieServlet_74a extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = ""; 
        {
            File file = new File("C:\\data.txt");
            FileInputStream streamFileInput = null;
            InputStreamReader readerInputStream = null;
            BufferedReader readerBuffered = null;
            try
            {
                streamFileInput = new FileInputStream(file);
                readerInputStream = new InputStreamReader(streamFileInput, "UTF-8");
                readerBuffered = new BufferedReader(readerInputStream);
                data = readerBuffered.readLine();
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
                    if (streamFileInput != null)
                    {
                        streamFileInput.close();
                    }
                }
                catch (IOException exceptIO)
                {
                    IO.logger.log(Level.WARNING, "Error closing FileInputStream", exceptIO);
                }
            }
        }
        HashMap<Integer,String> dataHashMap = new HashMap<Integer,String>();
        dataHashMap.put(0, data);
        dataHashMap.put(1, data);
        dataHashMap.put(2, data);
        (new CWE113_HTTP_Response_Splitting__File_addCookieServlet_74b()).badSink(dataHashMap , request, response );
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
        goodB2G(request, response);
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = "foo";
        HashMap<Integer,String> dataHashMap = new HashMap<Integer,String>();
        dataHashMap.put(0, data);
        dataHashMap.put(1, data);
        dataHashMap.put(2, data);
        (new CWE113_HTTP_Response_Splitting__File_addCookieServlet_74b()).goodG2BSink(dataHashMap , request, response );
    }
    private void goodB2G(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = ""; 
        {
            File file = new File("C:\\data.txt");
            FileInputStream streamFileInput = null;
            InputStreamReader readerInputStream = null;
            BufferedReader readerBuffered = null;
            try
            {
                streamFileInput = new FileInputStream(file);
                readerInputStream = new InputStreamReader(streamFileInput, "UTF-8");
                readerBuffered = new BufferedReader(readerInputStream);
                data = readerBuffered.readLine();
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
                    if (streamFileInput != null)
                    {
                        streamFileInput.close();
                    }
                }
                catch (IOException exceptIO)
                {
                    IO.logger.log(Level.WARNING, "Error closing FileInputStream", exceptIO);
                }
            }
        }
        HashMap<Integer,String> dataHashMap = new HashMap<Integer,String>();
        dataHashMap.put(0, data);
        dataHashMap.put(1, data);
        dataHashMap.put(2, data);
        (new CWE113_HTTP_Response_Splitting__File_addCookieServlet_74b()).goodB2GSink(dataHashMap , request, response );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
