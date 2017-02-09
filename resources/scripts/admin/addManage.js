(function() {
	var view = document.querySelector('#add-manage-page');
	var item = view.querySelector('#add-manage-item');
	var universe = view.querySelector('#add-manage-universe');

	window.registerView('add-manage');

	if(!window.modules) {
		window.modules = {};
	}

	window.modules['add-manage'] = {
		init: function() {
			console.log('initialisation du module add-manage');
			item.addEventListener('click', function() {				
				API.getAllItems().then(function(data) {
					loadView('manage', data);
				});
			});

			universe.addEventListener('click', function() {
				loadView('manage-universes');
			});
		},
		/**
		* Load the login view.
		* @param params An object that contains the parameters
		* mode: 'login' | 'register'
		*/
		load: function(params) {
			function init() {
				console.log('add manage laucnhed');
				hideAllViews();
				modules.menu.show();
				modules.menu.showBackArrow(false);
				modules.help.pushContext('add-manage');
				view.classList.remove('hide');
				modules.menu.clearBackArrow();

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
		}
	};
})();