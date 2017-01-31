
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

	parent.classList.add('boolean');
	yes.classList.add('boolean-option');
	no.classList.add('boolean-option');
	transfert.classList.add('boolean-trans');

	yes.innerHTML = 'Yes';
	no.innerHTML = 'No';

	var checked = false;
	no.classList.add('checked');

	this.init = function() {
		parent.appendChild(transfert);
		parent.appendChild(yes);
		parent.appendChild(no);

		elem.appendChild(parent);

		yes.addEventListener('click', function() {
			if(checked) {
				return;
			}

			$(transfert).animate({
				'margin-left': '13%'
			}, 200);

			checked = true;
			yes.classList.add('checked');
			no.classList.remove('checked');
		});

		no.addEventListener('click', function() {
			if(!checked) {
				return;
			}

			$(transfert).animate({
				'margin-left': '56%'
			}, 200);

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