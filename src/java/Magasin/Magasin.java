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
        
        try (PrintWriter out = response.getWriter()) {
            //afficherEntete(request, response);
            OracleConnection oradb = null;
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
            out.println("<div id='recherche'>Recherche: <input type='text'> <div id='loupe'></div></div>");
            out.println("</td>");
            out.println("<td>");
            out.println("<input type='button' id='connection' value='Connection'>");
            out.println("</td>");
            out.println("<td>");
            out.println("<div id='panier'></div>");
            out.println("</td>");
            out.println("<td>");
            out.println("<span id ='total'>0 Écus</span>");
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
            out.println("<option value='tous'>Tous</option>");
            out.println("<option value='armes'>Arme</option>");
            out.println("<option value='habilites'>Habileté</option>");
            out.println("<option value='armure'>Armure</option>");
            out.println("<option value='potions'>Potion</option>");
            out.println("</select>");
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
            try {
                oradb = new OracleConnection();
                oradb.connecter();
                
                CallableStatement stm = oradb.getConnexion().prepareCall("{? = call Gestion_Magasin.LISTER_TOUS_ITEMS(?, ?, ?, ?)}",
                        ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
                
                stm.registerOutParameter(1, OracleTypes.CURSOR);
                
                stm.setString(2, "Arme");
                stm.setString(3, "Armure");
                stm.setString(4, "Potion");
                stm.setString(5, "Habilete");
                
                stm.execute();
                
                ResultSet rst = (ResultSet)stm.getObject(1);
                
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
                out.println(ex);
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
                    String resp = "";
                    if(stm.execute(sql)) {
                        Cookie alias = new Cookie("alias", joueur );
                        alias.setMaxAge(60*60);
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
        }
    }
    
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request, response);
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            OracleConnection oradb = null;
            
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
            out.println("<div id='recherche'>Recherche: <input type='text'> <div id='loupe'></div></div>");
            out.println("</td>");
            out.println("<td>");
            out.println("<input type='button' id='connection' value='Connection'>");
            out.println("</td>");
            out.println("<td>");
            out.println("<div id='panier'></div>");
            out.println("</td>");
            out.println("<td>");
            out.println("<span id ='total'>0 Écus</span>");
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
            out.println("<option value='tous'>Tous</option>");
            out.println("<option value='armes'>Arme</option>");
            out.println("<option value='habilites'>Habilete</option>");
            out.println("<option value='armure'>Armure</option>");
            out.println("<option value='potions'>Potion</option>");
            out.println("</select>");
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
            out.println("</ul>");
            out.println("</td>");
            out.println("</tr>");
            out.println("</table>");
            out.println("<div id='commande'>");
            out.println("<div id='quantite'>Quantité: <input type='number' id='inputQuantite' name='quant'></div>");
            out.println("<div id='total'>Total:<span id='inputTotal'>0</span></div>");
            
            try {
                oradb = new OracleConnection();
                oradb.connecter();
                int qte = 0;
                int num = 0;
                
                String id = "Visiteur";
                Cookie[] cookies = request.getCookies();
                for(int i = 0; i < cookies.length; ++i) {
                    Cookie c = cookies[i];
                    if(c.getName().equals("alias")) {
                        id = c.getValue();
                    }
                    if(c.getName().equals("quantite"))
                    {
                        qte = Integer.parseInt(c.getValue());
                    }
                    if(c.getName().equals("numItem"))
                    {
                        num = Integer.parseInt(c.getValue());
                    }
                }
                CallableStatement stm = oradb.getConnexion().prepareCall("{call Gestion_Magasin.Inserer_Panier(?, ?, ?)}",
                        ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
                
                stm.setInt(1, qte);
                stm.setString(2, id);
                stm.setInt(3, num);
                
                stm.execute();
            }
            catch(SQLException ex) {
                
            }
            finally {
                if(oradb != null)
                    oradb.deconnecter();
            }
            
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
            
            try {
                oradb = new OracleConnection();
                oradb.connecter();
                
                CallableStatement stm = oradb.getConnexion().prepareCall("{? = call Gestion_Magasin.LISTER_TOUS_ITEMS(?, ?, ?, ?)}",
                        ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
                
                stm.registerOutParameter(1, OracleTypes.CURSOR);
                
                stm.setString(2, "Arme");
                stm.setString(3, "Armure");
                stm.setString(4, "Potion");
                stm.setString(5, "Habilete");
                
                stm.execute();
                
                ResultSet rst = (ResultSet)stm.getObject(1);
                
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
                if(oradb != null)
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
