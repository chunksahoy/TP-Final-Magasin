$(document).ready(function (){
   $("#numeroItem").prop('disabled',true);
   document.getElementById("inputQuantite").value = 1; 
   function createLogMenu() {
      var container = document.createElement("div");
      var div = document.createElement("div");
      container.id = "logMenu";

      div.innerHTML = "<form action='magasin' method='get'><table>"+
              "<tr><td>Alias: <input type='text' name='alias'/></td></tr>"+
              "<tr><td><input type='submit' id='login' value='Connexion'/><input type='button' name='cancel' id='cancel' value='Annuler'/></td></tr>"+
              "<tr colspan=2><td><a href='inscription'>Inscription</a></td></tr>" +
              "</table></form>";
      container.appendChild(div);
      $("body").prepend(container);
      $("#cancel").on('click', function () {
         $("#logMenu").remove();
      });

   }
   $("#connection").on('click', createLogMenu);
   
   $("#items tr").on('click', function() {
      var raw = $(this).text().split('\n');
      $("#items tr").each(function (index) {
         $(this).css("background-color", "white");
         $(this).css("color", "black");
         $(this).removeClass("highlightItem");

      });
      $(this).addClass("highlightItem");
      var stats = [];
      stats[1] = raw[1];
      stats[3] = raw[2];
      stats[5] = raw[5];
      stats[6] = raw[3];

      document.cookie = "numItem=" + stats[1] + ";expires=Sat, 30 May 2014 08:00:00 UTC";
      document.cookie = "typeItem=" + stats[5] + ";expires=Sat, 30 May 2014 08:00:00 UTC";
      document.cookie = "prixItem=" + stats[6] + ";expires=Sat, 30 May 2014 08:00:00 UTC";
      $.get( "http://localhost:8084/TP-Final-Magasin/magasin", function( data ) {
         $( "#statistiques" ).html( $(data).find("#statistiques") );
         $( "#ID" ).html( $(data).find("#ID") );
         $( "#totalJoueur" ).html( $(data).find("#totalJoueur") );
         $("#statistiques tr td").each(function (index) {
            if(index%2 !== 0 && index >1) {
               $(this).text(stats[index]);
            } 
            document.getElementById("numeroItem").value = stats[1];
            document.getElementById("inputQuantite").value = 1; 
            $("#inputTotal").text(stats[6] + " Écus");
            $('#inputQuantite').attr('max', raw[4]);
         });
      });
   });
   $("#inputQuantite").on('click', function() {
      var price = getCookie("prixItem");      
      var qte = document.getElementById("inputQuantite").value;
      $("#inputTotal").text(price * qte + " Écus");      
   });

   function getCookie(name){
      var pattern = RegExp(name + "=.[^;]*");
      matched = document.cookie.match(pattern);
      if(matched){
         var cookie = matched[0].split('=');
         return cookie[1];
      }
      return false;
   }
});


	
