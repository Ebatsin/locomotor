
/**
* Use the elem given as a parameter to display the range. Does not change the elem, only its content
* calling destroy() will give the elem as it was at the begining
* @param elem the elem in which the input will be inserted
*/
function Float(elem, lowBound, upBound) {

	var that = this;

	var parent = document.createElement('div');
	var before = document.createElement('div');
	var after = document.createElement('div');
	var input = document.createElement('input');


	parent.classList.add('float');
	before.classList.add('float-before');
	after.classList.add('float-after');
	input.classList.add('float-input');

	var sub = function(){};

	before.innerHTML = lowBound + ' ≤';
	after.innerHTML = '≤ ' + upBound;

	var checkValidity = function() {
		return parseFloat(input.value) >= lowBound && parseFloat(input.value) <= upBound;
	};

	this.init = function() {
		parent.appendChild(before);
		parent.appendChild(input);
		parent.appendChild(after);

		input.addEventListener('keyup', function(e) {
			if(e.keyCode == 13 && that.isValid()) {
				sub();
			}
		});

		elem.appendChild(parent);

		input.focus();
	};

	this.destroy = function() {
		parent.parentNode.removeChild(parent);
	};

	this.getValue = function() {
		return parseFloat(input.value);
	};

	this.setValue = function(value) {
		input.value = value;
	};

	this.isValid = checkValidity;

	this.onSubmit = function(callback) {
		sub = callback;
	};
}