(function() {
	var onLogin = true; // whether the current page is login or register

	var toggle = document.querySelector('#login-form-toggle');
	var title = document.querySelector('#login-form h2');
	var name = document.querySelector('#login-form-name');
	var password = document.querySelector('#login-form-password');
	var confirm = document.querySelector('#login-form-confirm');
	var button = document.querySelector('#login-form button');
	var form = document.querySelector('#login-form');
	var error = document.querySelector('#login-form p');

	var errorDisplayed = false;
	var fixingMethod = function() {};

	function emptyFields() {
		name.value = '';
		password.value = '';
		confirm.value = '';
	}

	toggle.addEventListener('click', function() {
		if(onLogin) {
			// now register view
			onLogin = false;
			toggle.innerHTML = 'Log-in';
			title.innerHTML = 'SUBSCRIBE';
			button.innerHTML = 'Subscribe';
			emptyFields();
			confirm.classList.remove('hide');
		}
		else {
			// now login view
			onLogin = true;
			toggle.innerHTML = "Subscribe";
			title.innerHTML = "LOG IN";
			button.innerHTML = 'Log in';
			emptyFields();
			confirm.classList.add('hide');
		}
	});

	form.addEventListener('submit', function(e) {
		if(name.value.trim().length === 0 || password.value.length === 0) {
			e.preventDefault();
			return;
		}
		else if(!onLogin) { // the user is subscribing
			if(confirm.value.length === 0) {
				e.preventDefault();
				return;
			}
			// check if the 2 passwords are the same
			if(password.value === confirm.value) {
				// @todo send the request
				console.log('envoi de la requête de inscription');
				emptyFields();
			}
			else {
				showError('The password field and the password confirmation field do not match. Please try again.');
				setErrorFixingMethod(mismatchingPasswordsFix);
				e.preventDefault();
			}
		}
		else {
			// @todo send the request
			console.log('envoie de la requête de connexion');
			emptyFields();
		}

		e.preventDefault();
	});

	function showError(errorMessage) {
		errorDisplayed = true;
		error.innerHTML = errorMessage;
		error.style.display = 'block';
	}

	function hideError() {
		errorDisplayed = false;
		error.style.display = 'none';
	}

	function setErrorFixingMethod(method) {
		fixingMethod = method;
	}

	function mismatchingPasswordsFix() {
		if(password.value === confirm.value) {
			hideError();
		}
	}

	function errorFixingHandler() {
		if(errorDisplayed) {
			fixingMethod();
		}
	}

	name.addEventListener('input', errorFixingHandler);
	password.addEventListener('input', errorFixingHandler);
	confirm.addEventListener('input', errorFixingHandler);
})();