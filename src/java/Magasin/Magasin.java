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
import java.sql.Statement;
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
@WebServlet(name = "Magasin", urlPatterns = {"/magasin"})
public class Magasin extends HttpServlet {
   
   
   protected void processRequest(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
      response.setContentType("text/html;charset=UTF-8");
      
      try (PrintWriter out = response.getWriter()) {
         //afficherEntete(request, response);
         
      }
   }
//   private void afficherEntete(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
//      try  {
//         out.println("<!DOCTYPE html>");
//         out.println("<html>");
//         out.println("<head>");
//         out.println("<meta name='description' content='Page Principale'/>");
//         out.println("<meta name='author' content='Charles Hunter-Roy & Alexis Lalonde' />");
//         out.println("<title>L\'allée des marchands</title>");
//         out.println("<link href=' html/styles/main.css' rel='stylesheet' type='text/css'/>");
//         out.println("<script type='text/javascript' src='html/scripts/jquery-1.11.0.min.js'></script>");
//         out.println("<script type='text/javascript' src='html/scripts/scripts.js'></script>");
//         out.println("</head>");
//
//      }
//   }
   
   @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
      //processRequest(request, response);
      response.setContentType("text/html;charset=UTF-8");
      ArrayList list = new ArrayList();
      try (PrintWriter out = response.getWriter()) {
         //afficherEntete(request, response);
         OracleConnection oradb = new OracleConnection();
         String id = "Visiteur";
         int total = 0;
         
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
            }
            //////////////////////////////////////////////////////////////
            try {
               ///////récupération du montant du joueur connecté////////////////
               oradb.connecter();
               String sql = "select montant from joueurs where aliasjoueur='" + id + "'";
               PreparedStatement stm = oradb.getConnexion().prepareStatement(sql);
               ResultSet rst = stm.executeQuery(sql);
               
               while(rst.next()) {
                  total = rst.getInt(1);
               }
               
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
         /////////////////////////////////////////////////////////////////////////////////
         
         //connexion d'un usager
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
                  Cookie alias = new Cookie("alias", joueur );
                  alias.setMaxAge(60 * 60 * 60);
                  response.addCookie(alias);
               }
               else {
                  //usager invalide
               }
            }
            if(quantite != null ) {
               Cookie qte = new Cookie("quantite", quantite);
               qte.setMaxAge(60*60);
               response.addCookie(qte);
            }
            if(numitem != null) {
               Cookie num = new Cookie("numItem", numitem);
               num.setMaxAge(60*60);
               response.addCookie(num);
            }
         }
         catch(SQLException ex){
            
         }
         finally {
            oradb.deconnecter();
         }
         
         ///////////////////////////affichage de l'entête/////////////////////////////////
         out.println("<!DOCTYPE html>");
         out.println("<html>");
         out.println("<head>");
         out.println("<meta name='description' content='Page Principale'/>");
         out.println("<meta name='author' content='Charles Hunter-Roy & Alexis Lalonde' />");
         out.println("<title>L\'allée des marchands</title>");
         out.println("<link href='html/styles/main.css' rel='stylesheet' type='text/css'/>");
         out.println("<script type='text/javascript' src='html/scripts/jquery-1.11.0.min.js'></script>");
         out.println("<script type='text/javascript' src='html/scripts/scripts.js'></script>");
         out.println("</head>");
         //////////////////////////////////////////////////////////////
         /////////////////////////affichage du body////////////////////
         out.println("<body id='body'>");
         out.println("<form action='magasin' method='post'>");
         out.println("<table id='background'>");
         out.println("<tr>");
         out.println("<td>");
         out.println("<table id='container'>");
         out.println("<tr>");
         out.println("<td>");
         out.println("<div id='headerMenu'>");
         out.println("<table id='options'>");
         out.println("<tr>");
         out.println("<td>");
         out.println("<form action='recherche' method='post'><div id='recherche'>Recherche: <input type='text' name='recherche'> <input type='submit' id='loupe'></div></form>");
         out.println("</td>");
         out.println("<td> <span id='ID'>" + id + "</span>");
         out.println("<input type='button' id='connection' value='Connexion'>");
         out.println("</td>");
         out.println("<td>");
         out.println("<a href='panier'><div id='panier'></div></a>");
         out.println("</td>");
         out.println("<td>");
         out.println("<span id ='totalJoueur'>" + total +"Écus</span>");
         out.println("</td>");
         out.println("</td>");
         out.println("</tr>");
         out.println("</table>");
         out.println("</div>");
         out.println(" </td>");
         out.println("</tr>");
         out.println("<tr>");
         out.println("<td>");
         out.println("<div id='bodyMenu'>");
         out.println("<div id='sideMenu'>");
         out.println("<div id='categories'><div class='head'>Catégories</div>");
         out.println("<select name='categoriesItems' id='categoriesItems'>");
         out.println("<option name='Tous' value='Tous'>Tous</option>");
         out.println("<option name='Arme' value='Arme'>Arme</option>");
         out.println("<option name='Habileté' value='Habileté'>Habilete</option>");
         out.println("<option name='Armure' value='Armure'>Armure</option>");
         out.println("<option name='Potion' value='Potion'>Potion</option>");
         out.println("</select>");
         out.println("<input type='submit'value='Filtrer'>");
         out.println("</div>");
         out.println("<div id='selectedItem'>");
         out.println("<div class='head'>Item</div>");
         out.println("<table id='statistiques'>");
         out.println("<tr>");
         out.println("<td>Numéro:<input type='number' id='numeroItem' name='numItem'></td>");
         out.println("<td></td>");
         out.println("</tr>");
         out.println("<tr>");
         out.println("<td>Nom:</td>");
         out.println("<td></td>");
         out.println("</tr>");
         out.println("<tr>");
         out.println("<td>Genre:</td>");
         out.println("<td></td>");
         out.println("</tr>");
         out.println("<tr>");
         out.println("<td>");
         out.println("<ul id='attributs'>");
         for(int i = 0; i < list.size(); ++i)
         {
            out.println("<li>" + list.get(i) + "</li>");
         }
         out.println("</ul>");
         out.println("</td>");
         out.println("</tr>");
         out.println("</table>");
         out.println("<div id='commande'>");
         out.println("<div id='total'>Prix:<span id='inputTotal'>0</span></div>");
         out.println("<div id='quantite'>Quantité: <input type='number' min='1' id='inputQuantite' name='quant'></div>");
         out.println("<div><input type='submit' value='Ajouter au panier' id='ajouter'></div>");
         out.println("</div>");
         out.println("</div>");
         out.println("</div>");
         out.println("<div id='shopBrowsing'>");
         out.println("<table id='shop'>");
         out.println("<tr>");
         out.println("<td>");
         out.println("<table id='legende'>");
         out.println("<tr>");
         out.println("<td>Numéro</td>");
         out.println("<td>Nom</td>");
         out.println("<td>Prix</td>");
         out.println("<td>Quantité</td>");
         out.println("<td>Genre</td>");
         out.println("</tr>");
         out.println("</table>");
         out.println("</td>");
         out.println("</tr>");
         out.println("<tr>");
         out.println("<td>");
         out.println("<table id='items'>");
         
         String filtre = (String)request.getParameter("categoriesItems");
         String search = (String)request.getParameter("recherche");
         ////////////////affichage du catalogue d'items//////////////
         try {
            
            oradb.connecter();
            CallableStatement stm = null;
            ResultSet rst = null;
            String sql = "";
            ////////si on recherche par catégories////////////////
            if(search == null || search.equals("")) {
               if(filtre == null || filtre.equals("Tous")) {
                  sql = "{? = call Gestion_Magasin.LISTER_TOUS_ITEMS(?, ?, ?, ?)}";
               }
               else {
                  sql = "{? = call Gestion_Magasin.LISTER_ITEMS(?)}";
               }
               stm = oradb.getConnexion().prepareCall(sql, ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
               
               stm.registerOutParameter(1, OracleTypes.CURSOR);
               if(filtre == null || filtre.equals("Tous")) {
                  stm.setString(2, "Arme");
                  stm.setString(3, "Armure");
                  stm.setString(4, "Potion");
                  stm.setString(5, "Habilete");
               }
               else {
                  stm.setString(2,filtre);
               }
               stm.execute();
               rst = (ResultSet)stm.getObject(1);
               
            }
            else{
               stm = oradb.getConnexion().prepareCall("{call GESTION_MAGASIN.RECHERCHER_TOUS_ITEMs(?,?,?,?,?,?)}",ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
               stm.setString(1, search);
               stm.setString(2, "Arme");
               stm.setString(3, "Armure");
               stm.setString(4, "Habilete");
               stm.setString(5, "Potion");
               stm.registerOutParameter(6, OracleTypes.CURSOR);
               stm.execute();
               rst = (ResultSet)stm.getObject(6);
            }            
            
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
         out.println("</td>");
         out.println("</tr>");
         out.println("</table>");
         out.println("</div>");
         out.println("</div>");
         out.println("</td>");
         out.println("</tr>");
         out.println("<tr>");
         out.println("<td>");
         out.println("<div id='footer'></div>");
         out.println("</td>");
         out.println("</tr>");
         out.println("</table>");
         out.println("</td>");
         out.println("</tr>");
         out.println("</table>");
         out.println("</form>");
         out.println("</body>");
         out.println("</html>");
         
      }
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
   
   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
      //processRequest(request, response);
      response.setContentType("text/html;charset=UTF-8");
      
      try (PrintWriter out = response.getWriter()) {
         
         OracleConnection oradb  = new OracleConnection();
         String id = "Visiteur";
         int total = 0;
         try {
            oradb.connecter();
            int qte = 0;
            int num = 0;
            
            
            Cookie[] cookies = request.getCookies();
            for(int i = 0; i < cookies.length; ++i) {
               Cookie c = cookies[i];
               if(c.getName().equals("alias")) {
                  id = c.getValue();
               }
               if(c.getName().equals("quantite"))
               {
                  String value = c.getValue();
                  if(!value.equals(""))
                     qte = Integer.parseInt(value);
               }
               if(c.getName().equals("numItem"))
               {
                  num = Integer.parseInt(c.getValue());
               }
            }
            try {

               String sql = "select montant from joueurs where aliasjoueur='" + id + "'";
               PreparedStatement stm = oradb.getConnexion().prepareStatement(sql);
               ResultSet rst = stm.executeQuery(sql);
               
               while(rst.next()) {
                  total = rst.getInt(1);
               }
               
            }
            catch(SQLException ex) {
               out.println(ex);
            }
            finally {
               oradb.deconnecter();
            }
            ////////////////////////ajout d'un item au panier////////////////
            if(qte > 0){
               oradb.connecter();
               CallableStatement stm = oradb.getConnexion().prepareCall("{call Gestion_Magasin.Inserer_Panier(?, ?, ?)}",
                     ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            
               stm.setInt(1, qte);
               stm.setString(2, id);
               stm.setInt(3, num);
            
               stm.execute();
            }
            ////////////////////////////////////////////////////////////////////
         }
         catch(SQLException ex) {
            
         }
         finally {
            oradb.deconnecter();
         }
         
         out.println("<!DOCTYPE html>");
         out.println("<html>");
         out.println("<head>");
         out.println("<meta name='description' content='Page Principale'/>");
         out.println("<meta name='author' content='Charles Hunter-Roy & Alexis Lalonde' />");
         out.println("<title>L\'allée des marchands</title>");
         out.println("<link href='html/styles/main.css' rel='stylesheet' type='text/css'/>");
         out.println("<script type='text/javascript' src='html/scripts/jquery-1.11.0.min.js'></script>");
         out.println("<script type='text/javascript' src='html/scripts/scripts.js'></script>");
         out.println("</head>");
         
         out.println("<body>");
         out.println("<form action='magasin' method='get'>");
         out.println("<table id='background'>");
         out.println("<tr>");
         out.println("<td>");
         out.println("<table id='container'>");
         out.println("<tr>");
         out.println("<td>");
         out.println("<div id='headerMenu'>");
         out.println("<table id='options'>");
         out.println("<tr>");
         out.println("<td>");
         out.println("<form action='recherche' method='post'><div id='recherche'>Recherche: <input type='text' name='recherche'> <input type='submit' id='loupe'></div></form>");
         out.println("</td>");
         out.println("<td>");
         out.println("<input type='button' id='connection' value='Connexion'>");
         out.println("<td> <span id='ID'>" + id + "</span>");
         out.println("<td>");
         out.println("<a href='panier'><div id='panier'></div></a>");
         out.println("</td>");
         out.println("<td>");
         out.println("<span id ='totalJoueur'>" + total + " Écus</span>");
         out.println("</td>");
         out.println("</td>");
         out.println("</tr>");
         out.println("</table>");
         out.println("</div>");
         out.println(" </td>");
         out.println("</tr>");
         out.println("<tr>");
         out.println("<td>");
         out.println("<div id='bodyMenu'>");
         out.println("<div id='sideMenu'>");
         out.println("<div id='categories'><div class='head'>Catégories</div>");
         out.println("<select name='categoriesItems' id='categoriesItems'>");
         out.println("<option name='Tous' value='Tous'>Tous</option>");
         out.println("<option name='Arme'value='Arme'>Arme</option>");
         out.println("<option name='Habilete' value='Habilete'>Habilete</option>");
         out.println("<option name='Armure' value='Armure'>Armure</option>");
         out.println("<option name='Potion' value='Potion'>Potion</option>");
         out.println("</select>");
         out.println("<input type='submit'value='Filtrer'>");
         out.println("</div>");
         out.println("<div id='selectedItem'>");
         out.println("<div class='head'>Item</div>");
         out.println("<table id='statistiques'>");
         out.println("<tr>");
         out.println("<td>Numéro:<input type='number' id='numeroItem' name='numItem'></td>");
         out.println("<td></td>");
         out.println("</tr>");
         out.println("<tr>");
         out.println("<td>Nom:</td>");
         out.println("<td></td>");
         out.println("</tr>");
         out.println("<tr>");
         out.println("<td>Genre:</td>");
         out.println("<td></td>");
         out.println("</tr>");
         out.println("<tr>");
         out.println("<td>");
         out.println("<ul id='attributs'>");
         out.println("<li>Attribut1</li>");
         out.println("<li>Attribut2</li>");
         out.println("<li>Attribut3</li>");
         out.println("</ul>");
         out.println("</td>");
         out.println("</tr>");
         out.println("</table>");
         out.println("<div id='commande'>");
         out.println("<div id='quantite'>Quantité: <input type='number' id='inputQuantite' name='quant'></div>");
         out.println("<div id='total'>Total:<span id='inputTotal'>0</span></div>");
         
         
         out.println("<div><input type='submit' value='Ajouter au panier' id='ajouter'></div>");
         out.println("</div>");
         out.println("</div>");
         out.println("</div>");
         out.println("<div id='shopBrowsing'>");
         out.println("<table id='shop'>");
         out.println("<tr>");
         out.println("<td>");
         out.println("<table id='legende'>");
         out.println("<tr>");
         out.println("<td>Numéro</td>");
         out.println("<td>Nom</td>");
         out.println("<td>Prix</td>");
         out.println("<td>Quantité</td>");
         out.println("<td>Genre</td>");
         out.println("</tr>");
         out.println("</table>");
         out.println("</td>");
         out.println("</tr>");
         out.println("<tr>");
         out.println("<td>");
         out.println("<table id='items'>");
         
         String filtre = (String)request.getParameter("categoriesItems");
         String search = (String)request.getParameter("recherche");
         try {
            oradb = new OracleConnection();
            oradb.connecter();
            String sql = "";
            CallableStatement stm = null;
            ResultSet rst = null;
            
            if(search.equals("") || search == null){
               if(filtre == null || filtre.equals("Tous")) {
                  sql = "{? = call Gestion_Magasin.LISTER_TOUS_ITEMS(?, ?, ?, ?)}";
               }
               else {
                  sql = "{? = call Gestion_Magasin.LISTER_ITEMS(?)}";
               }
               stm = oradb.getConnexion().prepareCall(sql, ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
               stm.registerOutParameter(1, OracleTypes.CURSOR);
               
               if(filtre == null || filtre.equals("Tous")) {
                  stm.setString(2, "Arme");
                  stm.setString(3, "Armure");
                  stm.setString(4, "Potion");
                  stm.setString(5, "Habilete");
               }
               else {
                  stm.setString(2,filtre);
               }
               stm.execute();               
               rst = (ResultSet)stm.getObject(1);
               
            }
            else {               
               stm = oradb.getConnexion().prepareCall("{call GESTION_MAGASIN.RECHERCHER_TOUS_ITEMs(?,?,?,?,?,?)}",ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
               stm.setString(1, search);
               stm.setString(2, "Arme");
               stm.setString(3, "Armure");
               stm.setString(4, "Habilete");
               stm.setString(5, "Potion");
               stm.registerOutParameter(6, OracleTypes.CURSOR);               
               stm.execute();               
               rst = (ResultSet)stm.getObject(6);
            }
            //affichage des résultats//
            while(rst.next()) {
               int num = rst.getInt("NUMITEM");
               String nom = rst.getString("NOM");
               String genre = rst.getString("GENRE");
               int prix = rst.getInt("PRIX");
               int qte = rst.getInt("QUANTITE");
               out.println("<tr>");
               out.println("<td>" + num + "</td>");
               out.println("<td>" + nom + "</td>");
               out.println("<td>" + prix + "</td>");
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
         out.println("</table>");
         out.println("</td>");
         out.println("</tr>");
         out.println("</table>");
         out.println("</div>");
         out.println("</div>");
         out.println("</td>");
         out.println("</tr>");
         out.println("<tr>");
         out.println("<td>");
         out.println("<div id='footer'></div>");
         out.println("</td>");
         out.println("</tr>");
         out.println("</table>");
         out.println("</td>");
         out.println("</tr>");
         out.println("</table>");
         out.println("</form>");
         
         out.println("</body>");
         out.println("</html>");
      }
   }
   
   @Override
   public String getServletInfo() {
      return "Short description";
   }// </editor-fold>
   
}
