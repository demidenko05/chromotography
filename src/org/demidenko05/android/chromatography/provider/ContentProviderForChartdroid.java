package org.demidenko05.android.chromatography.provider;

import org.demidenko05.android.chromatography.DataToPlot;
import com.googlecode.chartdroid.core.ColumnSchema;
import com.googlecode.chartdroid.core.ColumnSchema.PlotData;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class ContentProviderForChartdroid extends ContentProvider {

	public static final String TAG = "ChartDroid for chromotography";
	
	// This must be the same as what as specified as the Content Provider authority
	// in the manifest file.
	public static final String AUTHORITY = "org.demidenko05.android.chromatography.provider";

	public static Uri PROVIDER_URI = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(AUTHORITY).build();

	static final String MESSAGE_UNSUPPORTED_FEATURE = "Not supported by this provider";

	@Override
	public boolean onCreate() {
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		DataToPlot dataToPlot = DataToPlot.getInstance();
		MatrixCursor c;
		if (ColumnSchema.Aspect.DATASET_ASPECT_AXES.equals( uri.getQueryParameter(ColumnSchema.DATASET_ASPECT_PARAMETER) )) {

			c = new MatrixCursor(new String[] {
					BaseColumns._ID,
					ColumnSchema.Aspect.Axes.COLUMN_AXIS_LABEL});

			int row_index = 0;
			String[] axesLabels = {"retention time, min", "milliabsorbance units (mAU)"};
			for (int i=0; i<axesLabels.length; i++) {

				c.newRow().add( row_index ).add( axesLabels[i] );
				row_index++;
			}

		} else if (ColumnSchema.Aspect.DATASET_ASPECT_SERIES.equals( uri.getQueryParameter(ColumnSchema.DATASET_ASPECT_PARAMETER) )) {

			c = new MatrixCursor(new String[] {
					BaseColumns._ID,
					ColumnSchema.Aspect.Series.COLUMN_SERIES_LABEL});

			int row_index = 0;
			String[] titles = dataToPlot.getTitles();
			for (int i=0; i<titles.length; i++) {

				c.newRow().add(row_index).add(titles[i]);
				row_index++;
			}

			return c;

		} else {
			// Fetch the actual data

			c = new MatrixCursor(new String[] {
					BaseColumns._ID,
					ColumnSchema.Aspect.Data.COLUMN_AXIS_INDEX,
					ColumnSchema.Aspect.Data.COLUMN_SERIES_INDEX,
					ColumnSchema.Aspect.Data.COLUMN_DATUM_VALUE,
					ColumnSchema.Aspect.Data.COLUMN_DATUM_LABEL
			});

			int row_index = 0;

			// Add x-axis data
			double[] xAxisData = dataToPlot.getXAxisData();
			for (int i=0; i<xAxisData.length; i++) {
				c.newRow()
				.add( row_index )
				.add( ColumnSchema.X_AXIS_INDEX )
				.add( 0 )   // Only create data for the first series.
				.add( xAxisData[i] )
				.add( null );

				row_index++;
			}

			// Add y-axis data
			double[][] yAxisDataList = dataToPlot.getYAxisDataList();
			for (int i=0; i<yAxisDataList.length; i++) {
				for (int j=0; j<yAxisDataList[i].length; j++) {

					//                    c.newRow().add( Y_AXIS_INDEX ).add( i ).add( TemperatureData.DEMO_SERIES_LIST[i][j] ).add( null );
					c.newRow()
					.add( row_index )
					.add( ColumnSchema.Y_AXIS_INDEX )
					.add( i )
					.add( yAxisDataList[i][j] )
					.add( null );

					row_index++;
				}
			}

		}
		return c;
	}

	@Override
	public String getType(Uri uri) {
		return PlotData.CONTENT_TYPE_PLOT_DATA;
		//return EventData.CONTENT_TYPE_PLOT_DATA;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		throw new UnsupportedOperationException(MESSAGE_UNSUPPORTED_FEATURE);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		throw new UnsupportedOperationException(MESSAGE_UNSUPPORTED_FEATURE);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		throw new UnsupportedOperationException(MESSAGE_UNSUPPORTED_FEATURE);
	}

}
