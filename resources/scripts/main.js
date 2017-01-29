/**
* Handle all the modules and register them
*/

(function() {
	window.modules = {};

	window.initModules = function() {
		for(var i in window.modules) {
			if(window.modules.hasOwnProperty(i)) {
				window.modules[i].init();
			}
		}
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