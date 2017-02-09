(function() {
	var view = document.querySelector('#login-page');

	// fields
	var name = view.querySelector('#login-form-name');
	var password = view.querySelector('#login-form-password');

	// buttons
	var button = view.querySelector('#login-form button');

	var form = view.querySelector('#login-form');

	var error = view.querySelector('#login-form p');

	// error handling
	var errorDisplayed = false;
	var fixingMethod = function(){};

	function checkForFix() {
		if(errorDisplayed) {
			fixingMethod();
		}
	}

	// default fixing methods
	function fixMismatchingPasswords() {
		if(password.value === confirm.value) {
			modules.login.hideError();
		}
	}

	function fixEmptyField() {
		if(name.value.trim().length !== 0 && password.value.length !== 0 && (loginMode || confirm.value.length !== 0)) {
			modules.login.hideError();
		}
	}

	window.registerView('login');

	if(!window.modules) {
		window.modules = {};
	}

	window.modules['login'] = {
		init: function() {
			console.log('initialisation du module login');

			form.addEventListener('submit', function(e) {
				// check if all the fields are filled
				if(name.value.trim().length === 0 || password.value.length === 0) {
					modules.login.setFixingMethod(fixEmptyField);
					modules.login.showError('All the fields must be filled to continue.');
				}
				else {
					console.log('send the login request from here with : ' + name.value + ', ' + password.value);
					API.adminAuth(name.value, password.value).then(function(data) {
						modules.login.validAuth(data.data['short-term-token'], data.data['long-term-token']);
					}).catch(function(data) {
						modules.login.showError(data.message);
					});
					modules.login.emptyFields();
				}

				e.preventDefault();
			});

			name.addEventListener('input', checkForFix);
			password.addEventListener('input', checkForFix);

			// bind the disconnect function to the menu
			modules.menu.bind('disconnect', function() {
				app.deleteLongToken();
				//modules.help.clearContext();
				modules.menu.clearBackArrow();
				loadView('login');
			});

			modules.menu.bind('edit', function() {
				loadView('add-manage');
			});

			modules.menu.bind('add', function() {
				loadView('add-choice');
			});

			modules.menu.bind('users', function() {
				loadView('users');
			});

			modules.menu.bind('settings', function() {
				loadView('settings');
			});
			console.log('loggin has been inited');
		},
		/**
		* Load the login view.
		* @param params An object that contains the parameters
		* mode: 'login' | 'register'
		*/
		load: function(params) {
			console.log('loggin is starting');
			hideAllViews();
			modules.menu.hide();
			modules.menu.showOnlyHelp(true);
			modules.help.pushContext('admin-login');
			view.classList.remove('hide');

			if(app.getLongToken() != null) {
				console.log('local long token found. Using it');
				// try to log with this token
				API.adminAuth(app.getLongToken()).then(function(data) {
					console.log('successful token login');
					modules.login.validAuth(data.data['short-term-token'], app.getLongToken());
				}).catch(function(data) {
					console.log('failed token login : ' + data.message + "\nFalling back to normal login");
					app.deleteLongToken(); // no longer valid or invalid
					loadView('login', params); // reload the view
				});
				return;
			}
			else {
				modules.splash.hide();
			}

			
			modules.login.showLogin();

		},
		showLogin: function() {
			modules.login.hideError();

			app.setTitle('Administration - Log in');

			modules.login.emptyFields();

			console.log('shown');
		},
		emptyFields: function() {
			name.value = '';
			password.value = '';
		},
		showError: function(message) {
			errorDisplayed = true;
			error.innerHTML = message;
			error.style.display = 'block';
		},
		hideError: function() {
			errorDisplayed = false;
			error.style.display = 'none';
		},
		setFixingMethod: function(meth) {
			fixingMethod = meth;
		},
		validAuth: function(shortToken, longToken) {
			app.setShortToken(shortToken);
			app.setLongToken(longToken);

			console.log('sending modelRequest');
			API.getModel().then(function(data) {
				API.getUnits().then(function(dataUnit) {
					API.getAllUniverses().then(function(uni) {
						window.model = data.data;
						window.units = dataUnit.data.units;
						window.universes = uni.data.universes;
						// load the search view
						modules.help.popContext();
						loadView('add-manage');
					});

				}).catch(function(data) {
					console.log('Javascript : error while loading the units ' + data.message);
				});
			}).catch(function(data) {
				// @TODO handle this case ? oO
				console.log("Javascript : error while loading the model : " + data.message);
			});
		}
	};
})();