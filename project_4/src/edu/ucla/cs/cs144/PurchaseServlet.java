package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PurchaseServlet extends HttpServlet implements Servlet {

    public PurchaseServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
      HttpSession session = request.getSession(true);
      if (session.isNew()) {
        request.getRequestDispatcher("/redirect.jsp").forward(request, response);
        return;
      }
      String item = (String)session.getAttribute("item");
      if (item == null)
        item = "";
      String id = (String)session.getAttribute("id");


      request.setAttribute("id", id);
      request.setAttribute("result", item);

      String url = "https://" + request.getServerName() + ":8443/eBay/confirm";
      request.setAttribute("post_url", url);
      request.getRequestDispatcher("/purchasePage.jsp").forward(request, response);
    }
}
