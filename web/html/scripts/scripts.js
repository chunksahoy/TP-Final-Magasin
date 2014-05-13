$(document).ready(function (){
   $("#numeroItem").prop('disabled',true);
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
   $("#items tr").on('click', function() {
      var raw = $(this).text().split('\n');
      
       $("#items tr").each(function (index) {
         $(this).css("background-color", "white");
         $(this).css("color", "black");
         $(this).removeClass("highlightItem");

      });
      $(this).addClass("highlightItem");
      $(this).css("background-color", "blue");
      $(this).css("color", "white");
      var stats = [];
      stats[1] = raw[1];
      stats[3] = raw[2];
      stats[5] = raw[5];
      stats[6] = raw[3];
      $("#statistiques tr td").each(function (index) {
            if(index%2 !== 0 && index >1) {
               $(this).text(stats[index]);
            }
      });
      document.getElementById("numeroItem").value = stats[1];
      document.getElementById("inputQuantite").value = 1; 
      $("#inputTotal").text(stats[6] + " Écus");
      $('#inputQuantite').attr('max', raw[4]);
   });
   $("#inputQuantite").on('click', function() {
      var raw = $(".highlightItem").text().split('\n');
      var qte = document.getElementById("inputQuantite").value;      
      var stats = [];
      stats[0] = raw[3];
      $("#inputTotal").text(stats[0] * qte + " Écus");      
   });
   $("#connection").on('click',createLogMenu);
});

	
