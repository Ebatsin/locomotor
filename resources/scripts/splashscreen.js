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
			YUI().use('node', 'transition', function(Y) {
				Y.one(splash).transition({
					easing: 'ease-in-out',
					duration: 0.7,
					opacity: 0
				}, function() {
					shown = false;
					if(callback) callback();
				});
			});
		},
		show: function(callback) {
			shown = true;
			YUI().use('node', 'transition', function(Y) {
				Y.one(splash).transition({
					easing: 'ease',
					duration: 0.7,
					opacity: '1'
				}, function() {
					if(callback) callback();
				});
			});
		},
		isShown: function() {
			return shown;
		}
	};
})();