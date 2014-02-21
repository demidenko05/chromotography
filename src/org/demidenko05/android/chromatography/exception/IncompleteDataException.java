package org.demidenko05.android.chromatography.exception;

public class IncompleteDataException extends Exception {

	private static final long serialVersionUID = -1991519359810073981L;

	public IncompleteDataException() {
		super("The chosen series have incomplite data!");
	}
}
