/**
* Handle all the modules and register them
*/

(function() {
	window.modules = {};
	window.views = [];

	var zindex = 1100; // the Zindex value of the topmost popin

	window.initModules = function() {
		for(var i in window.modules) {
			if(window.modules.hasOwnProperty(i)) {
				window.modules[i].init();
			}
		}

		document.body.style.fontFamily = 'Roboto';

		loadView('login', {
			mode: 'login'
		});
	};

	/**
	* Gives the name of a module that can be loaded as a view (must have the 'load' method defined)
	*/
	window.registerView = function(view) {
		views.push(view);
	};

	window.loadView = function(view, params) {
		if(window.views.indexOf(view) != -1) {
			modules.help.close();
			window.modules[view].load(params);
		}
	};

	window.hideAllViews = function() {
		var pages = document.querySelectorAll('.page');
		for(var i = 0; i < pages.length; ++i) {
			pages[i].classList.add('hide');
		}
	}

	window.getNextZIndex = function() {
		++zindex;
		return zindex;
	}

	window.getBestAlt = function(value, unit) {
		var currentBest = 1000000000000;
		var id = 0;
		for(var i = 0; i < unit.length; ++i) {
			var grade = value / unit[i].factor;
			if(grade >= 1 && grade < currentBest) {
				currentBest = grade;
				id = i;
			}
		}
		return unit[id];
	};

	window.formatUnit = function(value, unitID, fixed) {
		for(var i = 0; i < window.units.length; ++i) {
			if(units[i]['_id'] == unitID) {
				var best = getBestAlt(value, units[i].alt);
				var factoredValue = value / best.factor;
				var term = best.shortName;

				if(factoredValue.toFixed(2).indexOf('.00') != -1) {
					return factoredValue.toFixed(0) + term;
				}
				else {
					return factoredValue.toFixed(2) + term;
				}
			}
		}
		return fixed !== undefined ? value.toFixed(fixed) : value;
	};

	/**
	* @param value The value
	* @param str a string that contains ${unit} in which the long unit will me placed and ${value} in which the value will be placed
	*/
	window.formatLongUnit = function(value, str, unitID) {
		for(var i = 0; i < window.units.length; ++i) {
			if(units[i]['_id'] == unitID) {
				var best = getBestAlt(value, units[i].alt);
				var factoredValue = value / best.factor;
				var term = best.longName.replace('%s', factoredValue >= 2 ? 's': '');
				
				if(factoredValue.toFixed(2).indexOf('.00') != -1) {
					return str.replace('${unit}', term).replace('${value}', factoredValue.toFixed(0));
				}
				else {
					return str.replace('${unit}', term).replace('${value}', factoredValue.toFixed(2));
				}
			}
		}
		return str.replace('${unit}', '').replace('${value}', value);
	};

	function checkReady() {
		console.log('vérification de la readyness');
		if(window.initWhenReady) {
			setTimeout(initModules, 100);
		}
		else {
			setTimeout(checkReady, 100);
		}
	}

	checkReady();
})();