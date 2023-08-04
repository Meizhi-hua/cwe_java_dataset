// 6. 使用ProcessBuilder执行OS命令并读取错误输出
import java.io.*;

public class OSCommandInjectionExample6 {
    public static void main(String[] args) throws IOException {
        String userInput = "example.com";
        ProcessBuilder builder = new ProcessBuilder("ping", userInput);
        builder.redirectErrorStream(true); // 合并错误输出到标准输出
        Process process = builder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        reader.close();
    }
}