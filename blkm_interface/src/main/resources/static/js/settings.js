
		var	hbr
		,	hbs
		,	fsr
		,	fss;
		function alertRetFalse(resp_ext){
			alert(resp_ext);
			return false;
		}

		$(document).ready(
			function() {
				$('#imap_user_same').on('change', function() {
					if ($(this).is(':checked'))
						sameu_disabled(true);
					 else
						sameu_disabled(false);
				});

				var sameu=$('#imap_user_same').val();
				if("on"==sameu)
					sameu_disabled(true);

				setInterval(function() {
					var col_read
					,	col_send
					,	hbrv
					,	hbsv
					,	params =
					{	'verb':'heartbeats'
					};
					$.ajax(
					{		url: 'settings/ajx_dispatcher' // url: '../ajx_dispatcher'
					,	method: "POST"
					,	contentType: 'application/json'
					,	data: JSON.stringify(params)
					,	success: function(data){
							col_read=data.READ;
							col_send=data.SEND;

							$('#read_mail_daemon_running').css("color", col_read);
							$('#send_mail_daemon_running').css("color", col_send);

							hbrv=data.HBR.value;
							hbsv=data.HBS.value;

							if(hbrv!=hbr)
								if(fsr=="12px")
									fsr="16px";
								else
									fsr="12px";
							hbr=hbrv;
							if(hbsv!=hbs)
								if(fss=="12px")
									fss="16px";
								else
									fss="12px";
							hbs=hbsv;
							$('#read_mail_daemon_running').css("font-size", fsr);
							$('#send_mail_daemon_running').css("font-size", fss);
						}
					,	error: function(data) {
							console.log(data);
							return alertRetFalse(data.responseText);
						}
					}
					)
				}, 1000);

			}
		);

		function sameu_disabled(value){
			$('#imap_uname').prop('disabled', value);
			$('#imap_password').prop('disabled', value);
			$('#imap_password2').prop('disabled', value);
		}

