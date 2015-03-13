package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ConfirmServlet extends HttpServlet implements Servlet {

    public ConfirmServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
      //TODO: Do secure SSL stuff (request.isSecure())
      HttpSession session = request.getSession(true);
      if (session.isNew()) {
        throw new ServletException("No session info for purchase");
      }
      String item = (String)session.getAttribute("item");
      if (item == null)
        item = "";
      String id = (String)session.getAttribute("id");

      request.setAttribute("id", id);
      request.setAttribute("result", item);
      request.getRequestDispatcher("/confirmPage.jsp").forward(request, response);
    }
}
