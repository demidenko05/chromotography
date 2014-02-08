package org.demidenko05.android.chromatography.model;

import android.content.ContentValues;

public abstract class AbstractHasSeries extends AbstractEntity {
	private SeriesHead seriesHead;
	@Override
	public ContentValues getValuesMap() {
		ContentValues valuesMap = super.getValuesMap();
		valuesMap.put(COLUMN_ID_SERIES, getSeriesHead().getId());
		return valuesMap;
	}
	public SeriesHead getSeriesHead() {
		return seriesHead;
	}
	public void setSeriesHead(SeriesHead seriesHead) {
		this.seriesHead = seriesHead;
	}
}
