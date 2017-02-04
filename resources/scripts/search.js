(function() {
	var view = document.querySelector('#search-page');
	var inputElem = view.querySelector('#search-input');

	var notUsedButton = view.querySelector('#search-not-used');
	var usedButton = view.querySelector('#search-used');

	var usedTitle = usedButton.querySelector('#search-used-title');

	var usedCriteria = false;

	var types = {
		2: 'boolean',
		3: 'integer-interval',
		4: 'float-interval',
		5: 'string-list',
		6: 'weighted-string-list',
		7: 'tree'
	};

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
					}, 200, function() {
						$(usedButton).css('height', 'auto');
					});

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

				/*var strli = new StringList(inputElem, {
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
				strli.setSelected([2, 3, 5, 7]);*/


				/*var tree = new Tree(inputElem, {"value":"all","id":31,"children":[{"value":"road","id":3,"children":[{"value":"urban","id":0},{"value":"campaign","id":1},{"value":"trail","id":2}]},{"value":"air","id":7,"children":[{"value":"high atmospheric layers","id":4},{"value":"middle atmospheric layers","id":5},{"value":"low atmospheric layers","id":6}]},{"value":"ground","id":20,"children":[{"value":"flat","id":11,"children":[{"value":"snowy","id":8},{"value":"desert","id":9},{"value":"boggy","id":10}]},{"value":"hilly","id":15,"children":[{"value":"snowy","id":12},{"value":"desert","id":13},{"value":"boggy","id":14}]},{"value":"mountainous","id":19,"children":[{"value":"snowy","id":16},{"value":"desert","id":17},{"value":"boggy","id":18}]}]},{"value":"tracks","id":21},{"value":"marine","id":26,"children":[{"value":"surface","id":22},{"value":"under water","id":23},{"value":"shallow","id":24},{"value":"deep","id":25}]},{"value":"space","id":29,"children":[{"value":"shallow","id":27},{"value":"deep","id":28}]},{"value":"underground","id":30}]});
				tree.init();
				tree.selectTree({"value":"all","id":"all","children":[{"value":"road","id":3,"children":[{"value":"trail","id":2}]},{"value":"ground","id":20,"children":[{"value":"hilly","id":15,"children":[{"value":"snowy","id":12},{"value":"desert","id":13}]}]}]});
				*/
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
			var catOpen = false;
			var topOffset = 0;

			var breadcrumb = document.querySelector('#search-breadcrumb');
			var breadcrumbCat = document.querySelector('#search-breadcrumb-cat');
			breadcrumbCat.innerHTML = '';

			for(var i = 0; i < model.model.length; ++i) {
				if(model.model[i].name === '_self_') {
					for(var j = 0; j < model.model[i].criteria.length; ++j) {
						categories.push({
							name: model.model[i].criteria[j].name,
							oldName: model.model[i].name,
							id: model.model[i].criteria[j].name,
							oldId: model.model[i].id
						});
					}
				}
				else {
					categories.push({
						name: model.model[i].name,
						oldName : model.model[i].name,
						id: model.model[i]['_id']
					});
				}
	
			}

			for(var i = 0; i < categories.length; ++i) {
				(function(i) {
					var item = document.createElement('li');
					item.innerHTML = categories[i].name;
					breadcrumbCat.appendChild(item);

					item.addEventListener('click', function() {
						if(catOpen) {
							topOffset = -i*4;
							$(breadcrumbCat).stop();
							$(breadcrumbCat).animate({
								'margin-top': topOffset + 'em'
							}, 200);
						}
					});
				})(i);
			}

			breadcrumbCat.querySelector('li').classList.add('selected');

			breadcrumbCat.addEventListener('click', function() {
				breadcrumb.classList.toggle('show-cat');
				catOpen = breadcrumb.classList.contains('show-cat');
			});

			breadcrumbCat.addEventListener('wheel', function(e) {
				if(catOpen) {
					if(e.deltaY > 0) {
						topOffset -= 4;
					}
					else {
						topOffset += 4;
					}

					if(topOffset > 0) {
						topOffset = 0;
					}
					else if(topOffset < -4 * (categories.length - 1)) {
						topOffset = -4 * (categories.length - 1);
					}

					$(breadcrumbCat).stop();
					$(breadcrumbCat).animate({
						'margin-top': topOffset + 'em'
					}, 200);
				}
			});
		}
	};
})();