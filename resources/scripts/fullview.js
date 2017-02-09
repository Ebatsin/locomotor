(function() {
	var view = document.querySelector('#fullview-page');
	var header = view.querySelector('#fullview-header');
	var itemImage = header.querySelector('img');
	var itemName = header.querySelector('h1');
	var itemPrice = header.querySelector('#fullview-price');
	var itemDescription = view.querySelector('#fullview-description');
	var itemSpecs = view.querySelector('#fullview-specs');

	var universe = view.querySelector('#fullview-universe');
	var universeImage = universe.querySelector('img');
	var universeName = universe.querySelector('h2');
	var universeDescription = universe.querySelector('p');

	var book = view.querySelector('#fullview-name-container button');

	window.registerView('fullview');

	if(!window.modules) {
		window.modules = {};
	}

	var id, name;

	window.modules['fullview'] = {
		init: function() {
			console.log('initialisation du module fullview');

			book.addEventListener('click', function() {
				loadView('booking', {
					id: id,
					name: name
				});
			});
		},
		/**
		* Load the login view.
		* @param params An object that contains the parameters
		* mode: 'login' | 'register'
		*/
		load: function(params) {
			view.classList.remove('hide');
			view.style['z-index'] = getNextZIndex();
			modules.help.pushContext('fullview');

			id = params;

			modules.menu.pushBackArrow(function() {
				modules.fullview.unload();
			});

			API.getItem(params).then(function(data) {
				modules.fullview.print(data.data.item);
			}).catch(function(data) {
				modules.fullview.unload();
			});
		},
		unload: function() {
			modules.menu.popBackArrow();
			view.classList.add('hide');
			modules.help.popContext();
		},
		print: function(data) {
			API.getImageUrl(data.image).then(function(data) {
				itemImage.src = data.replace('resources/', '');
			});

			API.getUniverse(data.universeID).then(function(data) {
				universeName.innerHTML = data.data.universe.name;
				universeDescription.innerHTML = data.data.universe.description;
				API.getImageUrl(data.data.universe.image).then(function(data) {
					universeImage.src = data.replace('resources/', '');
				});
			});

			name = data.name;

			itemName.innerHTML = data.name;
			itemDescription.innerHTML = data.description;
			itemSpecs.innerHTML = '';

			for(var i = 0; i < data.categories.length; ++i) {
				(function(category) {
					var container = document.createElement('li');
					var title = document.createElement('div');
					var content = document.createElement('ul');

					container.classList.add('category');
					title.classList.add('category-name');
					content.classList.add('category-content');

					title.innerHTML = category.name === '_self_' ? 'Miscellaneous' : category.name;

					for(var j = 0; j < category.criteria.length; ++j) {
						(function(criterion) {
							var item = document.createElement('li');
							var name = document.createElement('div');
							var value = document.createElement('div');

							item.classList.add('criterion');
							name.classList.add('criterion-name');
							value.classList.add('criterion-value');

							name.innerHTML = criterion.name + ' : ';
							value.innerHTML = modules.fullview.formatData(criterion.value, modules.fullview.getItemUniverse(criterion.criterionModel));
							if(criterion.name == 'price') {
								//itemPrice.innerHTML = value.innerHTML + '€ per day';
								itemPrice.innerHTML = formatUnit(criterion.value, modules.fullview.getItemUniverse(criterion.criterionModel).unitID, 0) + ' per day';
							}

							item.appendChild(name);
							item.appendChild(value);

							content.appendChild(item);
						})(category.criteria[j]);
					}

					container.appendChild(title);
					container.appendChild(content);

					itemSpecs.appendChild(container);

				})(data.categories[i]);
			}
		},
		getItemUniverse: function(id) {
			for(var i = 0; i < model.model.length; ++i) {
				for(var j = 0; j < model.model[i].criteria.length; ++j) {
					if(model.model[i].criteria[j]['_id'] === id) {
						return model.model[i].criteria[j];
					}
				}
			}
		},
		formatData: function(data, universe) {
			function findWeightedString(data, id) {
				for(var i = 0; i < data.length; ++i) {
					if(data[i].value == id) {
						return data[i].name;
					}
				}
			}

			function genTree(node) {
				if(!node.children) {
					return node.value;
				}

				var tab = [];
				for(var i = 0; i < node.children.length; ++i) {
					tab.push(genTree(node.children[i]));
				}

				return '(' + node.value + ': ' + tab.join(', ') + ')';
			}

			switch(universe.itemType) {
				case 0: // Integer
					switch(universe.universeType) {
						case 3: // integer interval
							return formatLongUnit(data, '${value} ${unit}', universe.unitID);
						case 6: // weighted string list
							return findWeightedString(universe.universe.values, data);
						default:
							console.log('unhandled integer type : ' + universe.universeType);
					}
					break;
				case 1: // float
					switch(universe.universeType) {
						case 4: // float interval
							return formatLongUnit(data, '${value} ${unit}', universe.unitID);
						default:
							console.log('unhandled float type : ' + universe.universeType);
					}
					break;
				case 2: // Boolean
					return data ? 'Yes' : 'No';
				case 3: // Integer interval
					switch(universe.universeType) {
						case 3: // integer interval
							return formatLongUnit(data.min, '${value} ${unit}', universe.unitID) + ' to ' + formatLongUnit(data.max, '${value} ${unit}', universe.unitID);
						case 6: //weighted string list
							return findWeightedString(universe.universe.values, data.min) + ' to ' + findWeightedString(universe.universe.values, data.max);
						default:
							console.log('unhandled integer interval type : ' + universe.universeType);
					}
					break;
				case 4: // float interval
					switch(universe.universeType) {
						case 4: // float interval
							return formatLongUnit(data.min, '${value} ${unit}', universe.unitID) + ' to ' + formatLongUnit(data.max, '${value} ${unit}', universe.unitID);
						default:
							console.log('unhandled float interval type : ' + universe.universeType);
					}
					break;
				case 8: // integer list
					switch(universe.universeType) {
						case 5: // string list
							var output = [];
							for(var i of data) {
								output.push(i.name);
							}
							return output.join(', ');
					}
				case 9: // tree
					return genTree(data);
			}
		}
	};
})();