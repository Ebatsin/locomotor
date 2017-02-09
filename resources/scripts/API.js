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

		if(password) {
			app.auth(name.trim().toLowerCase(), password, prom.getId());
		}
		else {
			app.tokenAuth(name.trim(), prom.getId());
		}


		return prom;
	},

	register: function(name, password) {
		var prom = new JPromises();

		app.register(name.trim().toLowerCase(), password, prom.getId());

		return prom;
	},

	getModel: function() {
		var prom = new JPromises();

		app.getModel(app.getShortToken(), prom.getId());

		return prom;
	},
	search: function(criteria) {
		var prom = new JPromises();

		app.search(app.getShortToken(), criteria, prom.getId());

		return prom;
	},
	getImageUrl: function(url) {
		var prom = new JPromises();

		app.getImage(app.getShortToken(), url, prom.getId());

		return prom;
	},
	getItem: function(id) {
		var prom = new JPromises();

		app.getItem(app.getShortToken(), id, prom.getId());

		return prom;
	},
	getUniverse: function(id) {
		var prom = new JPromises();

		app.getUniverse(app.getShortToken(), id, prom.getId());

		return prom;
	},
	getUserInfo: function() {
		var prom = new JPromises();

		app.getUserInfo(app.getShortToken(), prom.getId());

		return prom;
	},
	changeUsername: function(newName) {
		var prom = new JPromises();

		app.changeUsername(app.getShortToken(), newName, prom.getId());

		return prom;
	},
	changePassword: function(oldPass, newPass) {
		var prom = new JPromises();

		app.changePassword(app.getShortToken(), oldPass, newPass, prom.getId());

		return prom;
	},
	book: function(id, startDate, endDate, quantity) {
		var prom = new JPromises();

		app.book(app.getShortToken(), id, startDate, endDate, quantity, prom.getId());

		return prom;
	},
	getAllBooking: function() {
		var prom = new JPromises();

		app.getAllBooking(app.getShortToken(), prom.getId());

		return prom;
	},
	removeBooking: function(id) {
		var prom = new JPromises();

		app.removeBooking(app.getShortToken(), id, prom.getId());

		return prom;
	},
	getUnits: function() {
		var prom = new JPromises();

		app.getUnits(app.getShortToken(), prom.getId());

		return prom;
	},

	/* ----------------------------------- ADMIN ------------------------------- */

	adminAuth: function(name, password) {
		var prom = new JPromises();

		if(password) {
			app.adminAuth(name.trim().toLowerCase(), password, prom.getId());
		}
		else {
			app.adminTokenAuth(name.trim(), prom.getId());
		}

		return prom;
	},
	getAllItems: function() {
		var prom = new JPromises();

		app.getAllItems(app.getShortToken(), prom.getId());

		return prom;
	},
	getAllUniverses: function() {
		var prom = new JPromises();

		app.getAllUniverses(app.getShortToken(), prom.getId());

		return prom;
	},
	addItem: function(item) {
		var prom = new JPromises();

		app.addItem(app.getShortToken(), JSON.stringify(item), prom.getId());

		return prom;
	},
	updateItem: function(item) {
		var prom = new JPromises();

		app.updateItem(app.getShortToken(), JSON.stringify(item), prom.getId());

		return prom;
	},
	removeItem: function(item) {
		var prom = new JPromises();

		app.removeItem(app.getShortToken(), item, prom.getId());

		return prom;
	},
	removeUniverse: function(universe) {
		var prom = new JPromises();

		app.removeUniverse(app.getShortToken(), universe, prom.getId());

		return prom;
	},
	updateUniverse: function(item) {
		var prom = new JPromises();

		app.updateUniverse(app.getShortToken(), JSON.stringify(item), prom.getId());

		return prom;
	},
};