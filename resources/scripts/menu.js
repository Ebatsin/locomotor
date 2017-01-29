(function() {
	var menu = document.querySelector('#menu');
	var help = document.querySelector('#menu-only-help');
	var toggle = menu.querySelector('#menu-toggle');
	var backArrow = menu.querySelector('#menu-back');
	var settings = menu.querySelector('#menu-settings');
	var booking = menu.querySelector('#menu-booking');
	var helpElem = menu.querySelector('#menu-help');
	var disconnect = menu.querySelector('#menu-disconnect');

	var displayBackArrow = true;
	var mode = 'user';

	var menuWidth = menu.offsetWidth; // needed for the animations
	var animationDuration = 0.2; // length of the opening/closing animation in seconds

	// close the menu after having read its width
	menu.classList.remove('open');
	menu.style.width = '4em';

	function backItem() {
		callbacks.back();
	}

	function settingsItem() {
		callbacks.settings();
	}

	function bookingItem() {
		callbacks.booking();
	}

	function helpItem() {
		callbacks.help();
	}

	function disconnectItem() {
		callbacks.disconnect();
	}

	var callbacks = {
		back: function() {
			console.log('back item pressed');
		},
		settings: function() {
			console.log('settings item pressed');
		},
		booking: function() {
			console.log('booking item pressed');
		},
		help: function() {
			console.log('help item pressed');
		},
		disconnect: function() {
			console.log('disconnect item pressed');
		}
	};

	if(!window.modules) {
		window.modules = {};
	}

	window.modules['menu'] = {
		init: function() {
			console.log('Initalisation du module menu');
			toggle.addEventListener('click', function() {
				if(modules.menu.isOpen()) {
					modules.menu.close();
				}
				else {
					modules.menu.open();
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

				modules.menu.close();
			});

			// hooks des items
			backArrow.addEventListener('click', backItem);
			settings.addEventListener('click', settingsItem);
			booking.addEventListener('click', bookingItem);
			helpElem.addEventListener('click', helpItem);
			help.addEventListener('click', helpItem);
			disconnect.addEventListener('click', disconnectItem);
		},
		/**
		* Extends the menu to display the text
		*/
		open: function() {
			if(modules.menu.isOpen()) {
				return;
			}

			menu.classList.add('open');
			YUI().use('node', 'transition', function(Y) {
				Y.one(menu).transition({
					easing: 'ease-in-out',
					duration: animationDuration,
					width: menuWidth + 'px'
				});
			});
		},
		/**
		* Furl the menu to show only the icons
		*/
		close: function() {
			if(!modules.menu.isOpen()) {
				return;
			}

			menu.classList.remove('open');
			YUI().use('node', 'transition', function(Y) {
				Y.one(menu).transition({
					easing: 'ease-in-out',
					duration: animationDuration,
					width: '4em'
				});
			});
		},
		/**
		* Show the menu on the left
		*/
		show: function() {
			modules.menu.showOnlyHelp(false);
			menu.style.display = "inline-block";
			document.querySelector('#main-page').style.marginLeft = '4em';
		},
		/**
		* Hide the menu on the left
		*/
		hide: function() {
			menu.style.display = "none";
			document.querySelector('#main-page').style.marginLeft = '0';
		},
		/**
		* Check wether the menu is opened or closed
		*/
		isOpen: function() {
			return menu.classList.contains('open');
		},
		/**
		* Display the 'admin' mode of the menu or the 'user' mode
		*/
		setMode: function(m) { // 'admin' or 'user'
			mode = m;
		},
		/**
		* Show the back arrow in the menu or not
		*/
		showBackArrow: function(show) { // wether or not to show the back arrow
			backArrow.style.display = show ? 'block' : 'none';
		},
		/**
		* Closes the left menu and show only the help icon in the top left corner
		*/
		showOnlyHelp: function(show) { // wether or not to show the help menu in the top left corner
			modules.menu.hide();

			if(show) {
				help.style.display = 'block';
			}
			else {
				help.style.display = 'none';
			}

		},
		/**
		* item : 
		* back
		* settings
		* booking
		* help
		* disconnect
		*/
		bind: function(item, callback) {
			if(callbacks[item]) {
				callbacks[item] = callback;
			}
		}
	};
})();