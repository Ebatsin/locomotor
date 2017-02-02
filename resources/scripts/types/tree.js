
/**
* Use the elem given as a parameter to display the range. Does not change the elem, only its content
* calling destroy() will give the elem as it was at the begining
* @param elem the elem in which the input will be inserted
* @param an array of nodes [{value: "...", id: n, ?children: []}]
*/
function Tree(elem, tree) {
	console.log('meeeeeeeeeeeeeeeeeeh');
	var parent = document.createElement('ul');
	parent.classList.add('tree-root');

	var that = this;

	
	this.init = function() {
		for(var i = 0; i < tree.length; ++i) {
			parent.appendChild(that.createSubTree(tree[i]));
		}

		elem.appendChild(parent);
	};

	this.destroy = function() {
		parent.parentNode.removeChild(parent);
	};

	/**
	* takes a node and gen an element that contains it
	*/
	this.createSubTree = function(node) {
		return (function() {
			var elem = document.createElement('li');
			var name = document.createElement('div');

			elem.classList.add('tree-node');
			name.classList.add('tree-value');

			name.innerHTML = node.value;

			elem.appendChild(name);

			if(node.children) {
				var children = document.createElement('ul');
				children.classList.add('tree-children');
				for(var i = 0; i < node.children.length; ++i) {
					children.appendChild(that.createSubTree(node.children[i]));
				}
				elem.appendChild(children);
			}

				name.addEventListener("mouseover", function() {
					$(name).css('background', 'hsl(180, 60%, 15%)');
					$(elem).find('.tree-children > .tree-node > .tree-value').css('background', 'hsl(180, 60%, 15%)');
				});

				name.addEventListener('mouseout', function() {
					$(name).css('background', 'hsl(180, 60%, 30%)');
					$(elem).find('.tree-children > .tree-node > .tree-value').css('background', 'hsl(180, 60%, 30%)');
				});

			return elem;
		})();
	};
}