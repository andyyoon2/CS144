package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.URL;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;


public class ProxyServlet extends HttpServlet implements Servlet {
       
    public ProxyServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
      String charset = java.nio.charset.StandardCharsets.UTF_8.name();
      String url = "http://google.com/complete/search?output=toolbar&";
      String q = String.format("q=%s", URLEncoder.encode(request.getParameter("q"), charset));
      InputStream is = null;
      OutputStream os = null;

      HttpURLConnection connection = (HttpURLConnection) new URL(url + q).openConnection();
      is = connection.getInputStream();
      os = response.getOutputStream();
      int cur_byte = -1;

      response.setContentType("text/xml");
      try {
        while ((cur_byte = is.read()) != -1) {
          os.write(cur_byte);
        }
        os.flush();
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        if (is != null) is.close();
        if (os != null) os.close();
      }
    }
}
