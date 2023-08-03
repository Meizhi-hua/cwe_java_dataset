
package testcases.CWE760_Predictable_Salt_One_Way_Hash;
import testcasesupport.*;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Random;
public class CWE760_Predictable_Salt_One_Way_Hash__basic_12 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        if (IO.staticReturnsTrueOrFalse())
        {
            Random random = new Random();
            MessageDigest hash = MessageDigest.getInstance("SHA-512");
            hash.update((Integer.toString(random.nextInt())).getBytes("UTF-8"));
            byte[] hashValue = hash.digest("hash me".getBytes("UTF-8"));
            IO.writeLine(IO.toHex(hashValue));
        }
        else
        {
            SecureRandom secureRandom = new SecureRandom();
            MessageDigest hash = MessageDigest.getInstance("SHA-512");
            SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");
            hash.update(prng.generateSeed(32));
            byte[] hashValue = hash.digest("hash me".getBytes("UTF-8"));
            IO.writeLine(IO.toHex(hashValue));
        }
    }
    private void good1() throws Throwable
    {
        if (IO.staticReturnsTrueOrFalse())
        {
            SecureRandom secureRandom = new SecureRandom();
            MessageDigest hash = MessageDigest.getInstance("SHA-512");
            SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");
            hash.update(prng.generateSeed(32));
            byte[] hashValue = hash.digest("hash me".getBytes("UTF-8"));
            IO.writeLine(IO.toHex(hashValue));
        }
        else
        {
            SecureRandom secureRandom = new SecureRandom();
            MessageDigest hash = MessageDigest.getInstance("SHA-512");
            SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");
            hash.update(prng.generateSeed(32));
            byte[] hashValue = hash.digest("hash me".getBytes("UTF-8"));
            IO.writeLine(IO.toHex(hashValue));
        }
    }
    public void good() throws Throwable
    {
        good1();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
