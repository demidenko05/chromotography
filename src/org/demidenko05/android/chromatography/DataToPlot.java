package org.demidenko05.android.chromatography;

import java.util.Map;

import org.demidenko05.android.chromatography.model.SeriesHead;

public class DataToPlot {
	
	private Map<String, SeriesHead> seriesToShow;

	private String[] titles;
	
	private double[][] yAxisDataList;
	
	private double[] xAxisData;
	
	private static DataToPlot dataToPlot = new DataToPlot();
	
	public static DataToPlot getInstance() {
		return dataToPlot;
	}
	
	public Map<String, SeriesHead> getSeriesToShow() {
		return seriesToShow;
	}

	public void setSeriesToShow(Map<String, SeriesHead> seriesToShow) {
		this.seriesToShow = seriesToShow;
	}

	public String[] getTitles() {
		return titles;
	}

	public void setTitles(String[] titles) {
		this.titles = titles;
	}

	public double[][] getYAxisDataList() {
		return yAxisDataList;
	}

	public void setYAxisDataList(double[][] yAxisDataList) {
		this.yAxisDataList = yAxisDataList;
	}

	public double[] getXAxisData() {
		return xAxisData;
	}

	public void setXAxisData(double[] xAxisData) {
		this.xAxisData = xAxisData;
	}
	

}
