package org.demidenko05.android.chromatography.model;

public class SeriesBody extends AbstractHasSeries {
	private double time;
	private double value;
	private Analyte analyte;
	private int injection;

	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public Analyte getAnalyte() {
		return analyte;
	}
	public void setAnalyte(Analyte analyte) {
		this.analyte = analyte;
	}
	public int getInjection() {
		return injection;
	}
	public void setInjection(int injection) {
		this.injection = injection;
	}
}
