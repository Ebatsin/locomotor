(function() {
	var view = document.querySelector('#settings-page');
	var username = view.querySelector('#settings-username');
	var changename = view.querySelector('#settings-change-username');
	var nameInput = view.querySelector('#settings-username-input');
	var usernameOptions = view.querySelector('#settings-change-username-options');
	var nameCancel = usernameOptions.querySelector('#settings-username-cancel');
	var nameValid = usernameOptions.querySelector('#settings-username-valid');
	var error = view.querySelector('#settings-name-error');

	var pass = view.querySelector('#settings-password');
	var passTitle = pass.querySelector('div');
	var passOld = pass.querySelector('#settings-pass-old');
	var passNew = pass.querySelector('#settings-pass-new');
	var passVerif = pass.querySelector('#settings-pass-verif');
	var passValid = pass.querySelector('#settings-password-valid');
	var passCancel = pass.querySelector('#settings-password-cancel');

	var passOpen = false;

	window.registerView('settings');

	if(!window.modules) {
		window.modules = {};
	}

	function openPassEvent() {
		if(passOpen) {
			return;
		}

		modules.settings.openPass();
	}

	window.modules['settings'] = {
		init: function() {
			console.log('initialisation du module settings');

			changename.addEventListener('click', function() {
				modules.settings.changeName();
			});

			pass.addEventListener('click', openPassEvent);
		},
		/**
		* Load the login view.
		* @param params An object that contains the parameters
		* mode: 'login' | 'register'
		*/
		load: function(params) {
			view.classList.remove('hide');
			view.style['z-index'] = getNextZIndex();
			modules.help.pushContext('settings');

			modules.menu.pushBackArrow(function() {
				modules.settings.unload();
			});

			API.getUserInfo().then(modules.settings.print);
		},
		unload: function() {
			modules.menu.popBackArrow();
			view.classList.add('hide');
			modules.help.popContext();
		},
		print: function(data) {
			username.innerHTML = data.data.username;
		},
		changeName: function() {
			// refresh the events
			var clone = nameCancel.cloneNode();
			nameCancel.parentNode.replaceChild(clone, nameCancel);
			nameCancel = clone;

			clone = nameValid.cloneNode();
			nameValid.parentNode.replaceChild(clone, nameValid);
			nameValid = clone;

			nameInput.value = username.innerHTML;
			username.classList.add('hidden');
			nameInput.classList.remove('hidden');

			usernameOptions.classList.remove('hidden');
			changename.classList.add('hidden');

			nameCancel.addEventListener('click', function() {
				error.style.display = 'none';
				username.classList.remove('hidden');
				nameInput.classList.add('hidden');

				usernameOptions.classList.add('hidden');
				changename.classList.remove('hidden');
			});

			function changeAction() {
				if(nameInput.value.trim().toLowerCase() === username.innerHTML) {
					error.style.display = 'none';
					username.classList.remove('hidden');
					nameInput.classList.add('hidden');

					usernameOptions.classList.add('hidden');
					changename.classList.remove('hidden');
					return;
				}

				API.changeUsername(nameInput.value.trim().toLowerCase()).then(function(data) {
					error.style.display = 'none';
					username.innerHTML = nameInput.value.trim().toLowerCase();
					username.classList.remove('hidden');
					nameInput.classList.add('hidden');

					usernameOptions.classList.add('hidden');
					changename.classList.remove('hidden');
				}).catch(function(data) {
					error.style.display = 'block';
					error.innerHTML = data.message;
				});
			}

			nameValid.addEventListener('click', changeAction);
			nameInput.addEventListener('keydown', function(e) {
				if(e.keyCode === 13) {
					changeAction();
				}
			});
		},
		openPass: function() {
			pass.removeEventListener('click', openPassEvent);

			pass.classList.add('selected');
			$(passTitle).animate({
				'margin-top': '1em'
			}, 400, function() {
				$(pass).find('.container').css('pointer-events', 'auto');
				$(pass).find('.container').animate({
					opacity: 1
				}, 400);
			});
			passOpen = true;

			passOld.value = '';
			passNew.value = '';
			passVerif.value = '';

			// refresh the events
			var clone = passCancel.cloneNode();
			passCancel.parentNode.replaceChild(clone, passCancel);
			passCancel = clone;

			clone = passValid.cloneNode();
			passValid.parentNode.replaceChild(clone, passValid);
			passValid = clone;

			clone = passOld.cloneNode();
			passOld.parentNode.replaceChild(clone, passOld);
			passOld = clone;

			clone = passNew.cloneNode();
			passNew.parentNode.replaceChild(clone, passNew);
			passNew = clone;

			clone = passVerif.cloneNode();
			passVerif.parentNode.replaceChild(clone, passVerif);
			passVerif = clone;

			function changeAction() {
				if(passOld.value.length == 0 || passNew.value.length == 0 || passVerif.value.length == 0) {
					error.innerHTML = "You must fill all the fields before submiting";
					error.style.display = 'block';
					return
				}
				else if(passNew.value != passVerif.value) {
					error.innerHTML = "The two passwords are not the same. Please try again";
					error.style.display = 'block';
					return
				}
				else {
					error.style.display = 'none';
					// send the request to change
					API.changePassword(passOld.value, passNew.value).then(function(data) {
						// close the menu
						modules.settings.closePass();
					}).catch(function(data) {
						error.innerHTML = data.message;
						error.style.display = 'block';
					});
				}
			}

			passCancel.addEventListener('click', modules.settings.closePass);
			passValid.addEventListener('click', changeAction);
			passOld.addEventListener('keyup', function(e) {
				if(e.keyCode == 13) {
					changeAction();
				}
			});
			passNew.addEventListener('keyup', function(e) {
				if(e.keyCode == 13) {
					changeAction();
				}
			});
			passVerif.addEventListener('keyup', function(e) {
				if(e.keyCode == 13) {
					changeAction();
				}
			});

		},
		closePass: function() {
			pass.classList.remove('selected');
			pass.classList.add('option');
			error.style.display = 'none';
			$(pass).find('.container').css('pointer-events', 'none');

			$(pass).find('.container').animate({
				opacity: 0
			}, 400, function() {
				$(passTitle).animate({
					'margin-top': '7em'
				}, 400, function() {
					pass.addEventListener('click', openPassEvent);
				});
			});
			passOpen = false;
		}
	};
})();