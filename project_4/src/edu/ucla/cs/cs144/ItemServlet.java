package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ucla.cs.cs144.AuctionSearchClient;

public class ItemServlet extends HttpServlet implements Servlet {

    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
      String id = request.getParameter("id");
      if(id == null)
        id = "Empty";
      String item = AuctionSearchClient.getXMLDataForItemId(id);
      if(item == null)
        item = "";

      request.setAttribute("id", id);
      request.setAttribute("result", item.replace("'", "\\'").replaceAll("[\n\r]", "").replaceAll("\t", ""));
      request.getRequestDispatcher("/itemId.jsp").forward(request, response);
    }
}
