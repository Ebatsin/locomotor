(function() {
	var view = document.querySelector('#results-page');
	var listContainer = view.querySelector('#results-list ul');
	var preview = document.querySelector('#results-preview');
	var book = document.querySelector('#results-book');
	var toFullview = document.querySelector('#results-fullview');

	window.registerView('results');

	if(!window.modules) {
		window.modules = {};
	}

	var rawList;
	var currentId;

	window.modules['results'] = {
		init: function() {
			console.log('initialisation du module results');

			toFullview.addEventListener('click', function() {
				loadView('fullview', rawList[currentId]['_id']);
			});

			book.addEventListener('click', function() {
				loadView('booking', {
					id: rawList[currentId]['_id'],
					name: rawList[currentId].name
				});
			});
		},
		/**
		* Load the login view.
		* @param params An object that contains the parameters
		* mode: 'login' | 'register'
		*/
		load: function(params) {
			view.classList.remove('hide');
			view.style['z-index'] = getNextZIndex();
			modules.help.pushContext('results');

			rawList = params.data.results;

			modules.menu.pushBackArrow(function() {
				modules.results.unload();
			});

			modules.results.printList();
		},
		unload: function() {
			modules.menu.popBackArrow();
			view.classList.add('hide');
			modules.help.popContext();
		},
		printList: function() {
			listContainer.innerHTML = '';
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


					API.getImageUrl(rawList[i].image).then(function(data) {
						image.src = data.replace('resources/', '');
					});

					container.appendChild(title);
					container.appendChild(universe);
					container.appendChild(percentage);
					item.appendChild(image);
					item.appendChild(container);

					title.innerHTML = rawList[i].name;
					universe.innerHTML = 'Universe : ' + rawList[i].universe;
					percentage.innerHTML = 'Match : ' + Math.round(rawList[i].grade * 100) + '%';
					listContainer.appendChild(item);

					item.addEventListener('click', function() {
						modules.results.showPreview(i);
					});
				})(i);
			}
			if(rawList.length > 0) {
				modules.results.showPreview(0);
			}
		},
		showPreview: function(id) {
			currentId = id;
			preview.querySelector('h2').innerHTML = rawList[id].name;
			preview.querySelector('.universe').innerHTML = rawList[id].universe;
			preview.querySelector('.percentage').innerHTML = Math.round(rawList[id].grade * 100) + '%';

			API.getImageUrl(rawList[id].image).then(function(data) {
				preview.querySelector('img').src = data.replace('resources/', '');
			});

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