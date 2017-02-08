(function() {
	var view = document.querySelector('#users-page');

	window.registerView('users');

	if(!window.modules) {
		window.modules = {};
	}

	window.modules['users'] = {
		init: function() {
			console.log('initialisation du module users');
		},
		load: function() {
			function init() {
				console.log('users loaded');
				hideAllViews();
				modules.menu.show();
				modules.menu.showBackArrow(false);
				modules.help.pushContext('users');
				view.classList.remove('hide');
				app.setTitle('Administration - Manage users');

				modules.splash.hide();
			}

			if(!modules.splash.isShown()) {
				console.log('not shown');
				modules.splash.show(init);
			}
			else {
				console.log('shown');
				init();
			}
		},
	};
})();