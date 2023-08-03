
package testcases.CWE400_Resource_Exhaustion.s01;
import testcasesupport.*;
import javax.servlet.http.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.logging.Level;
public class CWE400_Resource_Exhaustion__getCookies_Servlet_write_67b
{
    public void badSink(CWE400_Resource_Exhaustion__getCookies_Servlet_write_67a.Container countContainer , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int count = countContainer.containerOne;
        File file = new File("badSink.txt");
        FileOutputStream streamFileOutput = new FileOutputStream(file);
        OutputStreamWriter writerOutputStream = new OutputStreamWriter(streamFileOutput, "UTF-8");
        BufferedWriter writerBuffered = new BufferedWriter(writerOutputStream);
        int i;
        for (i = 0; i < count; i++)
        {
            try
            {
                writerBuffered.write("Hello");
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error with stream writing", exceptIO);
            }
        }
        try
        {
            if (writerBuffered != null)
            {
                writerBuffered.close();
            }
        }
        catch (IOException exceptIO)
        {
            IO.logger.log(Level.WARNING, "Error closing BufferedWriter", exceptIO);
        }
        try
        {
            if (writerOutputStream != null)
            {
                writerOutputStream.close();
            }
        }
        catch (IOException exceptIO)
        {
            IO.logger.log(Level.WARNING, "Error closing OutputStreamWriter", exceptIO);
        }
        try
        {
            if (streamFileOutput != null)
            {
                streamFileOutput.close();
            }
        }
        catch (IOException exceptIO)
        {
            IO.logger.log(Level.WARNING, "Error closing FileOutputStream", exceptIO);
        }
    }
    public void goodG2BSink(CWE400_Resource_Exhaustion__getCookies_Servlet_write_67a.Container countContainer , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int count = countContainer.containerOne;
        File file = new File("badSink.txt");
        FileOutputStream streamFileOutput = new FileOutputStream(file);
        OutputStreamWriter writerOutputStream = new OutputStreamWriter(streamFileOutput, "UTF-8");
        BufferedWriter writerBuffered = new BufferedWriter(writerOutputStream);
        int i;
        for (i = 0; i < count; i++)
        {
            try
            {
                writerBuffered.write("Hello");
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error with stream writing", exceptIO);
            }
        }
        try
        {
            if (writerBuffered != null)
            {
                writerBuffered.close();
            }
        }
        catch (IOException exceptIO)
        {
            IO.logger.log(Level.WARNING, "Error closing BufferedWriter", exceptIO);
        }
        try
        {
            if (writerOutputStream != null)
            {
                writerOutputStream.close();
            }
        }
        catch (IOException exceptIO)
        {
            IO.logger.log(Level.WARNING, "Error closing OutputStreamWriter", exceptIO);
        }
        try
        {
            if (streamFileOutput != null)
            {
                streamFileOutput.close();
            }
        }
        catch (IOException exceptIO)
        {
            IO.logger.log(Level.WARNING, "Error closing FileOutputStream", exceptIO);
        }
    }
    public void goodB2GSink(CWE400_Resource_Exhaustion__getCookies_Servlet_write_67a.Container countContainer , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int count = countContainer.containerOne;
        if (count > 0 && count <= 20)
        {
            File file = new File("goodSink.txt");
            FileOutputStream streamFileOutput = new FileOutputStream(file);
            OutputStreamWriter writerOutputStream = new OutputStreamWriter(streamFileOutput, "UTF-8");
            BufferedWriter writerBuffered = new BufferedWriter(writerOutputStream);
            int i;
            for (i = 0; i < count; i++)
            {
                try
                {
                    writerBuffered.write("Hello");
                }
                catch (IOException exceptIO)
                {
                    IO.logger.log(Level.WARNING, "Error with stream writing", exceptIO);
                }
            }
            try
            {
                if (writerBuffered != null)
                {
                    writerBuffered.close();
                }
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error closing BufferedWriter", exceptIO);
            }
            try
            {
                if (writerOutputStream != null)
                {
                    writerOutputStream.close();
                }
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error closing OutputStreamWriter", exceptIO);
            }
            try
            {
                if (streamFileOutput != null)
                {
                    streamFileOutput.close();
                }
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error closing FileOutputStream", exceptIO);
            }
        }
    }
}
