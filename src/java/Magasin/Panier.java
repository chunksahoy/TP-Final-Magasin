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
@WebServlet(name = "Panier", urlPatterns = {"/panier"})
public class Panier extends HttpServlet {

   protected void processRequest(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
      response.setContentType("text/html;charset=UTF-8");
      try (PrintWriter out = response.getWriter()) {
         
         out.println("<!DOCTYPE html>");
         out.println("<html>");
         out.println("<head>");
         out.println("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\" />");
         out.println("<meta name=\"description\" content=\"Panier/Inventaire\"/>");
         out.println("<meta name=\"author\" content=\"Charles Hunter-Roy & Alexis Lalonde\" />");
         out.println("<title>Panier</title> ");
         out.println("<link href=\" html/styles/panier.css\" rel=\"stylesheet\" type=\"text/css\"/>");
         out.println("<script type=\"text/javascript\" src=\"html/scripts/jquery-1.11.0.min.js\"></script>");
         out.println("</head>");
         
         
         out.println("<form action='panier' action='get'");
         out.println("<body>");
         out.println("<div id=\"container\">");
         out.println("<div id=\"sub\">");
         out.println("<table>");
         out.println("<tr>");
         out.println("<td>");
         out.println("<div id=\"panierJoueur\">");
         out.println("<table id=\"panierItems\">");
         out.println("<tr id=\"panierLegende\">");
         out.println("<td>Item</td>");
         out.println("<td>Prix</td>");
         out.println("<td>Quantité</td>");
         out.println("<td>Genre</td>");
         out.println("</tr>");
         out.println("</table>");
         out.println("</div>");
         out.println("</td>");
         out.println("<td>");
         out.println("<div id=\"attributes\">");
         out.println("<table>");
         out.println("<tr id=\"attLegende\">");
         out.println("<td colspan=\"2\">Item</td>");
         out.println("</tr>");
         out.println("<tr id=\"stats\">");
         out.println("<td id=\"nom\" colspan=\"2\">Nom</td>");
         out.println("</tr>");
         out.println("<tr>");
         out.println("<td colspan=\"2\">attribut1</td>");
         out.println("</tr>");
         out.println("<tr>");
         out.println("<td colspan=\"2\">attribut2</td>");
         out.println("</tr>");
         out.println("<tr>");
         out.println("<td colspan=\"2\">attribut3</td>");
         out.println("</tr>");
         out.println("<tr id=\"info\">");
         out.println("<td>Prix:<span>0</span></td>");
         out.println("<td>Qte:<input type=\"number\" id=\"quantite\"/></td>");
         out.println("</tr>");
         out.println("<tr>");
         out.println("<td colspan=\"2\">");
         out.println("<input type=\"button\" id=\"update\" value=\"updateTODO\"/>");
         out.println("</td>");
         out.println("</tr>");
         out.println("</table>");
         out.println("</div>");
         out.println("</td>");
         out.println("</tr>");
         out.println("<tr>");
         out.println("<td>");
         out.println("<div id=\"inventaireJoueur\">");
         out.println("<table>");
         out.println("<tr id=\"inventLegende\">");
         out.println("<td>Item</td>");
         out.println("<td>Quantité</td>");
         out.println("</tr>");
         out.println("</table>");
         out.println("</div>");
         out.println("</td>");
         out.println("<td>");
         out.println("<div id=\"paiementOptions\">");
         out.println("<table>");
         out.println("<tr>");
         out.println("<td>Total:<span id=\"total\">0</span>$</td>");
         out.println("</tr>");
         out.println("<tr>");
         out.println("<td>Montant:<span id=\"montant\">0</span>$</td>");
         out.println("</tr>");
         out.println("<tr>");
         out.println("<td><input type=\"submit\" id=\"confirm\" value=\"Confirmer le paiement\" name='confirm'/></td>");
         out.println("</tr>");
         out.println("</table>");
         out.println("</div>");
         out.println("</td>");
         out.println("</tr>");
         out.println("</table>");
         out.println("</div>");
         out.println("</div>");
         out.println("</body>");
         out.println("</form>");
         out.println("</html>");
         
      }
   }
   
   // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
   /**
    * Handles the HTTP <code>GET</code> method.
    *
    * @param request servlet request
    * @param response servlet response
    * @throws ServletException if a servlet-specific error occurs
    * @throws IOException if an I/O error occurs
    */
   @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
      processRequest(request, response);
   }
   
   /**
    * Handles the HTTP <code>POST</code> method.
    *
    * @param request servlet request
    * @param response servlet response
    * @throws ServletException if a servlet-specific error occurs
    * @throws IOException if an I/O error occurs
    */
   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
      processRequest(request, response);
   }
   
   /**
    * Returns a short description of the servlet.
    *
    * @return a String containing servlet description
    */
   @Override
   public String getServletInfo() {
      return "Short description";
   }// </editor-fold>
   
}
