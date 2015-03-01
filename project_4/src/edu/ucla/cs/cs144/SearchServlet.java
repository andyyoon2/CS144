package edu.ucla.cs.cs144;

import edu.ucla.cs.cs144.AuctionSearchClient;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchServlet extends HttpServlet implements Servlet {

    public SearchServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
      String q = request.getParameter("q");
      if(q == null)
        q = "";

      int numResultsToSkip = 0;
      int numResultsToReturn = 0;
      try {
        numResultsToSkip = Integer.parseInt(request.getParameter("numResultsToSkip"));
        numResultsToReturn = Integer.parseInt(request.getParameter("numResultsToReturn"));
      } catch (NumberFormatException e){
        numResultsToReturn = 0;
        numResultsToSkip = 0;
      } catch (NullPointerException e){
        numResultsToReturn = 0;
        numResultsToSkip = 0;
      }
      SearchResult[] basicResults = AuctionSearchClient.basicSearch(q, numResultsToSkip, numResultsToReturn);

      request.setAttribute("query", q);
      request.setAttribute("skip", numResultsToSkip);
      request.setAttribute("return", numResultsToReturn);
      request.setAttribute("results", basicResults);
      request.setAttribute("length", basicResults.length);
      request.getRequestDispatcher("/basicSearch.jsp").forward(request, response);
    }
}
