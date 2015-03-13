package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ConfirmServlet extends HttpServlet implements Servlet {

    public ConfirmServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
      //TODO: Do secure SSL stuff (request.isSecure())
      if(!request.isSecure()){
        request.getRequestDispatcher("/redirect.jsp").forward(request, response);
        return;
      }


      String card = request.getParameter("card");
      if(card == null)
        card = "";
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
      request.setAttribute("card", card);
      DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
      Date date = new Date();
      request.setAttribute("time", dateFormat.format(date));
      request.getRequestDispatcher("/confirmPage.jsp").forward(request, response);
    }
}
