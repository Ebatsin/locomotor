
/**
* Use the elem given as a parameter to display the range. Does not change the elem, only its content
* calling destroy() will give the elem as it was at the begining
* @param elem the elem in which the input will be inserted
*/
function PoorFloatRange(elem, lowBound, upBound) {

	var that = this;

	var parent = document.createElement('div');
	var before = document.createElement('div');
	var after = document.createElement('div');
	var middle = document.createElement('div');
	var inputMin = document.createElement('input');
	var inputMax = document.createElement('input');

	var sub = function(){};


	parent.classList.add('poor-float-range');
	before.classList.add('poor-float-range-before');
	after.classList.add('poor-float-range-after');
	middle.classList.add('poor-float-range-middle');
	inputMin.classList.add('poor-float-range-input');
	inputMax.classList.add('poor-float-range-input');

	before.innerHTML = lowBound + ' ≤';
	middle. innerHTML = ' ≤ ';
	after.innerHTML = '≤ ' + upBound;

	var checkValidity = function() {
		return parseFloat(inputMin.value) >= lowBound && parseFloat(inputMax.value) >= parseFloat(inputMin.value) && parseFloat(inputMax.value) <= upBound;
	};

	this.init = function() {
		parent.appendChild(before);
		parent.appendChild(inputMin);
		parent.appendChild(middle);
		parent.appendChild(inputMax);
		parent.appendChild(after);

		inputMin.addEventListener('keyup', function(e) {
			if(e.keyCode == 13 && that.isValid()) {
				sub();
			}
			else if(e.keyCode == 13) {
				inputMax.focus();
			}
		});

		inputMax.addEventListener('keyup', function(e) {
			if(e.keyCode == 13 && that.isValid()) {
				sub();
			}
		});
		
		elem.appendChild(parent);

		inputMin.focus();
	};

	this.destroy = function() {
		parent.parentNode.removeChild(parent);
	};

	this.getMinValue = function() {
		return parseFloat(inputMin.value);
	};

	this.getMaxValue = function() {
		return parseFloat(inputMax.value);
	};

	this.setMinValue = function(value) {
		inputMin.value = value;
	};

	this.setMaxValue = function(value) {
		inputMax.value = value;
	};

	this.isValid = checkValidity;

	this.onSubmit = function(callback) {
		sub = callback;
	};
}