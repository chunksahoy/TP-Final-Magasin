/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Magasin;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Charles
 */
@WebServlet(name = "Inscription", urlPatterns = {"/inscription"})
public class Inscription extends HttpServlet {

   protected void processRequest(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
      response.setContentType("text/html;charset=UTF-8");
      try (PrintWriter out = response.getWriter()) {
         afficherPageConnection(request, response);
      }
   }

   @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
      processRequest(request, response);
   }

   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
      processRequest(request, response);
   }

   @Override
   public String getServletInfo() {
      return "Short description";
   }// </editor-fold>
   private void afficherPageConnection(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
      response.setContentType("text/html;charset=UTF-8");
               
      try (PrintWriter out = response.getWriter()) {
         out.println("<!DOCTYPE html>");
         out.println("<html>");
         out.println("<head>");
         out.println("<title>L\'allée des marchands</title>");
         out.println("<link href=\" html/styles/register.css\" rel=\"stylesheet\" type=\"text/css\"/>");
         out.println("<script type=\"text/javascript\" src=\"html/scripts/jquery-1.11.0.min.js\"></script>");
         out.println("</head>");
         out.println("<body>");
         
         out.println("<div id=\"container\">");
         out.println("<div id=\"inscriptionPanel\">");
         out.println("<table>");
         out.println("<tr>");
         out.println("<td>");
         out.println("<div>");
         out.println("<table id=\"inscriptionForm\">");
         out.println("<tr>");
         out.println("<td class=\"input\">");
         out.println("<span >Alias:</span></td>");
         out.println("<td><input type=\"text\" id=\"alias\" class=\"info\"/>");
         out.println("</td>");
         out.println("</tr>");
         out.println("<tr>");
         out.println("<td class=\"input\">");
         out.println("<span>Prénom:</span></td>");
         out.println("<td><input type=\"text\" id=\"prenom\" class=\"info\"/>");
         out.println("</td>");
         out.println("</tr>");
         out.println("<tr>");
         out.println("<td class=\"input\">");
         out.println("<span >Nom:</span></td>");
         out.println("<td><input type=\"text\" id=\"nom\" class=\"info\"/>");
         out.println("</td>");
         out.println("</tr>");
         out.println("</table>");
         out.println("<table>");
         out.println("<tr class=\"buttons\">");
         out.println("<td>");
         out.println("<input type=\"button\" id=\"ok\" value=\"Ok\"/>");
         out.println("<input type=\"button\" id=\"cancel\" value=\"Annuler\"/>");
         out.println("</td>");
         out.println("</tr>");
         out.println("</table>");
         out.println("</div>");
         out.println("</td>");
         out.println("</tr>");
         out.println("</table>");
         out.println("</div>");
         out.println("</div>");
         out.println("</body>");
         out.println("</html>");

      }
   }
}
