(function() {
	var view = document.querySelector('#search-page');
	var inputElem = view.querySelector('#search-input');

	var notUsedButton = view.querySelector('#search-not-used');
	var usedButton = view.querySelector('#search-used');

	var usedTitle = usedButton.querySelector('#search-used-title');
	var question = view.querySelector('#search-question');

	var breadcrumb = document.querySelector('#search-breadcrumb');
	var breadcrumbCat = document.querySelector('#search-breadcrumb-cat');
	var breadcrumbCrit = document.querySelector('#search-breadcrumb-crit');

	var refresh = document.querySelector('#search-refresh');
	var back = document.querySelector('#search-back');
	var next = document.querySelector('#search-next');
	var start = document.querySelector('#search-start');

	var reallyImportant = document.querySelector('#search-really-important');

	var usedCriteria = false;

	var types = {
		2: 'boolean',
		3: 'integer-interval',
		4: 'float-interval',
		5: 'string-list',
		6: 'weighted-string-list',
		7: 'tree'
	};

	// model datas
	var categories = [];
	var currentCat = 0;
	var currentCrit = 0;

	window.registerView('search');

	if(!window.modules) {
		window.modules = {};
	}

	window.modules['search'] = {
		init: function() {
			console.log('initialisation du module search');
			refresh.addEventListener('click', function() {
				loadView('search');
			})
		},
		load: function() {
			console.log('loading search');
			function init() {
				console.log('search loaded');
				hideAllViews();
				modules.menu.show();
				modules.menu.showBackArrow(false);
				modules.help.pushContext('search');
				view.classList.remove('hide');
				app.setTitle('Search');

				modules.splash.hide();

				notUsedButton.addEventListener('click', function() {
					modules.search.setUnused();
				});

				usedButton.addEventListener('click', function() {
					modules.search.setUsed();
				});

				reallyImportant.addEventListener('change', function() {
					categories[currentCat].criteria[currentCrit].isCritical = !!(reallyImportant.querySelector('input').checked);
				});

				start.removeEventListener('click', modules.search.start);

				start.addEventListener('click', modules.search.start);

				categories = [];
				currentCat = 0;
				currentCrit = 0;

				modules.search.initBreadcrumb();
			}

			if(!modules.splash.isShown()) {
				console.log('not shown');
				modules.splash.show(init);
			}
			else {
				console.log('shown');
				init();
			}
		},
		setUsed: function() {
			if(usedCriteria) {
				return;
			}

			var auto;
			$(usedButton).css('height', 'auto');
			$(usedTitle).css('height', '0');
			auto = $(usedButton).height();
			$(usedButton).css('height', '4rem');
			$(usedTitle).css('height', '4rem');

			notUsedButton.classList.remove('selected');
			usedButton.classList.add('selected');

			$(usedTitle).animate({
				height: 0
			}, 200);
			$(usedButton).animate({
				height: auto + 'px'
			}, 200, function() {
				$(usedButton).css('height', 'auto');
			});

			usedCriteria = true;
			categories[currentCat].criteria[currentCrit].used = true;
		},
		setUnused: function() {
			if(!usedCriteria) {
				return;
			}

			notUsedButton.classList.add('selected');
			usedButton.classList.remove('selected');
			usedCriteria = false;
			categories[currentCat].criteria[currentCrit].used = false;

			// transitionning the title to the top
			$(usedTitle).animate({
				height: '4rem'
			}, 200);
			$(usedButton).animate({
				height: '4rem'
			}, 200);
		},
		setCriteria: function(criteria) {
			breadcrumbCrit.innerHTML = '';
			for(var i = 0; i < criteria.length; ++i) {
				(function(i) {
					var item = document.createElement('li');
					item.innerHTML = criteria[i].name;

					breadcrumbCrit.appendChild(item);

					criteria[i].select = function() {
						currentCrit = i;
						var toRemove = breadcrumbCrit.querySelector('.selected');
						if(toRemove) {
							toRemove.classList.remove('selected');
						}
						item.classList.add('selected');
						question.innerHTML = criteria[i].question;

						if(!criteria[i].everOpened) {
							criteria[i].used = true;
							criteria[i].everOpened = true;
						}

						// select the right option

						inputElem.innerHTML = '';

						switch(types[criteria[i].type]) {
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

								break;
							case 'integer-interval':
								var range;
								if(criteria[i].universe.max - criteria[i].universe.min > 1000) {
									range = new Range(inputElem, criteria[i].universe.min, criteria[i].universe.max, 0.0001, 0);
									range.init(true);
									range.setUnitID(criteria[i].unitID);

									if(criteria[i].userValue) { // put back the old values in the field
										range.setMin(criteria[i].userValue.min);
										range.setMax(criteria[i].userValue.max);
									}
								}
								else {
									range = new Range(inputElem, criteria[i].universe.min, criteria[i].universe.max);								
									range.init();
									range.setUnitID(criteria[i].unitID);

									if(criteria[i].userValue) { // put back the old values in the field
										range.setMin(criteria[i].userValue.min);
										range.setMax(criteria[i].userValue.max);
									}
								}

								range.onChange(function(data) {
									//save the new values in the criterion
									criteria[i].userValue = data;
								});

								break;
							case 'float-interval':
								if(criteria[i].universe.max - criteria[i].universe.min > 1000) {
									range = new Range(inputElem, criteria[i].universe.min, criteria[i].universe.max, 0.0001, 2);
									range.init(true);
									range.setUnitID(criteria[i].unitID);

									if(criteria[i].userValue) { // put back the old values in the field
										range.setMin(criteria[i].userValue.min);
										range.setMax(criteria[i].userValue.max);
									}
								}
								else {
									range = new Range(inputElem, criteria[i].universe.min, criteria[i].universe.max, 0.01, 2);								
									range.init();
									range.setUnitID(criteria[i].unitID);

									if(criteria[i].userValue) { // put back the old values in the field
										range.setMin(criteria[i].userValue.min);
										range.setMax(criteria[i].userValue.max);
									}
								}

								range.onChange(function(data) {
									//save the new values in the criterion
									criteria[i].userValue = data;
								});
								break;
							case 'string-list':
								var list = [];
								for(var j = 0; j < criteria[i].universe.nodes.length; ++j) {
									list[criteria[i].universe.nodes[j].id] = criteria[i].universe.nodes[j].name;
								}
								var strli = new StringList(inputElem, list);
								strli.init();

								if(criteria[i].userValue) {
									strli.setSelected(criteria[i].userValue);
								}

								strli.onChange(function(data) {
									//save the new values in the criterion
									criteria[i].userValue = data;
								});

								break;
							case 'weighted-string-list':
								var list = [];
								for(var j = 0; j < criteria[i].universe.values.length; ++j) {
									list[criteria[i].universe.values[j].value] = criteria[i].universe.values[j].name;
								}
								var range = new Range(inputElem, criteria[i].universe.min, criteria[i].universe.max, list);
								range.init();

								if(criteria[i].userValue) { // put back the old values in the field
									range.setMin(criteria[i].userValue.min);
									range.setMax(criteria[i].userValue.max);
								}

								range.onChange(function(data) {
									//save the new values in the criterion
									criteria[i].userValue = data;
								});
								break;
							case 'tree':
								var tree = new Tree(inputElem, criteria[i].universe.tree);
								tree.init();

								if(criteria[i].userValue) {
									tree.selectTree(criteria[i].userValue);
								}

								tree.onChange(function(data) {
									//save the new values in the criterion
									criteria[i].userValue = data;
								});
						}

						if(criteria[i].used) {
							modules.search.setUsed();
						}
						else {
							modules.search.setUnused();
						}

						if(criteria[i].isCritical) {
							reallyImportant.querySelector('input').checked = true;
						}
						else {
							reallyImportant.querySelector('input').checked = false;
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
						categories.push({
							name: model.model[i].criteria[j].name,
							oldName: model.model[i].name,
							id: model.model[i].criteria[j]['_id'],
							oldId: model.model[i]['_id'],
							criteria: [{
								everOpened: false, // if the user have opened this criterion before
								used: false, // wether the criteria is used
								isCritical: false, // wether the criteria is critical or not
								name: model.model[i].criteria[j].name,
								id: model.model[i].criteria[j]['_id'],
								question: model.model[i].criteria[j].question,
								type: model.model[i].criteria[j].userType,
								universe: model.model[i].criteria[j].universe,
								unitID: model.model[i].criteria[j].unitID
							}]
						});
					}
				}
				else {
					var criteria = [];
					for(var j = 0; j < model.model[i].criteria.length; ++j) {
						if(!model.model[i].criteria[j].isComparable) {
							continue;
						}
						criteria.push({
							everOpened: false,
							used: false,
							isCritical: false,
							name: model.model[i].criteria[j].name,
							id: model.model[i].criteria[j]['_id'],
							question: model.model[i].criteria[j].question,
							type: model.model[i].criteria[j].userType,
							universe: model.model[i].criteria[j].universe,
								unitID: model.model[i].criteria[j].unitID
						});
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
						topOffset = -i*4;
						$(breadcrumbCat).stop();
						$(breadcrumbCat).animate({
							'margin-top': topOffset + 'em'
						}, 200);
						currentCat = i;

						// set the criterias
						modules.search.setCriteria(categories[i].criteria);
						categories[i].criteria[0].select();
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
			console.log('building the output');
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
						if(!categories[i].criteria[j].used) {
							continue;
						}

						crits.push({
							criterionId: categories[i].criteria[j].id,
							disableFlex: categories[i].criteria[j].isCritical,
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

					if(!categories[i].criteria[0].used) {
						continue;
					}
					// each criteria of this category is on the _self_ category
					// we know there is only one criterion in this category
					tmpSelf.push({
						criterionId: categories[i].criteria[0].id,
						disableFlex: categories[i].criteria[0].isCritical,
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

			API.search(JSON.stringify(obj)).then(function(data) {
				loadView('results', data);
			}).catch(function(data) {
				console.log('Javascript : error while starting the search : ' + data.message);
			});
		}
	};
})();