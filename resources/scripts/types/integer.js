
/**
* Use the elem given as a parameter to display the range. Does not change the elem, only its content
* calling destroy() will give the elem as it was at the begining
* @param elem the elem in which the input will be inserted
*/
function Integer(elem, lowBound, upBound) {

	var that = this;

	var parent = document.createElement('div');
	var before = document.createElement('div');
	var after = document.createElement('div');
	var input = document.createElement('input');


	parent.classList.add('integer');
	before.classList.add('integer-before');
	after.classList.add('integer-after');
	input.classList.add('integer-input');

	before.innerHTML = lowBound + ' ≤';
	after.innerHTML = '≤ ' + upBound;

	var sub = function(){};

	var checkValidity = function() {
		return parseFloat(input.value) == parseInt(input.value) && parseInt(input.value) >= lowBound && parseInt(input.value) <= upBound;
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
		return parseInt(input.value);
	};

	this.setValue = function(value) {
		input.value = value;
	};

	this.isValid = checkValidity;

	this.onSubmit = function(callback) {
		sub = callback;
	};
}