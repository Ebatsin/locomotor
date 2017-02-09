(function() {
	var view = document.querySelector('#manage-universes-page');
	var listContainer = view.querySelector('#manage-universes-list ul');

	window.registerView('manage-universes');

	if(!window.modules) {
		window.modules = {};
	}

	window.modules['manage-universes'] = {
		init: function() {
			console.log('initialisation du module manage-universes');
		},
		/**
		* Load the login view.
		* @param params An object that contains the parameters
		* mode: 'login' | 'register'
		*/
		load: function(params) {
			console.log('manage loaded');
			view.classList.remove('hide');
			modules.menu.show();

			modules.menu.pushBackArrow(function() {
				modules['manage-universes'].unload();
			});
			view.style['z-index'] = getNextZIndex();
			modules.help.pushContext('manage-universes');

			modules.splash.hide();

			modules['manage-universes'].printList();

		},
		unload: function() {
			modules.menu.popBackArrow();
			view.classList.add('hide');
			modules.help.popContext();
		},
		printList: function() {
			listContainer.innerHTML = '';
			for(var i = 0; i < universes.length; ++i) {
				(function(uni) {
					var item = document.createElement('li');

					var editedView = document.createElement('div');
					var showView = document.createElement('div');

					var img = document.createElement('img');
					var scolumn = document.createElement('div');
					var tcolumn = document.createElement('div');

					var name = document.createElement('h2');
					var description = document.createElement('p');

					var change = document.createElement('button');
					var remove = document.createElement('button');

					scolumn.appendChild(name);
					scolumn.appendChild(description);

					tcolumn.appendChild(change);
					tcolumn.appendChild(remove);

					scolumn.classList.add('scolumn');
					tcolumn.classList.add('tcolumn');

					// edit view
					var nameLabel = document.createElement('label');
					var nameInput = document.createElement('input');

					var imageLabel = document.createElement('label');
					var imageInput = document.createElement('input');

					var descLabel = document.createElement('label');
					var descInput = document.createElement('textarea');

					var cancelButton = document.createElement('button');
					var validButton = document.createElement('button');
					var optionButtons = document.createElement('div');

					name.innerHTML = uni.name;
					API.getImageUrl(uni.image).then(function(data) {
						img.src = data.replace('resources/', '');
					});

					change.innerHTML = 'Modify';
					remove.innerHTML = 'Delete';
					description.innerHTML = uni.description;

					showView.appendChild(img);
					showView.appendChild(tcolumn);
					showView.appendChild(scolumn);

					item.appendChild(showView);

					nameLabel.innerHTML = 'Name: ';
					imageLabel.innerHTML = 'Image url: ';
					descLabel.innerHTML = 'Description: ';
					cancelButton.innerHTML = 'Cancel';
					validButton.innerHTML = 'Save';

					nameInput.value = uni.name,
					imageInput.value = uni.image;
					descInput.value = uni.description;

					descLabel.classList.add('universes-description');

					optionButtons.classList.add('tcolumn');

					optionButtons.appendChild(cancelButton);
					optionButtons.appendChild(validButton);

					editedView.appendChild(optionButtons);

					editedView.appendChild(nameLabel);
					editedView.appendChild(nameInput);
					editedView.appendChild(imageLabel);
					editedView.appendChild(imageInput);

					editedView.appendChild(descLabel);
					editedView.appendChild(descInput);

					item.appendChild(editedView);

					editedView.classList.add('hidden');

					cancelButton.addEventListener('click', function() {
						showView.classList.remove('hidden');
						editedView.classList.add('hidden');
					});

					change.addEventListener('click', function() {
						showView.classList.add('hidden');
						editedView.classList.remove('hidden');
					});

					remove.addEventListener('click', function() {
						(new Popin()).open('Delete an universe', 'Are you sure that you want to delete this universe ? Note that all the vehicles linked to it will be removed.', function() {
							API.removeUniverse(uni['_id']).then(function(data) {
								API.getAllUniverses().then(function(uni) {
									window.universes = uni.data.universes;
									modules['manage-universes'].unload();
									modules['manage-universes'].load();
								});
							}).catch(function(data) {
								console.log(data.message)
							});
						});
					}),

					validButton.addEventListener('click', function() {
						if(nameInput.value.trim().length == 0 || imageInput.value.trim().length == 0 || descInput.value.trim().length == 0) {
							return;
						}
						API.updateUniverse({
							id: uni['_id'],
							name: nameInput.value.trim(),
							image: imageInput.value.trim(),
							description: descInput.value.trim()
						}).then(function(data) {
							API.getAllUniverses().then(function(uni) {
								window.universes = uni.data.universes;
								modules['manage-universes'].unload();
								modules['manage-universes'].load();
							});
						});
					});


					listContainer.appendChild(item);
				})(universes[i]);
			}
		}
	};
})();