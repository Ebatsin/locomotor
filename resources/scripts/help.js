(function() {
	var view = document.querySelector('#help');
	var close = document.querySelector('#help-close');

	var isOpened = false;


	if(!window.modules) {
		window.modules = {};
	}

	window.modules['help'] = {
		init: function() {
			console.log('initialisation du module help');
			modules.menu.bind('help', function() {
				modules.menu.close();
				if(isOpened) {
					modules.help.close();
				}
				else {
					modules.help.open();
				}
			});

			close.addEventListener('click', function() {
				modules.help.close();
			});
		},
		open: function() {
			if(modules.menu.isOnlyHelpShown()) {
				modules.menu.tmpShowOnlyHelp(false);
			}
			isOpened = true;

			$(view).animate({
				width: '25em'
			});
		},
		close: function() {
			isOpened = false;
			
			$(view).animate({
				width: '0'
			});
			
			setTimeout(function() {
				if(modules.menu.isOnlyHelpShown()) {
					modules.menu.tmpShowOnlyHelp(true);
				}
			}, 700);
		},
		setContext: function(context) {
			var elem = document.querySelector('#help-' + context);
			if(elem) {
				var current = document.querySelector('.help-page.current');
				if(current) {
					current.classList.remove('current');
				}
				elem.classList.add('current');
			}
		}
	};
})();