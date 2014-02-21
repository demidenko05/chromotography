package org.demidenko05.android.chromatography.model;

import java.util.List;
import java.util.Set;

public class SeriesHead extends AbstractEntityWithName {
	private Column column;
	private Detector detector;
	private int wavelength;
	private String flowRate;
	private Set<Solvent> solvents;
	private List<SeriesBody> seriesBody;
	public Column getColumn() {
		return column;
	}
	public void setColumn(Column column) {
		this.column = column;
	}
	public Detector getDetector() {
		return detector;
	}
	public void setDetector(Detector detector) {
		this.detector = detector;
	}
	public Set<Solvent> getSolvents() {
		return solvents;
	}
	public void setSolvents(Set<Solvent> solvents) {
		this.solvents = solvents;
	}
	public int getWavelength() {
		return wavelength;
	}
	public void setWavelength(int wavelength) {
		this.wavelength = wavelength;
	}
	public String getFlowRate() {
		return flowRate;
	}
	public void setFlowRate(String flowRate) {
		this.flowRate = flowRate;
	}
	public List<SeriesBody> getSeriesBody() {
		return seriesBody;
	}
	public void setSeriesBody(List<SeriesBody> seriesBody) {
		this.seriesBody = seriesBody;
	}
}
