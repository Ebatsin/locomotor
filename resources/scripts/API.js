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

	var that = this;

	this.resolve = function(data) {
		console.log('received on js');
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

	// the data is of the form {success: boolean}
	this.jsonResolve = function(data) {
		if(data.success === "true") {
			that.resolve(data);
		}
		else {
			that.reject(data);
		}
	};

	this.then = function(callback) {
		if(fullfilled && success) {
			callback(answer);
		}
		else if(!fullfilled) {
			onSuccess = callback;
		}

		return this;
	};

	this.catch = function(callback) {
		if(fullfilled && !success) {
			callback(answer);
		}
		else if(!fullfilled) {
			onFailure = callback;
		}

		return this;
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
	},

	register: function(name, password) {
		var prom = new JPromises();
		
		app.register(name, password, prom.getId());

		return prom;
	}
};