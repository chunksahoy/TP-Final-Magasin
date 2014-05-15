/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Magasin;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
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
      try (PrintWriter out = response.getWriter()) {
         out.println("<!DOCTYPE html>");
         out.println("<html>");
         out.println("<head>");
         out.println("<title>L\'allée des marchands</title>");
         out.println("<link href=\" html/styles/register.css\" rel=\"stylesheet\" type=\"text/css\"/>");
         out.println("<script type=\"text/javascript\" src=\"html/scripts/jquery-1.11.0.min.js\"></script>");
         out.println("</head>");
         
         out.println("<form action='inscription' method='post'>");
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
         out.println("<td><input type=\"text\" id=\"alias\" class=\"info\" name='alias'/>");
         out.println("</td>");
         out.println("</tr>");
         out.println("<tr>");
         out.println("<td class=\"input\">");
         out.println("<span>Prénom:</span></td>");
         out.println("<td><input type=\"text\" id=\"prenom\" class=\"info\" name='prenom'/>");
         out.println("</td>");
         out.println("</tr>");
         out.println("<tr>");
         out.println("<td class=\"input\">");
         out.println("<span >Nom:</span></td>");
         out.println("<td><input type=\"text\" id=\"nom\" class=\"info\" name='nom'/>");
         out.println("</td>");
         out.println("</tr>");
         out.println("</table>");
         out.println("<table>");
         out.println("<tr class=\"buttons\">");
         out.println("<td>");
         out.println("<input type=\"submit\" id=\"ok\" value=\"Ok\"/>");
         out.println("<a href='magasin'><input type=\"button\" id=\"cancel\" value=\"Annuler\" /></a>");
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
         out.println("</form>");
         out.println("</html>");
         
      }
   }
   
   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
      try (PrintWriter out = response.getWriter()) {
         boolean valide = true;
         //OracleConnection oradb = null;
         String alias = request.getParameter("alias");
         String nom = request.getParameter("nom");
         String prenom = request.getParameter("prenom");
         OracleConnection oradb = new OracleConnection();
         //String sql = ;
         
         
         //connection d'un usager
         try {
            String joueur = request.getParameter("alias");
            String quantite = request.getParameter("quant");
            String numitem = request.getParameter("numItem");
            
            if(joueur != null ) {
               //vérifier l'alias du joueur pour valider le login
               String sql = "select aliasjoueur from joueurs where aliasjoueur= '" + joueur + "'";
               oradb = new OracleConnection();
               oradb.connecter();
               
               Statement stm = oradb.getConnexion().createStatement();
               int resp = stm.executeUpdate(sql);
               
               if(resp > 0) {
                  valide = false;
               }
               else {
                  valide = true;
               }
            }
         }
         catch(SQLException ex){
            
         }
         finally {
            oradb.deconnecter();
         }
         //on inscrit le joueur si son alias n'est pas déjà utilisé
         if(valide){
            try {
               oradb.connecter();
               CallableStatement stm = oradb.getConnexion().prepareCall("{call Gestion_Magasin.INSERER_JOUEUR(?,?,?,?)}");
               stm.setString(1, alias);
               stm.setString(2, nom);
               stm.setString(3, prenom);
               stm.setInt(4, 200);
               
               stm.executeUpdate();
               
            }
            catch(SQLException ex) {
               
            }
            finally {
               if(oradb != null) {
                  oradb.deconnecter();
               }
            }
         }
         out.println("<!DOCTYPE html>");
         out.println("<html>");
         out.println("<head>");
         out.println("<title>L\'allée des marchands</title>");
         out.println("<link href=\" html/styles/register.css\" rel=\"stylesheet\" type=\"text/css\"/>");
         out.println("<script type=\"text/javascript\" src=\"html/scripts/jquery-1.11.0.min.js\"></script>");
         out.println("</head>");
         
         out.println("<form action='magasin' method='post'>");
         out.println("<body>");
         out.println("<div id=\"container\">");
         out.println("<div id=\"inscriptionPanel\">");
         out.println("<table>");
         out.println("<tr>");
         out.println("<td>");
         out.println("<div>");
         out.println("<table id=\"inscriptionForm\">");
         if(!valide){            
           
            out.println("<tr>");
            out.println("<td class=\"input\">");
            out.println("<span style='color:red'>Alias:</span></td>");
            out.println("<td><input type=\"text\" id=\"alias\" class=\"info\" name='alias'/><span style='color:red'>Cet alias est déjà utilisé!</span>");
            out.println("</td>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td class=\"input\">");
            out.println("<span>Prénom:</span></td>");
            out.println("<td><input type=\"text\" id=\"prenom\" class=\"info\" name='prenom'/>");
            out.println("</td>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td class=\"input\">");
            out.println("<span>Nom:</span></td>");
            out.println("<td><input type=\"text\" id=\"nom\" class=\"info\" name='nom'/>");
            out.println("</td>");
            out.println("</tr>");
            out.println("</table>");
            out.println("<table>");
            out.println("<tr class=\"buttons\">");
            out.println("<td>");
            out.println("<input type=\"submit\" id=\"ok\" value=\"Ok\"/>");
            out.println("<a href='magasin'><input type=\"button\" id=\"cancel\" value=\"Annuler\" /></a>");
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
            out.println("</form>");
            out.println("</html>");
         }
         else {
            out.println("<tr><td><span id='bienvenue'>Enregistrement Confirmé</span></td></tr>");
            out.println("</table>");
            out.println("<table>");
            out.println("<tr class=\"buttons\">");
            out.println("<td>");
            out.println("<a href='magasin'><input type=\"button\" id=\"cancel\" value=\"Magasiner dès maintenant!\"/></a>");
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
            out.println("</form>");
            out.println("</html>");
         }
         
         
      }
   }
   
   @Override
   public String getServletInfo() {
      return "Short description";
   }// </editor-fold>
   private void afficherPageConnection(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
      response.setContentType("text/html;charset=UTF-8");
      
      try (PrintWriter out = response.getWriter()) {
         
         OracleConnection oradb = new OracleConnection();
         String alias = request.getParameter("alias");
         String nom = request.getParameter("nom");
         String prenom = request.getParameter("prenom");
         
         String sql = "call Gestion_Magasin.INSERER_JOUEUR(?,?,?)";
         try {
            //oradb = new OracleConnection();
            CallableStatement stm = oradb.getConnexion().prepareCall(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            stm.setString(1, alias);
            stm.setString(2, nom);
            stm.setString(3, prenom);
            
         }
         catch(SQLException ex) {
            
         }
         finally {
            if(oradb != null) {
               oradb.deconnecter();
            }
         }
         out.println("<!DOCTYPE html>");
         out.println("<html>");
         out.println("<head>");
         out.println("<title>L\'allée des marchands</title>");
         out.println("<link href=\" html/styles/register.css\" rel=\"stylesheet\" type=\"text/css\"/>");
         out.println("<script type=\"text/javascript\" src=\"html/scripts/jquery-1.11.0.min.js\"></script>");
         out.println("</head>");
         
         out.println("<form action='magasin' method='post'>");
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
         out.println("<td><input type=\"text\" id=\"alias\" class=\"info\" name='alias'/>");
         out.println("</td>");
         out.println("</tr>");
         out.println("<tr>");
         out.println("<td class=\"input\">");
         out.println("<span>Prénom:</span></td>");
         out.println("<td><input type=\"text\" id=\"prenom\" class=\"info\" name='prenom'/>");
         out.println("</td>");
         out.println("</tr>");
         out.println("<tr>");
         out.println("<td class=\"input\">");
         out.println("<span>Nom:</span></td>");
         out.println("<td><input type=\"text\" id=\"nom\" class=\"info\" name='nom'/>");
         out.println("</td>");
         out.println("</tr>");
         out.println("</table>");
         out.println("<table>");
         out.println("<tr class=\"buttons\">");
         out.println("<td>");
         out.println("<input type=\"submit\" id=\"ok\" value=\"Ok\"/>");
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
         out.println("</form>");
         out.println("</html>");
         
      }
   }
}
