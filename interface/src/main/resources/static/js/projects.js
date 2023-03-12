

function alertRetFalse(resp_ext){
	alert(resp_ext);
	return false;
	return false;
}


function removePerson() {
	$('#selected_pers option:selected').each(function(idx, opt){
		$('#available_persons').append(opt);
		$('#selected_pers').find('option[value="'+opt.value+'"]').remove();
	});
}
function addPerson() {
	$('#available_persons option:selected').each(function(idx, opt){
		$('#selected_pers').append(opt);
		$('#available_persons').find('option[value="'+opt.value+'"]').remove();
	});
}

function removeCompany() {
	$('#selected_companies option:selected').each(function(idx, opt){
		$('#companies').append(opt);
		$('#selected_companies').find('option[value="'+opt.value+'"]').remove();
	});
}

function fill_values(){
	$('#member_of option').prop('selected', true);
	console.log($('#member_of'));
}


function addGroup() {
		var groups_array=$('#sel_groups').val()
		,	sel_pers=$('#selected_pers')
		,	ava_pers=$('#available_persons')
		,	selected_groups=$('#selected_groups')
		,	sel_groups=$('#sel_groups')
		,	params =
		{	'verb':'add_groups'
		,	'groups':groups_array
		};

		$.ajax(
		{	url: '/project/ajx_dispatcher'
		,	method: "POST"
		,	contentType: 'application/json'
		,	data: JSON.stringify(params)
		,	success: function(data){
			$.each( data.PEOPLE, function(key, val) {
				sel_pers.append(new Option(val[1]+' '+val[2], val[0]));
				ava_pers.find('option[value="'+val[0]+'"]').remove();
			});
			$.each( data.GROUPS, function(key, val) {
				selected_groups.append(new Option(val[1], val[0]));
				sel_groups.find('option[value="'+val[0]+'"]').remove();
			})
			}
		,	error: function(data) {
				console.log(data);
				return alertRetFalse(data.responseText);
			}
		}
		);
	}

function removeGroup(){
		var groups_array=$('#selected_groups').val()
		,	sel_pers=$('#selected_pers')
		,	ava_pers=$('#available_persons')
		,	selected_groups=$('#selected_groups')
		,	sel_groups=$('#sel_groups')
		,	params =
		{	'verb':'rm_groups'
		,	'groups':groups_array
		};

		$.ajax(
		{	url: '/project/ajx_dispatcher'
		,	method: "POST"
		,	contentType: 'application/json'
		,	data: JSON.stringify(params)
		,	success: function(data){
			$.each( data.PEOPLE, function(key, val) {
				ava_pers.append(new Option(val[1]+' '+val[2], val[0]));
				sel_pers.find('option[value="'+val[0]+'"]').remove();
			});
			$.each( data.GROUPS, function(key, val) {
				sel_groups.append(new Option(val[1], val[0]));
				selected_groups.find('option[value="'+val[0]+'"]').remove();
			})
			}
		,	error: function(data) {
				console.log(data);
				return alertRetFalse(data.responseText);
			}
		}
		);
	}

function addCompany() {
    var companies_array=$('#sel_companies').val()
    ,	sel_pers=$('#selected_pers')
    ,	ava_pers=$('#available_persons')
    ,	selected_companies=$('#selected_companies')
    ,	sel_companies=$('#sel_companies')
    ,	params =
    {	'verb':'add_companies'
    ,	'ajx_data':companies_array
    };

    $.ajax(
    {	url: '/project/ajx_dispatcher'
    ,	method: "POST"
    ,	contentType: 'application/json'
    ,	data: JSON.stringify(params)
    ,	success: function(data){
        $.each( data.PEOPLE, function(key, val) {
            sel_pers.append(new Option(val[1]+' '+val[2], val[0]));
            ava_pers.find('option[value="'+val[0]+'"]').remove();
        });
        $.each( data.COMPANIES, function(key, val) {
            selected_companies.append(new Option(val[1], val[0]));
            sel_companies.find('option[value="'+val[0]+'"]').remove();
        })
        }
    ,	error: function(data) {
            console.log(data);
            return alertRetFalse(data.responseText);
        }
    }
    );
}

