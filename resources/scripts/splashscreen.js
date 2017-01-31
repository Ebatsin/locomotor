(function() {
	var splash = document.querySelector('#splashscreen');

	var shown = true;

	if(!window.modules) {
		window.modules = {};
	}

	window.modules['splash'] = {
		init: function() {
			splash.style.display = 'block';
		},
		hide: function(callback) {
			$(splash).animate({
				opacity: 0
			}, 700, function() {
				shown = false;
				if(callback) callback();
			});
		},
		show: function(callback) {
			shown = true;
			$(splash).animate({
				opacity: 1
			}, 700, function() {
				if(callback) callback();
			});
		},
		isShown: function() {
			return shown;
		}
	};
})();