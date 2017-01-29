(function() {
	var view = document.querySelector('#login-page');

	// title
	var title = view.querySelector('#login-form h2');

	// fields
	var name = view.querySelector('#login-form-name');
	var password = view.querySelector('#login-form-password');
	var confirm = view.querySelector('#login-form-confirm');

	// buttons
	var button = view.querySelector('#login-form button');
	var toggle = view.querySelector('#login-form-toggle');

	var form = view.querySelector('#login-form');

	var error = view.querySelector('#login-form p');

	var loginMode = true;

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
			toggle.addEventListener('click', function() {
				if(loginMode) {
					modules.login.showRegister();
				}
				else {
					modules.login.showLogin();
				}
			});

			form.addEventListener('submit', function(e) {
				// check if all the fields are filled
				if(loginMode) {
					if(name.value.trim().length === 0 || password.value.length === 0) {
						modules.login.setFixingMethod(fixEmptyField);
						modules.login.showError('All the fields must be filled to continue.');
					}
					else {
						console.log('send the login request');
						API.auth(name.value, password.value).then(function(data) {
							modules.login.validAuth(data.data['short-term-token'], data.data['long-term-token']);
						}).catch(function(data) {
							modules.login.showError(data.message);
						});
						modules.login.emptyFields();
					}
				}
				else { // register
					if(name.value.trim().length === 0 || password.value.length === 0 || confirm.value.length === 0) {
						modules.login.setFixingMethod(fixEmptyField);
						modules.login.showError('All the fields must be filled to continue.');
					}
					else if(confirm.value !== password.value) {
						modules.login.setFixingMethod(fixMismatchingPasswords);
						modules.login.showError('The password field and the password confirmation field do not match. Please try again.');
					}
					else {
						// @todo send the request
						console.log('send the register request');
						API.register(name.value, password.value).then(function(data) {
							modules.login.validAuth(data.data['short-term-token'], data.data['long-term-token']);
						}).catch(function(data) {
							modules.login.showError(data.message);
						});
						modules.login.emptyFields();
					}
				}

				e.preventDefault();
			});

			name.addEventListener('input', checkForFix);
			password.addEventListener('input', checkForFix);
			confirm.addEventListener('input', checkForFix);

		},
		/**
		* Load the login view.
		* @param params An object that contains the parameters
		* mode: 'login' | 'register'
		*/
		load: function(params) {
			hideAllViews();
			modules.menu.hide();
			modules.menu.showOnlyHelp(true);
			view.classList.remove('hide');

			if(app.getLongToken() != null) {
				console.log('local long token found. Using it');
				console.log('"' + app.getLongToken() + '"');
				// try to log with this token
				API.auth(app.getLongToken()).then(function(data) {
					console.log('successful token login');
					modules.login.validAuth(data.data['short-term-token'], data.data['long-term-token']);
				}).catch(function(data) {
					console.log('failed token login : ' + data.message + "\nFalling back to normal login");
					app.deleteLongToken(); // no longer valid or invalid
					loadView('login', params); // reload the view
				});
				return;
			}
			// else no token. Normal log in

			if(params && params.mode === 'register') {
				modules.login.showRegister();
			}
			else {
				modules.login.showLogin();
			}
		},
		showRegister: function() {
			loginMode = false;

			modules.login.hideError();
			app.setTitle('Register');
			toggle.innerHTML = 'Log-in';
			title.innerHTML = 'REGISTER';
			button.innerHTML = 'Register';
			modules.login.emptyFields();
			confirm.classList.remove('hide');
		},
		showLogin: function() {
			loginMode = true;

			modules.login.hideError();
			app.setTitle('Log-in');
			toggle.innerHTML = 'Register';
			title.innerHTML = 'LOG IN';
			button.innerHTML = 'Log in';
			modules.login.emptyFields();
			confirm.classList.add('hide');
		},
		emptyFields: function() {
			name.value = '';
			password.value = '';
			confirm.value = '';
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
			// @todo save the tokens and process to the home view
			hideAllViews();
		}
	};
})();