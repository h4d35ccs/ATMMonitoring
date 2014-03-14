function defaultInitTabs() {
	$('#tabs .content_tab').hide(); // cierro todas las capas
	$('#tabs .content_tab:first').show(); // muestro la primera
	$('.sub_nav li').removeClass('current');
	$('.sub_nav li:first').addClass('current'); // activo el primer tab
	$('.sub_nav li').click(function(event) {
		$('.sub_nav li').removeClass('current'); // borro estilo current de todos los li
		$(this).addClass('current');
		var tab_click = $('.sub_nav li').index(this);
		$('#tabs .content_tab').hide();
		$('#tabs').find(".content_tab").eq(tab_click).show();

		event.preventDefault();

		var eventOnShowData = $(this).data('onShow');
		if (eventOnShowData) {
			eval(eventOnShowData);
		}
	});
}

function countOccurences(mainStr, strToCount) {
	return mainStr.split(strToCount).length;
}
/**
 * Load an URL inside an element
 * @param elementID where the content is going to be shown
 * @param url the remote url to show
 */
function loadInnerSection(elementID, url) {
	$(elementID).empty();
	$(elementID).load(url, function() {
//		try {
			initPageJS();
//		} catch (err) {
//			alert(err);
			//in case that the initPage is not defined on the page
//		}
	});
}

/**
 * Load an URL inside an element
 * @param link where the click is made
 * @param elementID where the content is going to be shown
 * @param url the remote url to show
 */
function loadInnerSectionMenu(link, elementID, url, segmentId) {

	loadInnerSection(elementID, url);

}
/**
 * Gets the result of submitting a form and put it on a HTML element
 * @param formId id of the form
 * @param elementID parent element where the result of the query will be put
 * @param otherElementID result to be put inside the parent
 */
function loadInnerSectionFromForm(formId, elementID, otherElementID) {

	// Get some values from elements on the page:
	var $form = $(formId), term = $form.serializeArray(), url = $form
			.attr("action");
	// Send the data using post
	$.post(url, term, function(data) {
		// Put the results in a div
		var content = $(data).find(otherElementID);
		$(elementID).empty();
		$(elementID).append(content);
	});
}
/**
 * Calls a controller and do not expect an answer
 * @param url
 */
function loadPostRequestNoResponse(url){
	$.post(url);
}
function getDate(today){
	
	var dd = today.getDate();
	var mm = today.getMonth()+1; //January is 0!
	var yyyy = today.getFullYear();
	
	if(dd<10) {
	    dd='0'+dd;
	} 
	if(mm<10) {
	    mm='0'+mm;
	} 
	today = dd+'/'+mm+'/'+yyyy;
	return today;
}
/**
 * clock and dateto show on the app
 * base code taken from : http://www.w3schools.com/js/tryit.asp?filename=tryjs_timing_clock
 */
function clock() {
	var today = new Date();
	var h = today.getHours();
	var m = today.getMinutes();
	var s = today.getSeconds();
	// add a zero in front of numbers<10
	m = checkTime(m);
	s = checkTime(s);
	$("#welcomeDate").empty().append(getDate(today)+" "+h + ":" + m + ":" + s);
	t = setTimeout(function() {
		clock();
	}, 500);
}

function checkTime(i) {
	if (i < 10) {
		i = "0" + i;
	}
	return i;
}
/************************************************* terminals functions ***************************/
function onLoadModelCB() {
	var value = $('#ManufacturerCombo').val();
	var $cb = $('#ModelsCombo');
	if (value == '') {
		$cb.empty();
		$cb.append($('<option selected="selected"></option>'));
	} else {
		var values = valuesTree[value];
		$('#ModelsCombo > option').each(function() {
			if (!(($(this).val() in values) || ($(this).val() == ''))) {
				$(this).remove();
			}
		});
	}
	if (!$cb.val()) {
		var photoUrl = valuesTree[value]['photoUrl'];
		$('.photo a').attr("href", photoUrl);
		$('.photo img').attr("src", photoUrl);
	}
};

function ChangeManufacturer() {
	var $cb1 = $('#ModelsCombo');
	var $cb2 = $('#ManufacturerCombo');
	$cb1.empty();
	$cb1.append($('<option selected="selected"></option>'));
	if ($cb2.val() != '') {
		var values = valuesTree[$cb2.val()];
		var keys = $.map(values, function(v, i) {
			return i;
		});
		keys.sort(function(a, b) {
			var compA = values[a].label;
			var compB = values[b].label;
			return (compA < compB) ? -1 : (compA > compB) ? 1 : 0;
		});
		$.each(keys, function(index, key) {
			if (key != 'photoUrl') {
				$cb1.append($('<option/>').attr("value", key).text(
						values[key]['product_class']));
			}
		});
		$cb1.prop('disabled', false);
	} else {
		$cb1.prop('disabled', true);
	}
	;
	$('#field_model').text('');
	$('#field_product_class').text('');
	$('#field_manufacturer').text('');
	$('#field_nickname').text('');
	$('#field_width').text('');
	$('#field_height').text('');
	$('#field_depth').text('');
	$('#field_min_weight').text('');
	$('#field_max_weight').text('');
	var photoUrl;
	if ($cb2.val()) {
		photoUrl = valuesTree[$cb2.val()]['photoUrl'];
	} else {
		photoUrl = '<ncr:terminalModelPhotoUrl />'
	}
	$('.photo a').attr("href", photoUrl);
	$('.photo img').attr("src", photoUrl);
};

function ChangeModel() {
	var $cb1 = $('#ModelsCombo');
	var $cb2 = $('#ManufacturerCombo');
	if (($cb1.val() != '') && ($cb2.val() != '')) {
		var values = valuesTree[$cb2.val()][$cb1.val()];
		$('#field_model').text(values.model);
		$('#field_product_class').text(values.product_class);
		$('#field_manufacturer').text(values.manufacturer);
		$('#field_nickname').text(values.nickname);
		$('#field_width').text(values.width);
		$('#field_height').text(values.height);
		$('#field_depth').text(values.depth);
		$('#field_min_weight').text(values.min_weight);
		$('#field_max_weight').text(values.max_weight);
		$('.photo a').attr("href", 'terminals/models/image/' + $cb1.val());
		$('.photo img').attr("src",
				'terminals/models/image/' + $cb1.val() + '?width=200');
	}
	if (!$cb1.val()) {
		var photoUrl = valuesTree[$cb2.val()]['photoUrl'];
		$('.photo a').attr("href", photoUrl);
		$('.photo img').attr("src", photoUrl);
	}
};

function requestSnmpUpdate() {
	window.location.assignWithBase("terminals/request/${terminal.id}");
}

/************************************************* queries functions ***************************/
function displayOnLoad(name, maxValue) {
	for (i = maxValue; i > 1; i--) {
		if ($('#' + name + 'Combo' + i + '1').val() != '') {
			for (j = 1; j < i; j++) {
				showHiddenRow(name, j);
			}
			break;
		}
	}
};
function onLoadValueCB2(entity, maxNumber) {
	for (var number = 1; number <= maxNumber; number++) {
		var value = $('#' + entity + 'Combo' + number + '1').val();
		var $cb = $('#' + entity + 'Combo' + number + '2');
		if (value == '') {
			$cb.empty();
			$cb.append($('<option selected="selected"></option>'));
		} else {
			$cb.prop('disabled', false);
			var values = valuesTree[entity][value].values;
			$('#' + entity + 'Combo' + number + '2 > option').each(function() {
				if (!(($(this).val() in values) || ($(this).val() == ''))) {
					$(this).remove();
				}
			});
			if ($cb.val() != '') {
				$('#' + entity + 'Field' + number).prop('disabled', false);
			}
		}
	}
};
function onLoadValueCB3(entity, maxNumber) {
	for (var number = 1; number <= maxNumber; number++) {
		var value1 = $('#' + entity + 'Combo' + number + '1').val();
		var $cb2 = $('#' + entity + 'Combo' + number + '2');
		var $cb3 = $('#' + entity + 'Combo' + number + '3');
		if (value1 == '') {
			$cb2.empty();
			$cb2.append($('<option selected="selected"></option>'));
			$cb3.empty();
			$cb3.append($('<option selected="selected"></option>'));
		} else {
			$cb2.prop('disabled', false);
			var values = valuesTree[entity][value1].values;
			$('#' + entity + 'Combo' + number + '2 > option').each(function() {
				if (!(($(this).val() in values) || ($(this).val() == ''))) {
					$(this).remove();
				}
			});
			var value2 = $cb2.val();
			if (value2 == '') {
				$cb3.empty();
				$cb3.append($('<option selected="selected"></option>'));
			} else {
				$cb3.prop('disabled', false);
				values = values[value2].values;
				$('#' + entity + 'Combo' + number + '3 > option')
						.each(
								function() {
									if (!(($(this).val() in values) || ($(this)
											.val() == ''))) {
										$(this).remove();
									}
								});
			}
			if ($cb3.val() != '') {
				$('#' + entity + 'Field' + number).prop('disabled', false);
			}
		}
	}
};
function ChangeValue2CB1(entity, number) {
	var $cb = $('#' + entity + 'Combo' + number + '2');
	$cb.empty();
	$cb.append($('<option selected="selected"></option>'));
	if ($('#' + entity + 'Combo' + number + '1').val() != '') {
		var values = valuesTree[entity][$('#' + entity + 'Combo' + number + '1')
				.val()].values;
		var keys = $.map(values, function(v, i) {
			return i;
		});
		keys.sort(function(a, b) {
			var compA = values[a].label;
			var compB = values[b].label;
			return (compA < compB) ? -1 : (compA > compB) ? 1 : 0;
		});
		$.each(keys, function(index, key) {
			$cb.append($('<option/>').attr("value", key)
					.text(values[key].label));
		});
		$('#' + entity + 'CB' + number).prop('disabled', false);
		$cb.prop('disabled', false);
	} else {
		$('#' + entity + 'CB' + number).prop('disabled', true);
		$cb.prop('disabled', true);
	}
	;
	$('#' + entity + 'Field' + number).prop('disabled', true);
};
function ChangeValue2CB2(entity, number) {
	if ($('#' + entity + 'Combo' + number + '2').val() != '') {
		if (valuesTree[entity][$('#' + entity + 'Combo' + number + '1').val()].values[$(
				'#' + entity + 'Combo' + number + '2').val()].values) {
			$('#' + entity + 'Field' + number).prop('disabled', false);
		} else {
			$('#' + entity + 'Field' + number).prop('disabled', true);
		}
	} else {
		$('#' + entity + 'Field' + number).prop('disabled', true);
	}
	;
};
function ChangeValue3CB1(entity, number) {
	var $cb = $('#' + entity + 'Combo' + number + '2');
	$cb.empty();
	$cb.append($('<option selected="selected"></option>'));
	if ($('#' + entity + 'Combo' + number + '1').val() != '') {
		var values = valuesTree[entity][$('#' + entity + 'Combo' + number + '1')
				.val()].values;
		var keys = $.map(values, function(v, i) {
			return i;
		});
		keys.sort(function(a, b) {
			var compA = values[a].label;
			var compB = values[b].label;
			return (compA < compB) ? -1 : (compA > compB) ? 1 : 0;
		});
		$.each(keys, function(index, key) {
			$cb.append($('<option/>').attr("value", key)
					.text(values[key].label));
		});
		$('#' + entity + 'CB' + number).prop('disabled', false);
		$cb.prop('disabled', false);
	} else {
		$cb.prop('disabled', true);
	}
	;
	$('#' + entity + 'Combo' + number + '3').prop('disabled', true);
	$('#' + entity + 'CB' + number).prop('disabled', true);
	$('#' + entity + 'Field' + number).prop('disabled', true);
};
function ChangeValue3CB2(entity, number) {
	var $cb = $('#' + entity + 'Combo' + number + '3');
	$cb.empty();
	$cb.append($('<option selected="selected"></option>'));
	if ($('#' + entity + 'Combo' + number + '2').val() != '') {
		var values = valuesTree[entity][$('#' + entity + 'Combo' + number + '1')
				.val()].values[$('#' + entity + 'Combo' + number + '2').val()].values
		var keys = $.map(values, function(v, i) {
			return i;
		});
		keys.sort(function(a, b) {
			var compA = values[a].label;
			var compB = values[b].label;
			return (compA < compB) ? -1 : (compA > compB) ? 1 : 0;
		});
		$.each(keys, function(index, key) {
			$cb.append($('<option/>').attr("value", key)
					.text(values[key].label));
		});
		$('#' + entity + 'CB' + number).prop('disabled', false);
		$cb.prop('disabled', false);
	} else {
		$('#' + entity + 'CB' + number).prop('disabled', true);
		$cb.prop('disabled', true);
	}
	;
	$('#' + entity + 'Field' + number).prop('disabled', true);
};
function ChangeValue3CB3(entity, number) {
	if ($('#' + entity + 'Combo' + number + '3').val() != '') {
		if (valuesTree[entity][$('#' + entity + 'Combo' + number + '1').val()].values[$(
				'#' + entity + 'Combo' + number + '2').val()].values[$(
				'#' + entity + 'Combo' + number + '3').val()].values) {
			$('#' + entity + 'Field' + number).prop('disabled', false);
		} else {
			$('#' + entity + 'Field' + number).prop('disabled', true);
		}
	} else {
		$('#' + entity + 'Field' + number).prop('disabled', true);
	}
	;
};
function checkSaveExecute() {
	if ($('#queryName').val().length > 0) {
		if ($("#save_execute").button("option", "disabled")) {
			$("#save_execute").button("option", "disabled", false);
		}
	} else {
		if (!$("#save_execute").button("option", "disabled")) {
			$("#save_execute").button("option", "disabled", true);
		}
	}
}
function showHiddenRow(name, rowNumber) {
	$('#' + name + 'Row' + (rowNumber + 1)).removeClass('hidden');
	$('#' + name + 'ShowButton' + rowNumber).addClass('hidden');
}
function userQuerySelected() {
	document.userQueriesForm.submit();
}
