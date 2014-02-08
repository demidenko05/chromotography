package org.demidenko05.android.chromatography.exception;

public class SeriesNotSameDurationException extends Exception{
	private static final long serialVersionUID = -220096468430136503L;

	public SeriesNotSameDurationException() {
		super("The chosen series have not the same duration!");
	}
}
