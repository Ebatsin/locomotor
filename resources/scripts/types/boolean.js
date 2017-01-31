
/**
* Use the elem given as a parameter to display the range. Does not change the elem, only its content
* calling destroy() will give the elem as it was at the begining
* @param elem the elem in which the input will be inserted
*/
function Boolean(elem) {

	var parent = document.createElement('div');
	var yes = document.createElement('div');
	var no = document.createElement('div');
	var transfert = document.createElement('div');

	var dummyYes = document.createElement('div');
	var dummyNo = document.createElement('div');

	parent.classList.add('boolean');
	yes.classList.add('boolean-option');
	no.classList.add('boolean-option');
	transfert.classList.add('boolean-trans');
	dummyYes.classList.add('boolean-dummy');
	dummyNo.classList.add('boolean-dummy');
	dummyYes.classList.add('boolean-yes');
	dummyNo.classList.add('boolean-no');

	yes.innerHTML = 'Yes';
	no.innerHTML = 'No';

	var checked = false;
	no.classList.add('checked');

	this.init = function() {
		parent.appendChild(dummyYes);
		parent.appendChild(dummyNo);
		parent.appendChild(transfert);
		parent.appendChild(yes);
		parent.appendChild(no);

		elem.appendChild(parent);

		dummyYes.addEventListener('click', function() {
			if(checked) {
				return;
			}

			no.style.color = 'hsl(180, 60%, 30%)';
			$(transfert).animate({
				'margin-left': '13%',
			}, 200, function() {
				yes.style.color = 'hsl(0, 0%, 95%)';
			});

			$(yes).animate({
				'color': 'hsl(0, 0%, 95%)'
			})

			checked = true;
			yes.classList.add('checked');
			no.classList.remove('checked');
		});

		dummyNo.addEventListener('click', function() {
			if(!checked) {
				return;
			}

			yes.style.color = 'hsl(180, 60%, 30%)';
			$(transfert).animate({
				'margin-left': '56%'
			}, 200, function() {
				no.style.color = 'hsl(0, 0%, 95%)';
			});

			checked = false;
			yes.classList.remove('checked');
			no.classList.add('checked');
		});
	};

	this.destroy = function() {

	};

	this.isChecked = function() {
		return checked;
	};

	this.setChecked = function(ch) {
		checked = ch;
	};
}