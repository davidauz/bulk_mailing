package com.davidauz.blkm_common.entity;

public class EmailStatusConstants {
	public static final String SENT_SUCCESS = "Sent"
			,	SENT_ERROR = "Error sending"
			,	ENQUEUED = "Enqueued"
			,	SYSTEM_ERROR = "System error"
			,	ERR_RECIPIENT_NOT_FOUND = "ERR: recipient not found"
			,	ERR_RECIPIENT_MAILBOX_FULL = "ERR: mailbox full"
			,	ERR_MAILBOX_NOT_FOUND = "ERR: mailbox not found"
			,	ERR_SERVER_UNAVAILABLE = "ERR: server unavailable"
			,	ERR_UNDEFINED="ERR: undefined"
			,	ERR_DOMAIN_NOT_FOUND = "ERR: Host or domain name not found"
			,	ERR_SYSTEM_PROBLEM = "ERR: System problem"
			,	ERR_TEMPORARY_SYSTEM_PROBLEM = "ERR: Temporary system problem"
			,	ERR_SMTP_PROTOCOL_VIOLATION = "ERR: SMTP protocol violation"
			,	ERR_CANNOT_DECODE_RESPONSE ="ERR:cannot decode response"
			,	ERR_UNRECOGNIZED_COMMAND ="ERR:unrecognized command"
			,	ERR_HELO_FIRST ="ERR:HELO first"
			,	ERR_WRONG_AUTH_TYPE ="ERR:wrong auth type"
			,	ERR_AUTH_REQUIRED ="ERR:auth required"
			,	ERR_AUTH_PROBLEM ="ERR:auth problem"
			,	ERR_SYNTAX_ERROR ="ERR:syntax error"
			;


// https://serversmtp.com/smtp-error/
// https://www.rfc-editor.org/rfc/rfc5321
	public static String[][] error_codes_table =
		{{ "451", EmailStatusConstants.ERR_SYSTEM_PROBLEM }
		,{ "421", EmailStatusConstants.ERR_TEMPORARY_SYSTEM_PROBLEM }
		,{ "452", EmailStatusConstants.ERR_RECIPIENT_MAILBOX_FULL }
		,{ "454", EmailStatusConstants.ERR_SMTP_PROTOCOL_VIOLATION }
		,{ "501", EmailStatusConstants.ERR_CANNOT_DECODE_RESPONSE }
		,{ "502", EmailStatusConstants.ERR_UNRECOGNIZED_COMMAND }
		,{ "503", EmailStatusConstants.ERR_HELO_FIRST }
		,{ "504", EmailStatusConstants.ERR_WRONG_AUTH_TYPE }
		,{ "530", EmailStatusConstants.ERR_AUTH_REQUIRED }
		,{ "535", EmailStatusConstants.ERR_AUTH_PROBLEM }
		,{ "550", EmailStatusConstants.ERR_RECIPIENT_NOT_FOUND }
		,{ "552", EmailStatusConstants.ERR_MAILBOX_NOT_FOUND }
		,{ "553", EmailStatusConstants.ERR_DOMAIN_NOT_FOUND }
		,{ "554 5.2.2", EmailStatusConstants.ERR_RECIPIENT_MAILBOX_FULL }
		,{ "555", EmailStatusConstants.ERR_SYNTAX_ERROR }
		,{ "server unavailable", EmailStatusConstants.ERR_DOMAIN_NOT_FOUND }
		,{ "Host or domain name not found", EmailStatusConstants.ERR_DOMAIN_NOT_FOUND }
		,{ "server unavailable or unable to receive mail", EmailStatusConstants.ERR_DOMAIN_NOT_FOUND }
		};


}




