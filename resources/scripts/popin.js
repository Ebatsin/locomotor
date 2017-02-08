function Popin() {
	var that = this;

	var _mary = document.querySelector('#mary');
	var _overlay = document.querySelector('#mary-overlay');
	var _title = mary.querySelector('h1');
	var _message = mary.querySelector('p');
	var _yes = mary.querySelector('.yes');
	var _no = mary.querySelector('.no');

	var yesCallback = function(){};
	var noCallback = function(){};

	_yes.addEventListener('click', function() {
		yesCallback();
		that.close();
	});

	_no.addEventListener('click', function() {
		noCallback();
		that.close();
	});

	_overlay.addEventListener('click', function() {
		noCallback();
		that.close();
	});

	this.open = function(title, message, onYes, onNo) {
		_title.innerHTML = title;
		_message.innerHTML = message;
		if(onYes) {
			yesCallback = onYes;
		}

		if(onNo) {
			noCallback = onNo;
		}

		_overlay.style['pointer-events'] = 'auto';
		$(_overlay).animate({
			opacity: 0.3
		}, 200);

		_mary.style['pointer-events'] = 'auto';
		$(_mary).animate({
			opacity: 1
		}, 200);
	};

	this.close = function() {
		_overlay.style['pointer-events'] = 'none';
		$(_overlay).animate({
			opacity: 0
		}, 200);

		_mary.style['pointer-events'] = 'none';
		$(_mary).animate({
			opacity: 0
		}, 200);
	};
}