/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Magasin;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import oracle.jdbc.OracleTypes;

/**
 *
 * @author Charles
 */
@WebServlet(name = "Panier", urlPatterns = {"/panier"})
public class Panier extends HttpServlet {
   OracleConnection oradb = new OracleConnection();
   String id = "Visiteur";
   int total = 0;
   int montantJoueur = 0;
   int prixItem = 0;
   String nomItem = "NomItem";
   String items = "";
   String type = "";
   String numItem = "";
   String quantite = "";
   protected void processRequest(HttpServletRequest request, HttpServletResponse response, String method)
           throws ServletException, IOException {
      ArrayList list = new ArrayList();
      response.setContentType("text/html;charset=UTF-8");   
   }
   private ArrayList getItemAttributes(String type) {
      ArrayList list = new ArrayList();
      switch(type){
         case "Potion":
            list.add("");
            list.add(0);
            list.add("");
            break;
         case "Armure":
            list.add("");
            list.add("");
            list.add(0);
            break;
         case "Arme":
            list.add("");
            list.add("");
            list.add(0);
            break;
         case "Habilete":
            list.add("");
            break;
      }
      return list;
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
      
            ArrayList list = new ArrayList();
      response.setContentType("text/html;charset=UTF-8");
      try (PrintWriter out = response.getWriter()) {
         
         /////////////récupération des cookies////////////////////////
         try {
            Cookie[] cookies = request.getCookies();

            for(int i = 0; i < cookies.length; ++i) {
               Cookie c = cookies[i];
               if(c.getName().equals("alias")) {
                  id = c.getValue();
               }
               if(c.getName().equals("numItem")) {
                  numItem = c.getValue();
               }
               if(c.getName().equals("typeItem")) {
                  type = c.getValue();
               }
               if(c.getName().equals("prixItem")){
                  prixItem = Integer.parseInt(c.getValue());
               }
               if(c.getName().equals("items")){
                  items = c.getValue();
               }
               if(c.getName().equals("quantite")){
                  quantite = c.getValue();
               }
            }
            //////////////////////////////////////////////////////////////
            try {
               ///////récupération du nom de l'item sélectionné////////////////
               oradb.connecter();
               String sql = "select nom from items where numitem=" + numItem ;
               PreparedStatement stm = oradb.getConnexion().prepareStatement(sql);
               ResultSet rst = stm.executeQuery(sql);
               
               while(rst.next()) {
                  nomItem = rst.getString(1);
               }
               
            }
            catch(SQLException ex) {
               out.println(ex);
            }
            finally {
               oradb.deconnecter();
            }
            //////////////////////////////////////////////////////////////
            try {
               ///////récupération  de la facture du joueur connecté////////////////
               oradb.connecter();
               String sql = "{? = call Gestion_Magasin.CALCULER_FACTURE(?)}";
               
               CallableStatement stm = oradb.getConnexion().prepareCall(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
                       ResultSet.CONCUR_READ_ONLY);
               
               stm.registerOutParameter(1, OracleTypes.NUMBER);
               stm.setString(2, id);
               
               stm.execute();
               total = stm.getInt(1);
               
            }
            catch(SQLException ex) {
               out.println(ex);
            }
            finally {
               oradb.deconnecter();
            }
            /////////////////////////////////////////////////////////////////////
            ////////////////////////affichage des attributs d'un item particulier//////////////////////////
            String sql = "";
            if(!type.equals("")) {
               switch(type) {
                  case "Potion":
                     sql = "{? = call Gestion_Magasin.AFFICHER_POTION(?)}";
                     break;
                  case "Arme":
                     sql = "{? = call Gestion_Magasin.AFFICHER_ARME(?)}";
                     break;
                  case "Armure":
                     sql = "{? = call Gestion_Magasin.AFFICHER_ARMURE(?)}";
                     break;
                  case "Habilete":
                     sql = "{? = call Gestion_Magasin.AFFICHER_HABILETE(?)}";
                     break;
               }
               oradb.connecter();
               CallableStatement stm = oradb.getConnexion().prepareCall(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
                       ResultSet.CONCUR_READ_ONLY);
               
               stm.registerOutParameter(1, OracleTypes.CURSOR);
               stm.setInt(2, Integer.parseInt(numItem));
               
               stm.execute();
               list = getItemAttributes(type);
               ResultSet rst = (ResultSet)stm.getObject(1);
               while(rst.next()) {
                  switch(type) {
                     case "Potion":
                        list.set(0, "Effet: " + rst.getString(1));
                        list.set(1, "Durée: " + rst.getInt(2));
                        list.set(2, "Ingrédients" + rst.getString(3));
                        break;
                     case "Armure":
                        list.set(0, "Qualité: " + rst.getString(1));
                        list.set(1, "Type: " + rst.getString(2));
                        list.set(2, "Poids: " + rst.getInt(3));
                        break;
                     case "Arme":
                        list.set(0, "Qualité: " + rst.getString(1));
                        list.set(1, "Type: " + rst.getString(2));
                        list.set(2, "Dégâts: " + rst.getInt(3));
                        break;
                     case "Habilete":
                        list.set(0, "Effet:" + rst.getString(1));
                        break;
                  }
               }
            }
         }
         catch(SQLException ex) {
            
         }
         finally {
            oradb.deconnecter();
         }
         
         
         /////////////////////////affichage de l'entête HTML//////////////////////
         out.println("<!DOCTYPE html>");
         out.println("<html>");
         out.println("<head>");
         out.println("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\" />");
         out.println("<meta name=\"description\" content=\"Panier/Inventaire\"/>");
         out.println("<meta name=\"author\" content=\"Charles Hunter-Roy & Alexis Lalonde\" />");
         out.println("<title>Panier</title> ");
         out.println("<link href=\" html/styles/panier.css\" rel=\"stylesheet\" type=\"text/css\"/>");
         out.println("<script type=\"text/javascript\" src=\"html/scripts/jquery-1.11.0.min.js\"></script>");
         out.println("<script type=\"text/javascript\" src=\"html/scripts/scripts.js\"></script>");
         out.println("</head>");
         out.println("<form action='panier' method='post'>");
         /////////////////////////////////////////////////////////////////////////        

         out.println("<body>");
         out.println("<div id=\"container\">");
         out.println("<div id=\"sub\">");
         out.println("<table>");
         out.println("<tr>");
         out.println("<td>");
         out.println("<div id=\"panierJoueur\">");
         out.println("<table id=\"panierItems\">");
         out.println("<tr id=\"panierLegende\">");
         out.println("<td>Numéro</td>");
         out.println("<td>Nom</td>");
         out.println("<td>Prix</td>");
         out.println("<td>Quantité</td>");
         out.println("<td>Genre</td>");
         out.println("</tr>");
         
         ////////////////affichage de l'inventaire du joueur//////////////
         try {
            
            oradb.connecter();
            CallableStatement stm = null;
            ResultSet rst = null;
            String sql = "";
            sql = "{? = call Gestion_Magasin.LISTER_PANIER(?)}";
            stm = oradb.getConnexion().prepareCall(sql, ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            
            stm.registerOutParameter(1, OracleTypes.CURSOR);
            stm.setString(2, id);
            stm.execute();
            rst = (ResultSet)stm.getObject(1);
            
            while(rst.next()) {
               int num = rst.getInt("NUMITEM");
               String nom = rst.getString("NOM");
               String genre = rst.getString("GENRE");
               int prix = rst.getInt("PRIX");
               int qte = rst.getInt("QUANTITE");
               out.println("<tr>");
               out.println("<td>" + num + "</td>");
               out.println("<td>" + nom +"</td>");
               out.println("<td>"+ prix +"</td>");
               out.println("<td>" + qte + "</td>");
               out.println("<td>" + genre + "</td>");
               out.println("</tr>");
            }
         }
         catch(SQLException ex) {
            
         }
         finally {
            oradb.deconnecter();
         }
         /////////////////////////////////////////////////////////////////////
         
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
         out.println("<td>" + nomItem + "</td>");
         out.println("<td><ul id='attributs'>");
         for(int i = 0; i < list.size(); ++i)
         {
            out.println("<li>" + list.get(i) + "</li>");
         }
         out.println("</ul></td>");
         out.println("</tr>");
         out.println("<tr id=\"info\">");
         out.println("<td>Prix:<span>" + prixItem + "</span></td>");
         out.println("<td>Qte:<input type=\"number\" id=\"inputQuantite\"/></td>");
         out.println("</tr>");
         out.println("<tr>");
         out.println("<td colspan=\"2\">");
         out.println("<input type=\"submit\" id=\"update\" value=\"Mettre à jour\"/>");
         out.println("</td>");
         out.println("</tr>");
         out.println("</table>");
         out.println("</div>");
         out.println("</td>");
         out.println("</tr>");
         out.println("<tr>");
         out.println("<td>");
         out.println("<div id=\"inventaireJoueur\">");
         out.println("<table id='inventaire'>");
         out.println("<tr id=\"inventLegende\">");
         out.println("<td>Numéro</td>");
         out.println("<td>Nom</td>");
         out.println("<td>Prix</td>");
         out.println("<td>Quantité</td>");
         out.println("<td>Genre</td>");
         out.println("</tr>");
         ////////////////affichage de l'inventaire du joueur//////////////
         try {
            
            oradb.connecter();
            CallableStatement stm = null;
            ResultSet rst = null;
            String sql = "";
            sql = "{? = call Gestion_Magasin.LISTER_INVENTAIRE(?)}";
            stm = oradb.getConnexion().prepareCall(sql, ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            
            stm.registerOutParameter(1, OracleTypes.CURSOR);
            stm.setString(2, id);
            stm.execute();
            rst = (ResultSet)stm.getObject(1);
            
            while(rst.next()) {
               int num = rst.getInt("NUMITEM");
               String nom = rst.getString("NOM");
               String genre = rst.getString("GENRE");
               int prix = rst.getInt("PRIX");
               int qte = rst.getInt("QUANTITE");
               out.println("<tr>");
               out.println("<td>" + num + "</td>");
               out.println("<td>" + nom +"</td>");
               out.println("<td>"+ prix +"</td>");
               out.println("<td>" + qte + "</td>");
               out.println("<td>" + genre + "</td>");
               out.println("</tr>");
            }
         }
         catch(SQLException ex) {
            
         }
         finally {
            oradb.deconnecter();
         }
         /////////////////////////////////////////////////////////////////////
         
         out.println("</table>");
         out.println("</div>");
         out.println("</td>");
         out.println("<td>");
         out.println("<div id=\"paiementOptions\">");
         out.println("<table>");
         out.println("<tr>");
         out.println("<td>Joueur:<span id=\"ID\">" + id + "</span></td>");
         out.println("</tr>");
         out.println("<tr>");
         out.println("<td>Total:<span id=\"totalJoueur\">" + total + "</span>$</td>");
         out.println("</tr>");
         out.println("<tr>");
         out.println("<td>Montant:<span id=\"montant\">" + montantJoueur + "</span>$</td>");
         out.println("</tr>");
         out.println("<tr>");
         out.println("<td><input type=\"submit\" id=\"confirm\" value=\"Confirmer le paiement\"/></td>");
         out.println("<td><a href='magasin'><input type=\"button\" id=\"confirm\" value=\"Annuler\"/></a></td>");
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
      
      ArrayList list = new ArrayList();
      response.setContentType("text/html;charset=UTF-8");
      try (PrintWriter out = response.getWriter()) {
         
         /////////////récupération des cookies////////////////////////
         try {
            Cookie[] cookies = request.getCookies();
            String type = "";
            String numItem = "";
            for(int i = 0; i < cookies.length; ++i) {
               Cookie c = cookies[i];
               if(c.getName().equals("alias")) {
                  id = c.getValue();
               }
               if(c.getName().equals("numItem")) {
                  numItem = c.getValue();
               }
               if(c.getName().equals("typeItem")) {
                  type = c.getValue();
               }
               if(c.getName().equals("prixItem")){
                  prixItem = Integer.parseInt(c.getValue());
               }
               if(c.getName().equals("items")){
                  items = c.getValue();
               }
            }
            //////////////////////////////////////////////////////////////
            try {
               ///////récupération du montant du joueur connecté////////////////
               oradb.connecter();
               String sql = "select montant from joueurs where aliasjoueur='" + id + "'";
               PreparedStatement stm = oradb.getConnexion().prepareStatement(sql);
               ResultSet rst = stm.executeQuery(sql);
               
               while(rst.next()) {
                  montantJoueur = rst.getInt(1);
               }
               
            }
            catch(SQLException ex) {
               out.println(ex);
            }
            finally {
               oradb.deconnecter();
            }
            //////////////////////////////////////////////////////////////
            try {
               ///////récupérationde la facture du joueur connecté////////////////
               oradb.connecter();
               String sql = "{? = call Gestion_Magasin.CALCULER_FACTURE(?)}";
               
               CallableStatement stm = oradb.getConnexion().prepareCall(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
                       ResultSet.CONCUR_READ_ONLY);
               
               stm.registerOutParameter(1, OracleTypes.NUMBER);
               stm.setString(2, id);
               
               stm.execute();
               total = stm.getInt(1);
               
            }
            catch(SQLException ex) {
               out.println(ex);
            }
            finally {
               oradb.deconnecter();
            }
            /////////////////////////////////////////////////////////////////////
            ////////////////////////affichage des attributs d'un item particulier//////////////////////////
            String sql = "";
            if(!type.equals("")) {
               switch(type) {
                  case "Potion":
                     sql = "{? = call Gestion_Magasin.AFFICHER_POTION(?)}";
                     break;
                  case "Arme":
                     sql = "{? = call Gestion_Magasin.AFFICHER_ARME(?)}";
                     break;
                  case "Armure":
                     sql = "{? = call Gestion_Magasin.AFFICHER_ARMURE(?)}";
                     break;
                  case "Habilete":
                     sql = "{? = call Gestion_Magasin.AFFICHER_HABILETE(?)}";
                     break;
               }
               oradb.connecter();
               CallableStatement stm = oradb.getConnexion().prepareCall(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
                       ResultSet.CONCUR_READ_ONLY);
               
               stm.registerOutParameter(1, OracleTypes.CURSOR);
               stm.setInt(2, Integer.parseInt(numItem));
               
               stm.execute();
               list = getItemAttributes(type);
               ResultSet rst = (ResultSet)stm.getObject(1);
               while(rst.next()) {
                  switch(type) {
                     case "Potion":
                        list.set(0, "Effet: " + rst.getString(1));
                        list.set(1, "Durée: " + rst.getInt(2));
                        list.set(2, "Ingrédients" + rst.getString(3));
                        break;
                     case "Armure":
                        list.set(0, "Qualité: " + rst.getString(1));
                        list.set(1, "Type: " + rst.getString(2));
                        list.set(2, "Poids: " + rst.getInt(3));
                        break;
                     case "Arme":
                        list.set(0, "Qualité: " + rst.getString(1));
                        list.set(1, "Type: " + rst.getString(2));
                        list.set(2, "Dégâts: " + rst.getInt(3));
                        break;
                     case "Habilete":
                        list.set(0, "Effet:" + rst.getString(1));
                        break;
                  }
               }
            }
         }
         catch(SQLException ex) {
            
         }
         finally {
            oradb.deconnecter();
         }        
         
         /////////////////////////affichage de l'entête HTML//////////////////////
         out.println("<!DOCTYPE html>");
         out.println("<html>");
         out.println("<head>");
         out.println("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\" />");
         out.println("<meta name=\"description\" content=\"Panier/Inventaire\"/>");
         out.println("<meta name=\"author\" content=\"Charles Hunter-Roy & Alexis Lalonde\" />");
         out.println("<title>Panier</title> ");
         out.println("<link href=\" html/styles/panier.css\" rel=\"stylesheet\" type=\"text/css\"/>");
         out.println("<script type=\"text/javascript\" src=\"html/scripts/jquery-1.11.0.min.js\"></script>");
         out.println("<script type=\"text/javascript\" src=\"html/scripts/scripts.js\"></script>");
         out.println("</head>");
         out.println("<form action='panier' method='post'>");
         /////////////////////////////////////////////////////////////////////////        

         out.println("<body>");
         out.println("<div id=\"container\">");
         out.println("<div id=\"sub\">");
         out.println("<table>");
         out.println("<tr>");
         out.println("<td>");
         out.println("<div id=\"panierJoueur\">");
         out.println("<table id=\"panierItems\">");
         out.println("<tr id=\"panierLegende\">");
         out.println("<td>Numéro</td>");
         out.println("<td>Nom</td>");
         out.println("<td>Prix</td>");
         out.println("<td>Quantité</td>");
         out.println("<td>Genre</td>");
         out.println("</tr>");
         
         ////////////////affichage de l'inventaire du joueur//////////////
         try {
            
            oradb.connecter();
            CallableStatement stm = null;
            ResultSet rst = null;
            String sql = "";
            sql = "{? = call Gestion_Magasin.LISTER_PANIER(?)}";
            stm = oradb.getConnexion().prepareCall(sql, ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            
            stm.registerOutParameter(1, OracleTypes.CURSOR);
            stm.setString(2, id);
            stm.execute();
            rst = (ResultSet)stm.getObject(1);
            
            while(rst.next()) {
               int num = rst.getInt("NUMITEM");
               String nom = rst.getString("NOM");
               String genre = rst.getString("GENRE");
               int prix = rst.getInt("PRIX");
               int qte = rst.getInt("QUANTITE");
               out.println("<tr>");
               out.println("<td>" + num + "</td>");
               out.println("<td>" + nom +"</td>");
               out.println("<td>"+ prix +"</td>");
               out.println("<td>" + qte + "</td>");
               out.println("<td>" + genre + "</td>");
               out.println("</tr>");
            }
         }
         catch(SQLException ex) {
            
         }
         finally {
            oradb.deconnecter();
         }
         /////////////////////////////////////////////////////////////////////
         
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
         out.println("<td>" + nomItem + "</td>");
         out.println("<td><ul id='attributs'>");
         for(int i = 0; i < list.size(); ++i)
         {
            out.println("<li>" + list.get(i) + "</li>");
         }
         out.println("</ul></td>");
         out.println("</tr>");
         out.println("<tr id=\"info\">");
         out.println("<td>Prix:<span id='inputTotal'>" + prixItem + "</span></td>");
         out.println("<td>Qte:<input type=\"number\" id=\"inputQuantite\"/></td>");
         out.println("</tr>");
         out.println("<tr>");
         out.println("<td colspan=\"2\">");
         out.println("<input type=\"submit\" id=\"update\" value=\"Mettre à jour\"/>");
         out.println("</td>");
         out.println("</tr>");
         out.println("</table>");
         out.println("</div>");
         out.println("</td>");
         out.println("</tr>");
         out.println("<tr>");
         out.println("<td>");
         out.println("<div id=\"inventaireJoueur\">");
         out.println("<table id='inventaire'>");
         out.println("<tr id=\"inventLegende\">");
         out.println("<td>Numéro</td>");
         out.println("<td>Nom</td>");
         out.println("<td>Prix</td>");
         out.println("<td>Quantité</td>");
         out.println("<td>Genre</td>");
         out.println("</tr>");
         ////////////////affichage de l'inventaire du joueur//////////////
         try {
            
            oradb.connecter();
            CallableStatement stm = null;
            ResultSet rst = null;
            String sql = "";
            sql = "{? = call Gestion_Magasin.LISTER_INVENTAIRE(?)}";
            stm = oradb.getConnexion().prepareCall(sql, ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            
            stm.registerOutParameter(1, OracleTypes.CURSOR);
            stm.setString(2, id);
            stm.execute();
            rst = (ResultSet)stm.getObject(1);
            
            while(rst.next()) {
               int num = rst.getInt("NUMITEM");
               String nom = rst.getString("NOM");
               String genre = rst.getString("GENRE");
               int prix = rst.getInt("PRIX");
               int qte = rst.getInt("QUANTITE");
               out.println("<tr>");
               out.println("<td>" + num + "</td>");
               out.println("<td>" + nom +"</td>");
               out.println("<td>"+ prix +"</td>");
               out.println("<td>" + qte + "</td>");
               out.println("<td>" + genre + "</td>");
               out.println("</tr>");
            }
         }
         catch(SQLException ex) {
            
         }
         finally {
            oradb.deconnecter();
         }
         /////////////////////////////////////////////////////////////////////
         
         out.println("</table>");
         out.println("</div>");
         out.println("</td>");
         out.println("<td>");
         out.println("<div id=\"paiementOptions\">");
         out.println("<table>");
         out.println("<tr>");
         out.println("<td>Joueur:<span id=\"ID\">" + id + "</span></td>");
         out.println("</tr>");
         out.println("<tr>");
         out.println("<td>Total:<span id=\"totalJoueur\">" + total + "</span>$</td>");
         out.println("</tr>");
         out.println("<tr>");
         out.println("<td>Montant:<span id=\"montant\">" + montantJoueur + "</span>$</td>");
         out.println("</tr>");
         out.println("<tr>");
         out.println("<td><input type=\"submit\" id=\"confirm\" value=\"Confirmer le paiement\"/></td>");
         out.println("<td><a href='magasin'><input type=\"button\" id=\"confirm\" value=\"Annuler\"/></a></td>");
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
      /////////// Mise à jour du panier /////////////////////////////
         try {
         ////////////////////////Paiement et récupération du panier////////////////////////
         ArrayList listItems = new ArrayList();
         String split[] = items.split("-");
         String[] splitList;
         for(int i = 0; i < split.length; ++i){
            splitList = split[i].split("/");
            listItems.add(splitList[0]);
            listItems.add(splitList[1]);
         }
         
         oradb.connecter();
         String sql = "{call Gestion_Magasin.INSERER_INVENTAIRE(?, ?, ?)}";
         
         for(int i = 0; i < listItems.size()-1; i+=2){
            CallableStatement stm = oradb.getConnexion().prepareCall(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            stm.setString(1, id);
            stm.setInt(2, Integer.parseInt(listItems.get(i).toString()));
            stm.setInt(3, Integer.parseInt(listItems.get(i+1).toString()));
            stm.execute();
         }
      }
      catch(SQLException ex) {
         
      }
      finally {
         oradb.deconnecter();
      }
      ////////////////////////////////////////////////////////////////////////////////
      try {
         ////////////////////////Modification de la quantité d'un item dans le panier////////////////////////

         
         oradb.connecter();
         String sql = "{call Gestion_Magasin.MODIFIER_PANIER(?, ?, ?)}";
            CallableStatement stm = oradb.getConnexion().prepareCall(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            
            stm.setInt(1, Integer.parseInt(quantite));
            stm.setString(2, id);
            stm.setInt(3, Integer.parseInt(numItem));
            
            stm.execute();
      }
      catch(SQLException ex) {
         
      }
      finally {
         oradb.deconnecter();
      }
      ////////////////////////////////////////////////////////////////////////////////
 
   }
   @Override
   public String getServletInfo() {
      return "Short description";
   }// </editor-fold>
   
}
