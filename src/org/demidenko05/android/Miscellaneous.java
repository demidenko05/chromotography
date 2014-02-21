package org.demidenko05.android;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;

public class Miscellaneous {

	public static void alertDlg(int message, Context ctx) {
		AlertDialog.Builder builder = new Builder(ctx);
	    builder
	    .setMessage(message)
	    .setCancelable(false)
	    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	    	public void onClick(DialogInterface dialog, int id) {
	    		dialog.cancel();
	    	}})
	    .show();
	}

}
