(function() {
	var menu = document.querySelector("#menu");
	var toggle = menu.querySelector("#menu-toggle");
	var isShown = true;

	toggle.addEventListener("click", function() {
		menu.classList.toggle("open");
	});

	document.addEventListener("click", function(e) {
		var current = e.target;
		while(current.parentNode != null) {
			if(current === menu) {
				return;
			}
			current = current.parentNode;
		}

		menu.classList.remove("open");
	});

	window.hideMenu = function() {
		menu.style.display = "none";
	}

	window.showMenu = function() {
		menu.style.display = "inline-block";
	}

	document.querySelector("#menu-back").addEventListener("click", function() {
		console.log("Retour arrière");
	});

	document.querySelector("#menu-settings").addEventListener("click", function() {
		console.log("Accès aux options");
	});

	document.querySelector("#menu-booking").addEventListener("click", function() {
		console.log("Accès aux réservations");
	});

	document.querySelector("#menu-help").addEventListener("click", function() {
		console.log("Affichage de l'aide");
	});

	document.querySelector("#menu-disconnect").addEventListener("click", function() {
		console.log("Deconnexion");
	});
})();