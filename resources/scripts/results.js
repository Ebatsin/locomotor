(function() {
	var view = document.querySelector('#results-page');
	var listContainer = view.querySelector('#results-list ul');
	var preview = document.querySelector('#results-preview');

	window.registerView('results');

	if(!window.modules) {
		window.modules = {};
	}

	var rawList;

	window.modules['results'] = {
		init: function() {
			console.log('initialisation du module results');
		},
		/**
		* Load the login view.
		* @param params An object that contains the parameters
		* mode: 'login' | 'register'
		*/
		load: function(params) {
			view.classList.remove('hide');
			view.style['z-index'] = getNextZIndex();

			rawList = params.data.results;

			modules.menu.showBackArrow(true);
			modules.menu.pushBackArrow(function() {
				console.log('retour depuis la vue des résultats');
			});

			modules.results.printList();

		},
		unload: function() {
			modules.menu.popBackArrow();
		},
		printList: function() {
			for(var i = 0; i < rawList.length; ++i) {
				(function(i) {
					var item = document.createElement('li');
					var image = document.createElement('img');
					var container = document.createElement('div');

					var title = document.createElement('h2');
					var universe = document.createElement('div');
					var percentage = document.createElement('div');

					universe.classList.add('universe');
					percentage.classList.add('percentage');

					container.appendChild(title);
					container.appendChild(universe);
					container.appendChild(percentage);
					item.appendChild(image);
					item.appendChild(container);

					title.innerHTML = rawList[i].name;
					universe.innerHTML = 'Universe : ' + rawList[i].universe;
					percentage.innerHTML = 'Match : ' + Math.round(rawList[i].grade * 100) + '%';
					listContainer.appendChild(item);
				})(i);
			}

			modules.results.showPreview(0);
		},
		showPreview: function(id) {
			preview.querySelector('h2').innerHTML = rawList[id].name;
			preview.querySelector('.universe').innerHTML = rawList[id].universe;
			preview.querySelector('.percentage').innerHTML = Math.round(rawList[id].grade * 100) + '%';

			var ul = preview.querySelector('ul');
			ul.innerHTML = '';
			for(var i = 0; i < rawList[id].criterias.length; ++i) {
				(function(i) {
					var li = document.createElement('li');
					var name = document.createElement('span');
					var percentage = document.createElement('span');

					percentage.classList.add('criteria-match');

					name.innerHTML = rawList[id].criterias[i].name;
					percentage.innerHTML = Math.round(rawList[id].criterias[i].grade * 100) + '%';

					li.appendChild(name);
					li.appendChild(percentage);
					ul.appendChild(li);
				})(i);
			}
		}
	};
})();