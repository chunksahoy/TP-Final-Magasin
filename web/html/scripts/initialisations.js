$(document).ready(function() {
	var main = document.createElement("table");
	main.id = "background";
	var mainRow = document.createElement("tr");
	var container = document.createElement("table");
	container.id = "container";		
	
	prepareContainer (container);

	main.appendChild(document.createElement("tr").appendChild(document.createElement("td")).appendChild(container));
	document.getElementsByTagName("body").item(0).appendChild(main);
	
	prepareBody();	
});

function prepareContainer(parent) {
	var bodyMenus = ["headerMenu","bodyMenu","footer"];
	for(var i = 0; i < bodyMenus.length; ++i) {
		var append = document.createElement("tr").appendChild(document.createElement("td"));	
		var div = document.createElement("div");
		div.id = bodyMenus[i];
		append.appendChild(div);
		parent.appendChild(append);
	}
}
function prepareBody() {
	prepareHeaderMenu();
	prepareBodyMenu(3);
	prepareFooter();
}
function prepareHeaderMenu() {
   var header = document.getElementById("headerMenu");   
}
function prepareBodyMenu (nb) {
	var bodyMenu = document.getElementById("bodyMenu");
	var optionsTable = document.createElement("table");
	optionsTable.id = "options";
	
	var row = document.createElement("tr");
	
	for(var i = 0; i < nb; ++i) {
		var cell = document.createElement("td");
		row.appendChild(cell);	
	}
	optionsTable.appendChild(row);
	bodyMenu.appendChild(optionsTable);
	
}
function prepareFooter() {
	var footer = document.getElementById("footer");	
}

var Options = (function () {
      var instance;
      function ZeOptions() {
          
      }			
      function createInstance() {
          var singleton = new ZeOptions();
          return singleton;
      }
      return {
          getInstance : function() {
              if (!instance) {
                  instance = createInstance();
              }
              return instance;
          }	
      };
  })();