
package testcases.CWE606_Unchecked_Loop_Condition;
import testcasesupport.*;
import java.util.HashMap;
import javax.servlet.http.*;
import java.util.StringTokenizer;
public class CWE606_Unchecked_Loop_Condition__getQueryString_Servlet_74a extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = ""; 
        {
            StringTokenizer tokenizer = new StringTokenizer(request.getQueryString(), "&");
            while (tokenizer.hasMoreTokens())
            {
                String token = tokenizer.nextToken(); 
                if(token.startsWith("id=")) 
                {
                    data = token.substring(3); 
                    break; 
                }
            }
        }
        HashMap<Integer,String> dataHashMap = new HashMap<Integer,String>();
        dataHashMap.put(0, data);
        dataHashMap.put(1, data);
        dataHashMap.put(2, data);
        (new CWE606_Unchecked_Loop_Condition__getQueryString_Servlet_74b()).badSink(dataHashMap , request, response );
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
        goodB2G(request, response);
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = "5";
        HashMap<Integer,String> dataHashMap = new HashMap<Integer,String>();
        dataHashMap.put(0, data);
        dataHashMap.put(1, data);
        dataHashMap.put(2, data);
        (new CWE606_Unchecked_Loop_Condition__getQueryString_Servlet_74b()).goodG2BSink(dataHashMap , request, response );
    }
    private void goodB2G(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = ""; 
        {
            StringTokenizer tokenizer = new StringTokenizer(request.getQueryString(), "&");
            while (tokenizer.hasMoreTokens())
            {
                String token = tokenizer.nextToken(); 
                if(token.startsWith("id=")) 
                {
                    data = token.substring(3); 
                    break; 
                }
            }
        }
        HashMap<Integer,String> dataHashMap = new HashMap<Integer,String>();
        dataHashMap.put(0, data);
        dataHashMap.put(1, data);
        dataHashMap.put(2, data);
        (new CWE606_Unchecked_Loop_Condition__getQueryString_Servlet_74b()).goodB2GSink(dataHashMap , request, response );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
