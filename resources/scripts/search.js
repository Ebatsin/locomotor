(function() {
	var view = document.querySelector('#search-page');
	var inputElem = view.querySelector('#search-input');

	var notUsedButton = view.querySelector('#search-not-used');
	var usedButton = view.querySelector('#search-used');

	var usedTitle = usedButton.querySelector('#search-used-title');

	var usedCriteria = false;

	window.registerView('search');

	if(!window.modules) {
		window.modules = {};
	}

	window.modules['search'] = {
		init: function() {
			console.log('initialisation du module search');
		},
		load: function() {
			console.log('loading search');
			function init() {
				console.log('search loaded');
				hideAllViews();
				modules.menu.show();
				modules.menu.showBackArrow(false);
				modules.help.setContext('search');
				view.classList.remove('hide');
				app.setTitle('Search');

				modules.splash.hide();

				notUsedButton.addEventListener('click', function() {
					if(!usedCriteria) {
						return;
					}

					notUsedButton.classList.add('selected');
					usedButton.classList.remove('selected');
					usedCriteria = false;

					// transitionning the title to the top
					$(usedTitle).animate({
						height: '6rem'
					}, 200);
					$(usedButton).animate({
						height: '6rem'
					}, 200);
				});

				usedButton.addEventListener('click', function() {
					if(usedCriteria) {
						return;
					}

					var auto;
					$(usedButton).css('height', 'auto');
					$(usedTitle).css('height', '0');
					auto = $(usedButton).height();
					$(usedButton).css('height', '6rem');
					$(usedTitle).css('height', '6rem');

					notUsedButton.classList.remove('selected');
					usedButton.classList.add('selected');

					$(usedTitle).animate({
						height: 0
					}, 200);
					$(usedButton).animate({
						height: auto + 'px'
					}, 200);

					usedCriteria = true;

				});

				inputElem.innerHTML = '';
				//var range = new Range(inputElem, 0, 10, ['aucun', 'très peu', 'peu', 'moyen', 'beaucoup', 'énormément']);
				//range.init();

				var boolean = new Boolean(inputElem);
				boolean.init();
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