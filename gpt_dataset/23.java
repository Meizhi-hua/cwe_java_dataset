import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

@WebServlet("/InsecureFileServlet")
public class InsecureFileServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String queryString = request.getQueryString(); // Get the query string from the request
        String filePath = "/var/www/html/" + queryString; // Combine the query string with a base path

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                response.getWriter().println(line); // Vulnerable to CWE-15
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

