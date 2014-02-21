package org.demidenko05.android.chromatography.model;

public class SeriesSolvents extends AbstractHasSeries {
	private Solvent solvent;
	private String amount;
	@Override
	public String toString() {
		return (solvent == null ? "" : solvent.getName()) + (amount == null ? "" : " " + amount);
	}
	public Solvent getSolvent() {
		return solvent;
	}
	public void setSolvent(Solvent solvent) {
		this.solvent = solvent;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
}
