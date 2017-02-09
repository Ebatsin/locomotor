(function() {
	var view = document.querySelector('#booking-page');
	var container = view.querySelector('#booking-side');
	var name = view.querySelector('#booking-item-name');
	var inputFrom = view.querySelector('#booking-from');
	var inputTo = view.querySelector('#booking-to');
	var inputQte = view.querySelector('#booking-quantity');
	var cancel = view.querySelector('#booking-cancel');
	var book = view.querySelector('#booking-book');

	var fromPicker, toPicker;
	var itemId;

	window.registerView('booking');

	if(!window.modules) {
		window.modules = {};
	}

	window.modules['booking'] = {
		init: function() {
			console.log('initialisation du module booking');
			fromPicker = new Pikaday({
				field: inputFrom,
				minDate: new Date(),
				onSelect: function() {
					toPicker.setMinDate(fromPicker.getDate());
				}
			});

			toPicker = new Pikaday({
				field: inputTo,
				minDate: new Date(),
				onSelect: function() {
					fromPicker.setMaxDate(toPicker.getDate());
				}
			});

			cancel.addEventListener('click', modules.booking.unload);
			book.addEventListener('click', modules.booking.validBooking);
		},
		/**
		* Load the login view.
		* @param params An object that contains the parameters
		* mode: 'login' | 'register'
		*/
		load: function(params) {
			view.classList.remove('hide');
			view.style['z-index'] = getNextZIndex();
			modules.help.pushContext('booking');

			modules.menu.pushBackArrow(function() {
				modules.booking.unload();
			});

			$(view).animate({
				opacity: 1
			}, 200);
			$(container).animate({
				right: 0
			}, 400);

			name.innerHTML = params.name;
			itemId = params.id;
		},
		unload: function() {
			modules.menu.popBackArrow();
			modules.help.popContext();

			$(view).animate({
				opacity: 0
			}, 400, function() {
				view.classList.add('hide');
			});
			$(container).animate({
				right: '-30em'
			}, 200);
		},
		validBooking: function() {
			if(parseInt(inputQte.value.trim()) > 0) {
				API.book(itemId, fromPicker.getDate().getTime(), toPicker.getDate().getTime(), parseInt(inputQte.value.trim())).then(function(data) {
					modules.booking.unload();
				}).catch(function(data) {
					console.log(data.message);
				});
			}
		}
	};
})();