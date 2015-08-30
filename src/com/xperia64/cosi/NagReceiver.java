/*******************************************************************************
 *   This file is part of COSI: The App.
 *   
 *   COSI: The App is free software: you can redistribute it and/or modify it under the terms of the
 *   GNU General Public License as published by the Free Software Foundation, either version 2 of the
 *   License, or (at your option) any later version.
 *   
 *   COSI: The App is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *   without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *   
 *   See the GNU General Public License for more details. You should have received a copy of the GNU
 *   General Public License along with COSI: The App. If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package com.xperia64.cosi;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

public class NagReceiver extends BroadcastReceiver {
	final static int REQUEST_CODE = 0xf00ba;

	@Override
	public void onReceive(Context context, Intent intent) {
		new NagTask().execute(context);
	}

	private class NagTask extends AsyncTask<Context, Void, Void> {

		@Override
		protected Void doInBackground(Context... params) {
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(params[0]);

			long aa = prefs.getLong("cosiNagId", 0);
			String sss = "";
			try {
				URL url = new URL("http://cslabs.clarkson.edu/announcement.txt");
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				InputStream issss = (InputStream) connection.getInputStream();
				int num;
				StringBuilder sb = new StringBuilder();
				while ((num = issss.read()) != -1) {
					sb.append((char) num);
				}
				sss = sb.toString();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			int bb = Integer.parseInt(sss.substring(0, sss.indexOf("::: ")));
			if (bb <= aa) {
				return null;
			}
			aa++;
			prefs.edit().putLong("cosiNagId", aa).commit();

			String cosiMsg = sss.substring(sss.indexOf("::: ") + 4);
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					params[0]).setSmallIcon(R.drawable.ic_launcher)
					.setContentTitle("COSI Alert").setContentText(cosiMsg);
			mBuilder.setDefaults(Notification.DEFAULT_SOUND
					| Notification.DEFAULT_VIBRATE);
			Intent resultIntent = new Intent(params[0], CosiActivity.class);
			resultIntent.putExtra("cosiFullMsg", cosiMsg);
			PendingIntent resultPendingIntent = PendingIntent.getActivity(
					params[0], 0, resultIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			mBuilder.setContentIntent(resultPendingIntent);
			int mNotificationId = 186;
			NotificationManager mNotifyMgr = (NotificationManager) params[0]
					.getSystemService(Context.NOTIFICATION_SERVICE);
			mNotifyMgr.notify(mNotificationId, mBuilder.build());
			return null;
		}

	}

}
