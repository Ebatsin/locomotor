(function() {
	var view = document.querySelector('#booking-list-page');
	var ul = view.querySelector('ul');

	window.registerView('booking-list');

	if(!window.modules) {
		window.modules = {};
	}

	window.modules['booking-list'] = {
		init: function() {
			console.log('initialisation du module booking-list');
		},
		/**
		* Load the login view.
		* @param params An object that contains the parameters
		* mode: 'login' | 'register'
		*/
		load: function(params) {
			view.classList.remove('hide');
			view.style['z-index'] = getNextZIndex();
			modules.help.pushContext('booking-list');

			modules.menu.pushBackArrow(function() {
				modules['booking-list'].unload();
			});

			API.getAllBooking().then(function(data) {
				modules['booking-list'].printList(data.data.bookings);
			});
		},
		unload: function() {
			modules.menu.popBackArrow();
			view.classList.add('hide');
			modules.help.popContext();
		},
		printList: function(data) {
			ul.innerHTML = '';
			for(var i = 0; i < data.length; ++i) {
				(function(data) {
					var item = document.createElement('li');
					var img = document.createElement('img');
					var nameColumn = document.createElement('div');
					var dateColumn = document.createElement('div');
					var buttonColumn = document.createElement('div');

					var name = document.createElement('div');
					var universe = document.createElement('div');
					var quantity = document.createElement('div');

					var fromDate = document.createElement('div');
					var toDate = document.createElement('div');

					var remove = document.createElement('button');
					var seeMore = document.createElement('button');

					seeMore.innerHTML = 'See more';
					seeMore.classList.add('booking-list-see-more');

					remove.innerHTML = 'Delete this booking';
					remove.classList.add('booking-list-delete');

					fromDate.classList.add('booking-list-from');
					toDate.classList.add('booking-list-to');

					name.classList.add('booking-list-name');
					universe.classList.add('booking-list-universe');
					quantity.classList.add('booking-list-quantity');

					nameColumn.classList.add('booking-list-name-container');
					dateColumn.classList.add('booking-list-date-container');
					buttonColumn.classList.add('booking-list-button-container');

					name.innerHTML = data.itemName;
					universe.innerHTML = data.universeName;
					quantity.innerHTML = 'Quantity : ' + data.qt;

					fromDate.innerHTML = 'From ' + (new Date(data.startDate)).toLocaleDateString();
					toDate.innerHTML = 'To ' + (new Date(data.endDate)).toLocaleDateString();

					API.getImageUrl(data.itemImage).then(function(data) {
						img.src = data.replace('resources/', '');
					});

					seeMore.addEventListener('click', function() {
						loadView('fullview', data.itemID);
					});

					remove.addEventListener('click', function() {
						API.removeBooking(data['_id']).then(function(data) {
							ul.removeChild(item);
						});
					})

					nameColumn.appendChild(name);
					nameColumn.appendChild(universe);
					nameColumn.appendChild(quantity);

					dateColumn.appendChild(fromDate);
					dateColumn.appendChild(toDate);

					buttonColumn.appendChild(remove);
					buttonColumn.appendChild(seeMore);

					item.appendChild(img);
					item.appendChild(nameColumn);
					item.appendChild(buttonColumn);
					item.appendChild(dateColumn);

					ul.appendChild(item);
				})(data[i]);
			}
		}
	};
})();