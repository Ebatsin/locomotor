(function() {
	var view = document.querySelector('#manage-page');
	var listContainer = view.querySelector('#manage-list ul');
	var preview = document.querySelector('#manage-preview');
	var toFullview = document.querySelector('#manage-fullview');
	var edit = view.querySelector('#manage-edit');
	var remove = view.querySelector('#manage-delete');

	window.registerView('manage');

	if(!window.modules) {
		window.modules = {};
	}

	var rawList;
	var currentId;

	window.modules['manage'] = {
		init: function() {
			console.log('initialisation du module manage');

			toFullview.addEventListener('click', function() {
				loadView('fullview', rawList[currentId]['_id']);
			});

			edit.addEventListener('click', function() {
				if(rawList.length == 0) {
					return;
				}
				API.getItem(rawList[currentId]['_id']).then(function(data) {
					loadView('add', data.data.item);
				});
			});

			remove.addEventListener('click', function() {
				if(rawList.length == 0)  {
					return;
				}
				(new Popin()).open("Delete the vehicle", "Are you sure that you want to delete this vehicle ?", function() {
					API.removeItem(rawList[currentId]['_id']).then(function(data) {
						rawList[currentId].remove();
					}).catch(function(data) {
						console.log(data.message);
					});
				});
			});
		},
		/**
		* Load the login view.
		* @param params An object that contains the parameters
		* mode: 'login' | 'register'
		*/
		load: function(params) {
			function init() {
				console.log('manage loaded');
				hideAllViews();
				modules.menu.show();
				modules.menu.showBackArrow(false);
				modules.menu.clearBackArrow();
				modules.help.pushContext('manage');
				view.classList.remove('hide');
				app.setTitle('Administration - Manage the vehicles');

				rawList = params.data.items;

				modules.splash.hide();
				modules.manage.printList();
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

					universe.classList.add('universe');


					API.getImageUrl(rawList[i].image).then(function(data) {
						image.src = data.replace('resources/', '');
					});

					container.appendChild(title);
					container.appendChild(universe);
					item.appendChild(image);
					item.appendChild(container);

					title.innerHTML = rawList[i].name;
					universe.innerHTML = 'Universe : ' + rawList[i].universe;
					listContainer.appendChild(item);

					item.addEventListener('click', function() {
						modules.manage.showPreview(i);
					});

					rawList[i].remove = function() {
						item.parentNode.removeChild(item);
					};
				})(i);
			}

			if(rawList.length > 0) {
				modules.manage.showPreview(0);
			}
		},
		showPreview: function(id) {
			currentId = id;
			preview.querySelector('h2').innerHTML = rawList[id].name;
			preview.querySelector('.universe').innerHTML = rawList[id].universe;

			API.getImageUrl(rawList[id].image).then(function(data) {
				preview.querySelector('img').src = data.replace('resources/', '');
			});
		}
	};
})();