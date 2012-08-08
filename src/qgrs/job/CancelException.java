package qgrs.job;

public class CancelException extends Exception {

	public CancelException () {
		super("Operation was cancelled by user");
	}
}
