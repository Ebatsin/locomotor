(function() {
	var view = document.querySelector('#help');
	var close = document.querySelector('#help-close');


	if(!window.modules) {
		window.modules = {};
	}

	window.modules['help'] = {
		init: function() {
			console.log('initialisation du module help');
			modules.menu.bind('help', function() {
				modules.menu.close();
				modules.help.open();
			});

			close.addEventListener('click', function() {
				console.log('fermeture');
				modules.help.close();
			});
		},
		open: function() {
			if(modules.menu.isOnlyHelpShown()) {
				modules.menu.tmpShowOnlyHelp(false);
			}

			YUI().use('node', 'transition', function(Y) {
				Y.one(view).transition({
					easing: 'ease-in-out',
					duration: 0.7,
					width: '25em'
				});
			});
		},
		close: function() {
			YUI().use('node', 'transition', function(Y) {
				Y.one(view).transition({
					easing: 'ease-in-out',
					duration: 0.7,
					width: 0
				});
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