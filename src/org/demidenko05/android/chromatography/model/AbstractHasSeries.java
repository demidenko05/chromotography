package org.demidenko05.android.chromatography.model;

public abstract class AbstractHasSeries extends AbstractEntity {
	private SeriesHead seriesHead;
	public SeriesHead getSeriesHead() {
		return seriesHead;
	}
	public void setSeriesHead(SeriesHead seriesHead) {
		this.seriesHead = seriesHead;
	}
}
