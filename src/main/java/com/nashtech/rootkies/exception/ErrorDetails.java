package com.nashtech.rootkies.exception;

import java.time.LocalDateTime;

public class ErrorDetails {
	private LocalDateTime timestamp;
	private String errorCode;
	private String details;

	public ErrorDetails(LocalDateTime timestamp, String errorCode, String details) {
		super();
		this.timestamp = timestamp;
		this.errorCode = errorCode;
		this.details = details;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public String getMessage() {
		return errorCode;
	}

	public String getDetails() {
		return details;
	}
}
