(function() {
	var view = document.querySelector('#add-choice-page');
	var item = view.querySelector('#add-choice-item');
	var universe = view.querySelector('#add-choice-universe');

	window.registerView('add-choice');

	if(!window.modules) {
		window.modules = {};
	}

	window.modules['add-choice'] = {
		init: function() {
			console.log('initialisation du module add-choice');
			item.addEventListener('click', function() {
				loadView('add');
			});
			universe.addEventListener('click', function() {
				loadView('create-universe');
			});
		},
		/**
		* Load the login view.
		* @param params An object that contains the parameters
		* mode: 'login' | 'register'
		*/
		load: function(params) {
			function init() {
				hideAllViews();
				modules.menu.show();
				modules.menu.showBackArrow(false);
				modules.help.pushContext('add-choice');
				view.classList.remove('hide');
				modules.menu.clearBackArrow();

				modules.splash.hide();
			}

			if(!modules.splash.isShown()) {
				modules.splash.show(init);
			}
			else {
				init();
			}
		}
	};
})();