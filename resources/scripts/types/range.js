
/**
* Use the elem given as a parameter to display the range. Does not change the elem, only its content
* calling destroy() will give the elem as it was at the begining
* @param elem the elem in which the input will be inserted
* @param minBound The min bound of the input range
* @param maxBoud The max bound of the input range
* @param step The steps between the selectable values or an array containing the strings reprensting the values, each one beeing a step
* @param signidigits optional, the number of digits after the point printed
*/
function Range(elem, minBound, maxBound, step, signiDigits) {
	var that = this;

	var bar = document.createElement('div');
	var subBar = document.createElement('div');

	var minKnob = document.createElement('div');
	var maxKnob = document.createElement('div');

	var minLabel = document.createElement('div');
	var maxLabel = document.createElement('div');
	var innerMinLabel = document.createElement('div');
	var innerMaxLabel = document.createElement('div');

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

	// expo ?
	var expoUsed = false;
	var offset = 0;

	var onChangeEvent = function(){};

	if(!step) {
		step = 1;
	}

	if(!signiDigits) {
		signiDigits = 0;
	}

	var language = null;
	if(step instanceof Array) {
		language = step;
		step = (maxBound - minBound) / (language.length - 1);
	}

	/**
	* If expo is true, the expo function is used to show the data
	*/
	this.init = function(expo) {
		if(expo && language == null) {
			offset = 1 - minBound;
			expoUsed = true;
			minBound = Math.log(minBound + offset);
			maxBound = Math.log(maxBound + offset);

			min = Math.log(min + offset);
			max = Math.log(max + offset);
		}

		minLabel.appendChild(innerMinLabel);
		maxLabel.appendChild(innerMaxLabel);

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
		bar.parentNode.removeChild(bar);
	};

	this.getMin = function() {
		return expoUsed ? (Math.exp(min) - offset) : min;
	};

	this.getMax = function() {
		return expoUsed ? (Math.exp(max) - offset) : max;
	};

	this.setMin = function(value) {
		if(expoUsed) {
			value = Math.log(value + offset)
		}

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

		updatePosition();
	};

	this.setMax = function(value) {
		if(expoUsed) {
			value = Math.log(value + offset)
		}

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

		updatePosition();
	};

	this.onChange = function(callback) {
		onChangeEvent = callback;
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
		maxLabel.style.marginLeft = 'calc(' + maxPos + '%' + ' - ' + (maxKnob.offsetWidth/2) + 'px)';

		if(language !== null) {
			innerMinLabel.innerHTML = language[Math.floor(min / step)];
			innerMaxLabel.innerHTML = language[Math.floor(max /step)];

			onChangeEvent({
				min: min,
				max: max
			});
		}
		else if(expoUsed) {
			var minUsed = Math.exp(min) - offset;
			var maxUsed = Math.exp(max) - offset;
			innerMinLabel.innerHTML = minUsed.toFixed(signiDigits);
			innerMaxLabel.innerHTML = maxUsed.toFixed(signiDigits);

			onChangeEvent({
				'min': (signiDigits == 0 ? Math.round(minUsed) : minUsed),
				'max': (signiDigits == 0 ? Math.round(maxUsed) : maxUsed)
			});
		}
		else {
			innerMinLabel.innerHTML = min.toFixed(signiDigits);
			innerMaxLabel.innerHTML = max.toFixed(signiDigits);

			onChangeEvent({
				min: signiDigits == 0 ? Math.round(min) : min,
				max: signiDigits == 0 ? Math.round(max) : max
			});
		}
	}
}