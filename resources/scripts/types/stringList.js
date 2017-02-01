
/**
* Use the elem given as a parameter to display the range. Does not change the elem, only its content
* calling destroy() will give the elem as it was at the begining
* @param elem the elem in which the input will be inserted
* @param list the strings to display alongside their IDs ({id: string, ...})
*/
function StringList(elem, list) {
	var parent = document.createElement('ul');
	var that = this;

	parent.classList.add('stringlist');

	this.init = function() {
		for(var i in list) {
			if(list.hasOwnProperty(i)) {
				(function() {
					var item = document.createElement('li');
					item.setAttribute('data-id', i);
					item.innerHTML = list[i];

					item.addEventListener('click', function() {
						item.classList.toggle('selected');
						console.log(JSON.stringify(that.getIDs()));
					});

					parent.appendChild(item);
				})();
			}

			elem.appendChild(parent);
		}
	};

	this.destroy = function() {

	};

	/**
	* Gives the list of the ids selected
	*/
	this.setSelected = function(list) {
		var allItem = parent.querySelectorAll('li');
		for(var i = 0; i < allItem.length; ++i) {
			if(list.indexOf(parseInt(allItem[i].getAttribute('data-id'))) !== -1) {
				allItem[i].classList.add('selected');
			}
			else {
				allItem[i].classList.remove('selected');
			}
		}
	};

	this.getIDs = function() {
		var selected = parent.querySelectorAll('.selected');
		var ids = [];
		for(var i = 0; i < selected.length; ++i) {
			ids.push(parseInt(selected[i].getAttribute('data-id')));
		}

		return ids;
	};
}