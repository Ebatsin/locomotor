(function() {
	var menu = document.querySelector("#menu");
	var toggle = menu.querySelector("#menu-toggle");
	var isShown = true;
	var width = menu.offsetWidth; // check the dimension when opened
	var transitionDuration = 0.2;
	menu.style.width = "4em"; // then close it

	function slideOpen() {
		if(menu.classList.contains('open')) return;

		menu.classList.add("open");
		YUI().use('node', 'transition', function(Y) {
			Y.one(menu).transition({
				easing: 'ease-in-out',
				duration: transitionDuration,
				width: width + 'px'
			});
		});
	}

	function slideClose() {
		if(!menu.classList.contains('open')) return;

		menu.classList.remove('open');
		YUI().use('node', 'transition', function(Y) {
			Y.one(menu).transition({
				easing: 'ease-in-out',
				duration: transitionDuration,
				width: '4em'
			});
		});
	}

	toggle.addEventListener("click", function() {
		if(menu.classList.contains("open")) {
			slideClose();
		}
		else {
			slideOpen();
		}

	});

	document.addEventListener("click", function(e) {
		var current = e.target;
		while(current.parentNode != null) {
			if(current === menu) {
				return;
			}
			current = current.parentNode;
		}

		if(menu.classList.contains('open')) {
			slideClose();
		}


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