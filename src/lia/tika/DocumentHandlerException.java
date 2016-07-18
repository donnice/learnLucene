package lia.tika;

import java.io.PrintStream;
import java.io.PrintWriter;

public class DocumentHandlerException extends Exception{
	private Throwable cause;
	
	public DocumentHandlerException(){
		super();
	}
	
	// constructs with message
	public DocumentHandlerException(String message) {
		super(message);
	}
	
	public DocumentHandlerException(Throwable cause) {
		super(cause.toString());
		this.cause = cause;
	}
	
	public DocumentHandlerException(String message, Throwable cause) {
		super(message,cause);
	}
	
	public Throwable getException() {
		return cause;
	}
	
	public void printStackTrace(PrintStream ps) {
		synchronized(ps) {
			super.printStackTrace(ps);
			if(cause != null) {
				ps.println("---Nested Exception---");
				cause.printStackTrace();
			}
		}
	}
	
	public void printStackTrace(PrintWriter pw) {
		synchronized(pw) {
			super.printStackTrace(pw);
			if(cause != null) {
				pw.println("---Nested Exception---");
				cause.printStackTrace(pw);
			}
		}
	}
}
