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
					YUI().use('node', 'transition', function(Y) {
						Y.one(usedTitle).transition({
							easing: 'ease',
							duration: 0.4,
							lineHeight: '6rem'
						});
					});
				});

				usedButton.addEventListener('click', function() {
					if(usedCriteria) {
						return;
					}

					notUsedButton.classList.remove('selected');
					usedButton.classList.add('selected');
					usedCriteria = true;

					// transitionning the title to the top
					YUI().use('node', 'transition', function(Y) {
						Y.one(usedTitle).transition({
							easing: 'ease',
							duration: 0.4,
							lineHeight: '4rem'
						});
					});

				});

				inputElem.innerHTML = '';
				var range = new Range(inputElem, 0, 10, 0.01, 2);
				range.init();
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