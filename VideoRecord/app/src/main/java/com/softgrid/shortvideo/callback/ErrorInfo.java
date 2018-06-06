package com.softgrid.shortvideo.callback;

public class ErrorInfo {

	private int errorCode;
	private String errorMessage;
	private Exception errorException;
	
	public ErrorInfo(){
		this.errorCode = 0;
		this.errorMessage = "";
		this.errorException = null;
	};
	
	public ErrorInfo(int errorCode){
		this.errorException = null;
		this.errorMessage = "";
		this.errorCode = errorCode;
	}
	
	public ErrorInfo(String errorMessage){
		this.errorException = null;
		this.errorMessage = errorMessage;
		this.errorCode = 0;
	}
	
	public ErrorInfo(Exception errorException){
		this.errorException = errorException;
		this.errorMessage = "";
		this.errorCode = 0;
	}
	
	public ErrorInfo(int errorCode,String errorMessage){
		this.errorException = null;
		this.errorMessage = errorMessage;
		this.errorCode = errorCode;
	}
	
	public ErrorInfo(int errorCode, String errorMessage, Exception errorException){
		this.errorException = errorException;
		this.errorMessage = errorMessage;
		this.errorCode = errorCode;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public Exception getErrorException() {
		return errorException;
	}
	public void setErrorException(Exception errorException) {
		this.errorException = errorException;
	}
	
}
