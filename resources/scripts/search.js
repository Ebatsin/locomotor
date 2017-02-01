(function() {
	var view = document.querySelector('#search-page');
	var inputElem = view.querySelector('#search-input');

	var notUsedButton = view.querySelector('#search-not-used');
	var usedButton = view.querySelector('#search-used');

	var usedTitle = usedButton.querySelector('#search-used-title');

	var usedCriteria = false;

	// model datas
	var categories = [];

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
						height: '4rem'
					}, 200);
					$(usedButton).animate({
						height: '4rem'
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
					$(usedButton).css('height', '4rem');
					$(usedTitle).css('height', '4rem');

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

				// Integer interval
				//var range = new Range(inputElem, 0, 1000);
				//range.init();

				// Float interval
				//var range = new Range(inputElem, 0, 100, 0.1, 1);
				//range.init();

				// String interval
				//var range = new Range(inputElem, 0, 5, ['first', 'second', 'third', 'fourth', 'fifth', 'sixth']);
				//range.init();

				// Integer interval with exponential function
				//var range = new Range(inputElem, 0, 1000000000, 0.0001, 0);
				//range.init(true);	


				/*var boolean = new Boolean(inputElem);
				boolean.init();
				boolean.setChecked(true);*/

				var strli = new StringList(inputElem, {
					'0': 'essence',
					'1': 'diesel',
					'2': 'électrique',
					'3': 'magique',
					'4': 'foin',
					'5': 'vapeur',
					'6': 'energie atomique',
					'7': 'puissance divine' 
				});

				strli.init();
				strli.setSelected([2, 3, 5, 7]);

				modules.search.initBreadcrumb();
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
		initBreadcrumb: function() {
			
		}
	};
})();