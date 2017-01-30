
/**
* Use the elem given as a parameter to display the range. Does not change the elem, only its content
* calling destroy() will give the elem as it was at the begining
*/
function Range(elem, minBound, maxBound, step, signiDigits) {
	var bar = document.createElement('div');
	var subBar = document.createElement('div');

	var minKnob = document.createElement('div');
	var maxKnob = document.createElement('div');

	var minLabel = document.createElement('div');
	var maxLabel = document.createElement('div');

	// styling
	bar.classList.add('range-bar');
	subBar.classList.add('range-subbar');
	minKnob.classList.add('range-knob');
	maxKnob.classList.add('range-knob');

	minLabel.classList.add('range-label');
	maxLabel.classList.add('range-label');

	var min = minBound + (maxBound - minBound) * 0.25;
	var max = maxBound - (maxBound - minBound) * 0.25;

	// move handlers
	var minMoving = false; // wether the min knob is moving
	var maxMoving = false; // wether the max knob is moving

	var posBegin = 0; // the position of the knob at the begining of the movement
	var mouseBegin = 0; // the position of the mouse at the begining of the movement


	this.init = function() {
		bar.appendChild(subBar);
		bar.appendChild(minKnob);
		bar.appendChild(maxKnob);
		bar.appendChild(minLabel);
		bar.appendChild(maxLabel);

		elem.appendChild(bar);

		minKnob.addEventListener('mousedown', function(e) {
			if(minMoving || maxMoving) {
				return;
			}

			minMoving = true;
			posBegin = min;
			mouseBegin = e.clientX;
		});

		maxKnob.addEventListener('mousedown', function(e) {
			if(minMoving || maxMoving) {
				return;
			}

			maxMoving = true;
			posBegin = max;
			mouseBegin = e.clientX;
		});

		document.addEventListener('mousemove', function(e) {
			if(minMoving) {
				var tmpMin = (e.clientX - mouseBegin) / (bar.offsetWidth / (maxBound - minBound)) + posBegin;
				if(tmpMin <= max && tmpMin >= minBound) {
					min = tmpMin;
					updatePosition();
				}
				else if(tmpMin < minBound) {
					min = minBound;
					updatePosition();
				}
			}
			else if(maxMoving) {
				var tmpMax = (e.clientX - mouseBegin) / (bar.offsetWidth / (maxBound - minBound)) + posBegin;
				if(tmpMax >= min && tmpMax <= maxBound) {
					max = tmpMax;
					updatePosition();
				}
				else if(tmpMax > maxBound) {
					max = maxBound;
					updatePosition();
				}
			}
		});

		document.addEventListener('mouseup', function() {
			minMoving = false;
			maxMoving = false;
		});

		updatePosition();
	};

	this.destroy = function() {

	};

	this.getMin = function() {
		return min;
	};

	this.getMax = function() {
		return max;
	};

	this.setMin = function(value) {
		if(value < minBound || value > maxBound) {
			return;
		}

		if(value <= max) {
			min = value;
		}
		else {
			min = value;
			max = value;
		}
	};

	this.setMax = function(value) {
		if(value < minBound || value > maxBound) {
			return;
		}

		if(value >= min) {
			max = value;
		}
		else {
			min = value;
			max = value;
		}
	};

	function updatePosition() {
		// do the rounding
		min = Math.round(min / step) * step;
		max = Math.round(max / step) * step;

		var minPos = ((min - minBound) / (maxBound - minBound)) * 100;
		var maxPos = ((max - minBound) / (maxBound - minBound)) * 100;
		minKnob.style.marginLeft = 'calc(' + minPos + '%' + ' - ' + (minKnob.offsetWidth/2) + 'px)';
		maxKnob.style.marginLeft = 'calc(' + maxPos + '%' + ' - ' + (maxKnob.offsetWidth/2) + 'px)';
		subBar.style.marginLeft = minPos + '%';
		subBar.style.width = (maxPos - minPos) + '%';

		minLabel.style.marginLeft = 'calc(' + minPos + '%' + ' - ' + (minKnob.offsetWidth/2) + 'px)';
		minLabel.innerHTML = signiDigits ? min.toFixed(signiDigits) : min;

		maxLabel.style.marginLeft = 'calc(' + maxPos + '%' + ' - ' + (maxKnob.offsetWidth/2) + 'px)';
		maxLabel.innerHTML = signiDigits ? max.toFixed(signiDigits) : max;
	}
}