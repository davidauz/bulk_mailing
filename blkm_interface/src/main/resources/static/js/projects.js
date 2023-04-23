
function alertRetFalse(resp_ext){
	alert(resp_ext);
	return false;
}


function scheduleSend(nproj) {
		var params =
		{	'verb':'schedule_send'
		,	'projn':nproj.toString()
		};

		$.ajax(
		{	url: 'projects/ajx_dispatcher'
		,	method: "POST"
		,	contentType: 'application/json'
		,	data: JSON.stringify(params)
		,	success: function(data){
		        var paperpl=$('#paperpl'+nproj)
		        , modaldialog=$('#modaldialog')
		        ;
		        paperpl.removeClass('normal_paper_plane');
		        paperpl.addClass('nacost');
		        modaldialog.removeClass('nacost');
			}
		,	error: function(data) {
				console.log(data);
				return alertRetFalse(data.responseText);
			}
		}
		);
	}

function hide_modal_dialog(){
    var mdial=$('#modaldialog');
    mdial.addClass('nacost');
}