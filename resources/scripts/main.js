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

	window.formatUnit = function(value, unitID, fixed) {
		for(var i = 0; i < window.units.length; ++i) {
			if(units[i]['_id'] == unitID) {
				// find the base unit
				for(var j = 0; j < units[i].alt.length; ++j) {
					if(units[i].alt[j].factor == 1) {
						if(fixed !== undefined) {
							return value.toFixed(fixed) + units[i].alt[j].shortName;
						}
						else {
							return value + units[i].alt[j].shortName;
						}						
					}
				}
			}
		}
		return fixed !== undefined ? value.toFixed(fixed) : value;
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


	// @TODO remove that
	// debug
	var size = document.createElement('div');
	size.style.position = "absolute";
	size.style.right = 0;
	size.style.bottom = 0;
	size.style['z-index'] = 10001;

	document.body.appendChild(size);

	size.innerHTML = $(window).width() + 'x' + $(window).height();

	window.addEventListener('resize', function(e) {
		size.innerHTML = $(window).width() + 'x' + $(window).height();
	});

})();