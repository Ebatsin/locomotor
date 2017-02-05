
/**
* Use the elem given as a parameter to display the range. Does not change the elem, only its content
* calling destroy() will give the elem as it was at the begining
* @param elem the elem in which the input will be inserted
* @param an array of nodes [{value: "...", id: n, ?children: []}]
*/
function Tree(elem, tree) {
	var parent = document.createElement('ul');
	parent.classList.add('tree-root');

	var that = this;
	var treeBackup = tree;
	tree = tree.children;

	var onChangeEvent = function() {};

	
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
			node.open = false;

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

			node.select = function() {
				$(name).css('background', 'hsl(180, 60%, 30%)');
				$(name).css('color', 'hsl(0, 0%, 90%)');
				$(children).slideDown(200);
				node.open = true;
			};

			node.unselect = function() {
				$(children).slideUp(200);
				$(name).css('color', 'hsl(0, 0%, 0%)');
				$(name).css('background', 'hsl(180, 10%, 80%)');
				node.open = false;
			};

			$(children).slideUp(0);

			name.addEventListener('click', function() {
				if(node.open) {
					node.unselect();
				}
				else {
					node.select();
				}
				onChangeEvent(that.getTree());
			});

			name.addEventListener("mouseover", function() {
				if(!node.open) {
					$(name).css('background', 'hsl(180, 10%, 80%)');
				}
				//$(elem).find('.tree-children > .tree-node > .tree-value').css('background', 'hsl(180, 60%, 15%)');
			});

			name.addEventListener('mouseout', function() {
				if(!node.open) {
					$(name).css('background', 'hsl(180, 10%, 85%)');
				}
				//$(elem).find('.tree-children > .tree-node > .tree-value').css('background', 'hsl(180, 60%, 30%)');
			});

			return elem;
		})();
	};

	this.getTree = function() {
		var nodes = [];
		var oneSelected = false;

		for(var i = 0; i < tree.length; ++i) {
			if(tree[i].open) {
				oneSelected = true;
				break;
			}
		}

		for(var i = 0; i < tree.length; ++i) {
			if(tree[i].open || !oneSelected) {
				nodes.push(that.getNode(tree[i], !oneSelected));
			}
		}

		return {
			id: treeBackup.id,
			children: nodes
		};
	};

	this.getNode = function(node, force) {

		if(!node.children) {
			return {
				id: node.id
			};
		}

		if(force) { // add the node no matter what
			var nodes = [];

			for(var i = 0; i < node.children.length; ++i) {
				nodes.push(that.getNode(node.children[i], true));
			}

			return {
				id: node.id,
				children: nodes
			};
		}
		else {
			var nodes = [];
			var oneSelected = false;

			for(var i = 0; i < node.children.length; ++i) {
				if(node.children[i].open) {
					oneSelected = true;
					break;
				}
			}

			for(var i = 0; i < node.children.length; ++i) {
				if(node.children[i].open || !oneSelected) {
					nodes.push(that.getNode(node.children[i], !oneSelected));
				}
			}

			return {
				id: node.id,
				children: nodes
			};
		}
	};

	this.selectTree = function(t, equivalentNode) {
		if(!equivalentNode) {
			equivalentNode = treeBackup;
		}

		if(!t.children) {
			equivalentNode.select();
		}
		else {
			// check if all the children are selected. If yes, do not print it
			if(t.children.length === equivalentNode.children.length) {
				return;
			}

			var j = 0;
			for(var i = 0; i < t.children.length; ++i) {

				while(t.children[i].id !== equivalentNode.children[j].id && j < equivalentNode.children.length) {
					++j;
				}

				if(t.children[i].id === equivalentNode.children[j].id) {
					equivalentNode.children[j].select();
					that.selectTree(t.children[i], equivalentNode.children[j]);
				}
			}
		}			
	};

	this.onChange = function(callback) {
		onChangeEvent = callback;
		callback(that.getTree());
	};
}