(function() {
	var view = document.querySelector('#create-universes-page');
	var name = view.querySelector('#create-universes-name');
	var image = view.querySelector('#create-universes-image');
	var desc = view.querySelector('#create-universes-description');
	var valid = view.querySelector('#create-universes-create');

	window.registerView('create-universe');

	if(!window.modules) {
		window.modules = {};
	}

	window.modules['create-universe'] = {
		init: function() {
			console.log('initialisation du module create-universe');

			valid.addEventListener('click', function() {
				modules['create-universe'].start();
			});
		},
		/**
		* Load the login view.
		* @param params An object that contains the parameters
		* mode: 'login' | 'register'
		*/
		load: function(params) {
			view.classList.remove('hide');
			modules.menu.show();

			modules.menu.pushBackArrow(function() {
				modules['create-universe'].unload();
			});
			view.style['z-index'] = getNextZIndex();
			modules.help.pushContext('create-universe');

			modules.splash.hide();

			name.value = '';
			image.value = '';
			desc.value = '';

		},
		unload: function() {
			modules.menu.popBackArrow();
			view.classList.add('hide');
			modules.help.popContext();
		},
		start: function() {
			if(name.value.trim().length == 0 || image.value.trim().length == 0 || desc.value.trim().length == 0) {
				return;
			}

			API.addUniverse({
				name: name.value.trim(),
				image: image.value.trim(),
				description: desc.value.trim()
			}).then(function(data) {
				API.getAllUniverses().then(function(uni) {
					window.universes = uni.data.universes;
					modules['create-universe'].unload();
				});
			});
		}
	};
})();