function JPromises() {
	var fullfilled = false;
	var success = false;
	var onSuccess = null;
	var onFailure = null;
	var answer = null;

	if(!window.promises) {
		window.promises = [];
	}

	var id = window.promises.length;
	window.promises.push(this);

	this.resolve = function(data) {
		fullfilled = true;
		success = true;
		if(onSuccess != null) {
			onSuccess(data);
		}
		else {
			answer = data;
		}
	};

	this.reject = function(data) {
		fullfilled = true;
		success = false;
		if(onFailure != null) {
			onFailure(data);
		}
		else {
			answer = data;
		}
	};

	this.then = function(callback) {
		if(fullfilled && success) {
			callback(answer);
		}
		else if(!fullfilled) {
			onSuccess = callback;
		}
	};

	this.catch = function(callback) {
		if(fullfilled && !success) {
			callback(answer);
		}
		else if(!fullfilled) {
			onFailure = callback;
		}
	}

	this.getId = function() {
		return id;
	}
}

window.API = {
	auth: function(name, password) {
		var prom = new JPromises();

		app.auth(name, password, prom.getId());

		return prom;
	}
};