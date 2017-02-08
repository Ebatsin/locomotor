(function() {
	var view = document.querySelector('#add-page');
	var title = document.querySelector('#add-page h1');
	var inputElem = view.querySelector('#add-input');
	var universeElem = view.querySelector('#add-universe');

	var breadcrumb = document.querySelector('#add-breadcrumb');
	var breadcrumbCat = document.querySelector('#add-breadcrumb-cat');
	var breadcrumbCrit = document.querySelector('#add-breadcrumb-crit');

	var back = document.querySelector('#add-back');
	var next = document.querySelector('#add-next');
	var start = document.querySelector('#add-start');

	var itemName = view.querySelector('#add-vehicle-name');
	var itemUrl = view.querySelector('#add-vehicle-url');
	var itemDesc = view.querySelector('#add-vehicle-desc');

	var types = {
		0: 'integer',
		1: 'float',
		2: 'boolean',
		3: 'integer-interval',
		4: 'float-interval',
		8: 'integer-list',
		9: 'integer-tree'
	};

	var categories = [];
	var currentCat = 0;
	var currentCrit = 0;

	var strli;

	var validCallback = function(){return true;};

	var modification = false;
	var data;

	window.registerView('add');

	if(!window.modules) {
		window.modules = {};
	}

	window.modules['add'] = {
		init: function() {
			console.log('initialisation du module add');
		},
		/**
		* Load the login view.
		* @param params : le full vehicule
		* mode: 'login' | 'register'
		*/
		load: function(params) {
			view.classList.remove('hide');
			view.style['z-index'] = getNextZIndex();
			modules.help.pushContext('add');

			if(params) {
				title.innerHTML = 'Modify a vehicle';
				modification = true;
				data = params;
			}
			else {
				title.innerHTML = 'Create a vehicle';
				modification = false;
			}

			modules.menu.pushBackArrow(function() {
				modules.add.unload();
			});

			start.removeEventListener('click', modules.add.start);

			start.addEventListener('click', modules.add.start);

			categories = [];
			currentCat = 0;
			currentCrit = 0;

			// ajout des univers
			document.querySelector('#add-vehicle-universe').innerHTML = '';
			var uni = {};
			for(var i = 0; i < universes.length; ++i) {
				uni[i] = universes[i].name;
			}
			strli = new StringList(document.querySelector('#add-vehicle-universe'), uni);
			strli.init();

			var strSelect;

			if(modification) {
				for(var i = 0; i < universes.length; ++i) {
					if(universes[i]['_id'] == data.universeID) {
						strli.setSelected([i]);
						strSelect = i;
						break;
					}
				}
			}

			strli.onChange(function() {
				if(strSelect !== undefined) {
					if(strli.getIDs()[0] === strSelect && strli.getIDs().length > 1) {
						strli.setSelected([strli.getIDs()[1]]);
					}
					else {
						strli.setSelected([strli.getIDs()[0]]);
					}
					strSelect = strli.getIDs()[0];
				}
				else {
					strSelect = strli.getIDs()[0];
				}
			});

			itemName.value = modification ? data.name : '';
			itemDesc.value = modification ? data.description : '';
			itemUrl.value = modification ? data.image : '';

			modules.add.initBreadcrumb();
		},
		unload: function() {
			modules.menu.popBackArrow();
			view.classList.add('hide');
			modules.help.popContext();
		},
		setCriteria: function(criteria) {
			breadcrumbCrit.innerHTML = '';
			for(var i = 0; i < criteria.length; ++i) {
				(function(i) {
					var item = document.createElement('li');
					item.innerHTML = criteria[i].name;

					breadcrumbCrit.appendChild(item);

					criteria[i].select = function(nocheck) {
						if(!nocheck) {
							if(!validCallback()) {
								return;
							}
						}

						currentCrit = i;
						var toRemove = breadcrumbCrit.querySelector('.selected');
						if(toRemove) {
							toRemove.classList.remove('selected');
						}

						item.classList.add('selected');

						// select the right option

						inputElem.innerHTML = '';
						universeElem.innerHTML = '';

						switch(types[criteria[i].type]) {
							case 'integer':
								if(criteria[i].universeType === 6) { // universe type
									var items = [];
									for(var j = 0; j < criteria[i].universe.values.length; ++j) {
										items.push(criteria[i].universe.values[j].value + ': ' + criteria[i].universe.values[j].name);
									}
									universeElem.innerHTML = items.join(', ');
								}
								var integer = new Integer(inputElem, criteria[i].universe.min, criteria[i].universe.max);
								integer.init();

								integer.onSubmit(function() {
									next.click();
								});

								if(criteria[i].userValue) {
									integer.setValue(criteria[i].userValue);
								}

								validCallback = function() {
									if(!integer.isValid()) {
										return false;
									}

									criteria[i].userValue = integer.getValue();
									criteria[i].filled = true;

									return true;
								};

								break;
							case 'float':
								var float = new Float(inputElem, criteria[i].universe.min, criteria[i].universe.max);
								float.init();

								float.onSubmit(function() {
									next.click();
								});

								if(criteria[i].userValue) {
									float.setValue(criteria[i].userValue);
								}

								validCallback = function() {
									if(!float.isValid()) {
										return false;
									}

									criteria[i].userValue = float.getValue();
									criteria[i].filled = true;

									return true;
								};

								break;
							case 'boolean':
								var boolean = new Boolean(inputElem);
								boolean.init();

								if(criteria[i].userValue) {
									boolean.setChecked(criteria[i].userValue);
								}

								boolean.onChange(function(data) {
									//save the new values in the criterion
									criteria[i].userValue = data;
								});

								validCallback = function() {
									criteria[i].filled = true;
									return true;
								};

								break;
							case 'integer-interval':
								if(criteria[i].universeType === 6) { // universe type
									var items = [];
									for(var j = 0; j < criteria[i].universe.values.length; ++j) {
										items.push(criteria[i].universe.values[j].value + ': ' + criteria[i].universe.values[j].name);
									}
									universeElem.innerHTML = items.join(', ');
								}
								var integer = new PoorIntRange(inputElem, criteria[i].universe.min, criteria[i].universe.max);
								integer.init();

								integer.onSubmit(function() {
									next.click();
								});

								if(criteria[i].userValue) {
									integer.setMinValue(criteria[i].userValue.min);
									integer.setMaxValue(criteria[i].userValue.max);
								}

								validCallback = function() {
									if(!integer.isValid()) {
										return false;
									}

									criteria[i].userValue = {
										min: integer.getMinValue(),
										max: integer.getMaxValue()
									};
									criteria[i].filled = true;

									return true;
								};

								break;
							case 'float-interval':
								var float = new PoorIntRange(inputElem, criteria[i].universe.min, criteria[i].universe.max);
								float.init();

								float.onSubmit(function() {
									next.click();
								});

								if(criteria[i].userValue) {
									float.setMinValue(criteria[i].userValue.min);
									float.setMaxValue(criteria[i].userValue.max);
								}

								validCallback = function() {
									if(!float.isValid()) {
										return false;
									}

									criteria[i].userValue = {
										min: float.getMinValue(),
										max: float.getMaxValue()
									};
									criteria[i].filled = true;
									
									return true;
								};

								break;
							case 'integer-list':
								var list = [];
								for(var j = 0; j < criteria[i].universe.nodes.length; ++j) {
									list[criteria[i].universe.nodes[j].id] = criteria[i].universe.nodes[j].name;
								}
								var strli = new StringList(inputElem, list);
								strli.init();

								if(criteria[i].userValue) {
									if(criteria[i].userValue.length > 0 && criteria[i].userValue[0].value) {
										// formating it right
										var arr = [];
										for(var k = 0; k < criteria[i].userValue.length; ++k) {
											arr.push(criteria[i].userValue[k].value);
										}
										strli.setSelected(arr);
									}
									else {
										strli.setSelected(criteria[i].userValue);
									}
								}

								strli.onChange(function(data) {
									//save the new values in the criterion
									criteria[i].userValue = data;
									criteria[i].filled = true;
								});

								validCallback = function() {
									criteria[i].filled = true;
									return true;
								};

								break;
							case 'integer-tree':
								var tree = new Tree(inputElem, criteria[i].universe.tree);
								tree.init();

								if(criteria[i].userValue) {
									tree.selectTree(criteria[i].userValue);
								}

								tree.onChange(function(data) {
									//save the new values in the criterion
									criteria[i].userValue = data;
								});

								validCallback = function() {
									criteria[i].filled = true;
									return true;
								};
						}
					};

					item.addEventListener('click', function() {
						criteria[i].select();
					});
				})(i);
			}
		},
		initBreadcrumb: function() {
			var catOpen = false;
			var topOffset = 0;

			breadcrumbCat.innerHTML = '';
			// refreshing the breadcrumbcat item, back, next
			var clone = breadcrumbCat.cloneNode();
			breadcrumbCat.parentNode.replaceChild(clone, breadcrumbCat);
			breadcrumbCat = clone;

			clone = back.cloneNode();
			while(back.firstChild) {
				clone.appendChild(back.lastChild);
			}
			back.parentNode.replaceChild(clone, back);
			back = clone;

			clone = next.cloneNode();
			while(next.firstChild) {
				clone.appendChild(next.lastChild);
			}
			next.parentNode.replaceChild(clone, next);
			next = clone;

			for(var i = 0; i < model.model.length; ++i) {
				if(model.model[i].name === '_self_') {
					for(var j = 0; j < model.model[i].criteria.length; ++j) {
						var curitem = {
							name: model.model[i].criteria[j].name,
							oldName: model.model[i].name,
							id: model.model[i].criteria[j]['_id'],
							oldId: model.model[i]['_id'],
							criteria: [{
								filled: modification, // if the user have changed it
								name: model.model[i].criteria[j].name,
								id: model.model[i].criteria[j]['_id'],
								type: model.model[i].criteria[j].itemType,
								universeType: model.model[i].criteria[j].universeType,
								universe: model.model[i].criteria[j].universe,
								unitID: model.model[i].criteria[j].unitID
							}]
						};

						if(modification) {
							curitem.criteria[0].userValue = findCriterion(model.model[i]['_id'], model.model[i].criteria[j]['_id']);
						}

						categories.push(curitem);
					}
				}
				else {
					var criteria = [];
					for(var j = 0; j < model.model[i].criteria.length; ++j) {
						var curitem = {
							filled: modification,
							name: model.model[i].criteria[j].name,
							id: model.model[i].criteria[j]['_id'],
							type: model.model[i].criteria[j].itemType,
							universeType: model.model[i].criteria[j].universeType,
							universe: model.model[i].criteria[j].universe,
							unitID: model.model[i].criteria[j].unitID
						};

						if(modification) {
							curitem.userValue = findCriterion(model.model[i]['_id'], model.model[i].criteria[j]['_id']);
						}

						criteria.push(curitem);
					}

					categories.push({
						name: model.model[i].name,
						oldName : model.model[i].name,
						oldId: model.model[i]['_id'],
						criteria: criteria
					});
				}
			}

			// creating the category dropdown list
			for(var i = 0; i < categories.length; ++i) {
				(function(i) {
					var item = document.createElement('li');
					item.innerHTML = categories[i].name;
					breadcrumbCat.appendChild(item);

					categories[i].select = function() {
						if(!validCallback()) {
							return;
						}
						topOffset = -i*4;
						$(breadcrumbCat).stop();
						$(breadcrumbCat).animate({
							'margin-top': topOffset + 'em'
						}, 200);
						currentCat = i;

						// set the criterias
						modules.add.setCriteria(categories[i].criteria);
						categories[i].criteria[0].select(true);
					};

					item.addEventListener('click', function() {
						if(catOpen) {
							categories[i].select();
						}
					});
				})(i);
			}

			breadcrumbCat.querySelector('li').classList.add('selected');

			breadcrumbCat.addEventListener('click', function() {
				breadcrumb.classList.toggle('show-cat');
				catOpen = breadcrumb.classList.contains('show-cat');
			});

			breadcrumbCat.addEventListener('wheel', function(e) {
				if(catOpen) {
					if(e.deltaY > 0) {
						topOffset -= 4;
					}
					else {
						topOffset += 4;
					}

					if(topOffset > 0) {
						topOffset = 0;
					}
					else if(topOffset < -4 * (categories.length - 1)) {
						topOffset = -4 * (categories.length - 1);
					}

					$(breadcrumbCat).stop();
					$(breadcrumbCat).animate({
						'margin-top': topOffset + 'em'
					}, 200);
				}
			});

			categories[0].select();

			back.addEventListener('click', function() {
				if(currentCrit > 0) {
					categories[currentCat].criteria[currentCrit - 1].select();
				}
				else if(currentCat > 0) {
					categories[currentCat - 1].select();
					categories[currentCat].criteria[categories[currentCat].criteria.length - 1].select();
				}
			});

			next.addEventListener('click', function() {
				if(currentCrit < categories[currentCat].criteria.length - 1) {
					categories[currentCat].criteria[currentCrit + 1].select();
				}
				else if(currentCat < categories.length - 1) {
					categories[currentCat + 1].select();
					categories[currentCat].criteria[0].select();
				}
			});
		},
		start: function() { // start a research
			validCallback();
			var obj = [];
			var tmpSelf = [];
			var tmpId;

			for(var i = 0; i < categories.length; ++i) {
				if(categories[i].oldName !== '_self_') {
					if(tmpSelf.length !== 0) {
						obj.push({
							categoryId: tmpId,
							criteria: tmpSelf
						});
						tmpSelf = [];
					}

					// normal category
					var crits = [];
					for(var j = 0; j < categories[i].criteria.length; ++j) {
						if(!categories[i].criteria[j].filled) {
							console.log('one criterion not filled : ' + categories[i].criteria[j].name);
							console.log('one criterion not filled : ' + categories[i].criteria[j].userValue);
							return;
						}

						if(modification && ['integer', 'float'].indexOf(types[categories[i].criteria[j].type]) != -1) {
							categories[i].criteria[j].userValue = categories[i].criteria[j].userValue[0];
						}
						else if(modification && types[categories[i].criteria[j].type] == 'integer-list') {
							if(categories[i].criteria[j].userValue.length > 0 && categories[i].criteria[j].userValue[0].value) {
								// oncverting to the right format
								var tmp = [];
								for(var k = 0; k < categories[i].criteria[j].userValue.length; ++k) {
									tmp.push(categories[i].criteria[j].userValue[k].value);
								}
								categories[i].criteria[j].userValue = tmp;
							}
						}

						crits.push({
							criterionId: categories[i].criteria[j].id,
							value: categories[i].criteria[j].userValue
						});
					}

					if(crits.length == 0) {
						continue;
					}

					obj.push({
						categoryId: categories[i].oldId,
						criteria: crits
					});
				}
				else {
					tmpId = categories[i].oldId;

					if(!categories[i].criteria[0].filled) {
						console.log('one criterion not filled : ' + categories[i].criteria[0].name);
						console.log('one criterion not filled : ' + categories[i].criteria[0].userValue);
						return; 
					}

					if(modification && ['integer', 'float'].indexOf(types[categories[i].criteria[0].type]) != -1) {
						categories[i].criteria[0].userValue = categories[i].criteria[0].userValue[0];
					}
					else if(modification && types[categories[i].criteria[0].type] == 'integer-list') {
						if(categories[i].criteria[0].userValue.length > 0 && categories[i].criteria[0].userValue[0].value) {
							// oncverting to the right format
							var tmp = [];
							for(var k = 0; k < categories[i].criteria[0].userValue.length; ++k) {
								tmp.push(categories[i].criteria[0].userValue[k].value);
							}
							categories[i].criteria[0].userValue = tmp;
						}
					}

					// each criteria of this category is on the _self_ category
					// we know there is only one criterion in this category
					tmpSelf.push({
						criterionId: categories[i].criteria[0].id,
						value: categories[i].criteria[0].userValue
					});
				}
			}

			if(tmpSelf.length !== 0) {
				obj.push({
					categoryId: tmpId,
					criteria: tmpSelf
				});
				tmpSelf = [];
			}

			console.log('checking name');

			if(itemName.value.trim().length == 0) {
				return;
			}
			console.log('checking desc');
			if(itemDesc.value.replace(/\n/g, '<br>').trim().length == 0) {
				return;
			}
			console.log('checking url');

			if(itemUrl.value.trim().length == 0) {
				console.log('not good ' + itemUrl.value.trim());
				return;
			}
			console.log('checking ids');

			if(strli.getIDs().length == 0) {
				return;
			}
			console.log('gen output');

			var out = {
				name: itemName.value.trim(),
				image: itemUrl.value.trim(),
				universe: universes[strli.getIDs()[0]]['_id'],
				description: itemDesc.value.replace(/\n/g, '<br>').trim(),
				categories: obj
			};

			if(modification) {
				out.id = data.id;
				API.updateItem(out).then(function() {
					modules.menu.popBackArrow();
					API.getAllItems().then(function(data) {
						loadView('manage', data);
					});
				}).catch(function(data) {
					console.log('Javascript : error while adding the item : ' + data.message);
				});
			}
			else {
				API.addItem(out).then(function() {
					modules.menu.popBackArrow();
					API.getAllItems().then(function(data) {
						loadView('manage', data);
					});
				}).catch(function(data) {
					console.log('Javascript : error while adding the item : ' + data.message);
				});
			}

		}
	};

	// returns item values
	function findCriterion(catId, critId) {
		for(var i = 0; i < data.categories.length; ++i) {
			if(data.categories[i].categoryModel == catId) {
				for(var j = 0; j < data.categories[i].criteria.length; ++j) {
					if(data.categories[i].criteria[j].criterionModel == critId) {
						return data.categories[i].criteria[j].value;
					}
				}
			}
		}
	}
})();