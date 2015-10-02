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
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FsuviusFragment extends Fragment implements FsuCallback {

	View rootView;

	public FsuviusFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fsuvius_layout, container, false);
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				new GetFsu().execute();
				handler.postDelayed(this, 2000);
			}
		}, 1500);
		return rootView;
	}

	private void updateFsu(String[] names, String[] fsu, String[] ids) {
		//

		ListView lv = ((ListView) rootView.findViewById(R.id.fsuList));
		if (lv.getAdapter() == null) {
			FsuviusListAdapter adapter = new FsuviusListAdapter(getActivity(),
					names, fsu, ids, this);
			lv.setAdapter(adapter);
		} else {
			((FsuviusListAdapter) lv.getAdapter()).update(names, fsu, ids);

		}
	}

	final Handler handler = new Handler();

	private String[][] parseData(String json) {
		//System.out.println("Parsing: "+json);
		String[][] result = new String[3][];
		ArrayList<String> namelist = new ArrayList<String>();
		ArrayList<String> fsulist = new ArrayList<String>();
		ArrayList<String> idlist = new ArrayList<String>();
		String[] split = json.split("\n");
		for (int i = 0; i < split.length; i++) {
			if (split[i].trim().startsWith("\"aid\"")) {
				idlist.add(split[i].substring(
						split[i].indexOf("\"aid\": ") + 7,
						split[i].lastIndexOf(",")));
				i++;
				String fsutmp = split[i].substring(
						split[i].indexOf("\"balance\": ") + 11,
						split[i].lastIndexOf(","));
				if (fsutmp.endsWith(".0"))
					fsutmp = fsutmp.replace(".0", "");
				fsulist.add(fsutmp);
				i++;
				namelist.add((split[i].substring(
						split[i].indexOf("\"name\": ") + 9,
						split[i].length() - 1)).replace("\\", ""));
			}
		}
		String[] ids = new String[idlist.size()];
		String[] fsu = new String[fsulist.size()];
		String[] names = new String[namelist.size()];
		for (int i = 0; i < namelist.size(); i++) {
			ids[i] = idlist.get(i);
			fsu[i] = fsulist.get(i);
			names[i] = namelist.get(i);
		}
		result[0] = names;
		result[1] = fsu;
		result[2] = ids;
		return result;
	}

	private class GetFsu extends AsyncTask<Void, Void, String[][]> {

		@Override
		protected String[][] doInBackground(Void... params) {
			String[][] res;
			try {
				URL url = new URL("http://fsuvius.cslabs.clarkson.edu/get");
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setDoInput(true);
				connection.setInstanceFollowRedirects(false);
				connection.setRequestMethod("POST");
				connection
						.setRequestProperty("Content-Type", "application/xml");
				InputStream is = connection.getInputStream();
				int i;
				StringBuilder s = new StringBuilder();
				while ((i = is.read()) != -1) {
					s.append((char) i);
				}
				res = parseData(s.toString());
				connection.disconnect();
			} catch (Exception e) {
				return null;
			}

			return res;
		}

		@Override
		protected void onPostExecute(String[][] result) {
			if (result != null)
				updateFsu(result[0], result[1], result[2]);
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}

	private class AddFsu extends AsyncTask<String, Void, Void> {

		private static final String LINE_FEED = "\r\n";

		@Override
		protected Void doInBackground(String... params) {
			try {
				URL url = new URL("http://fsuvius.cslabs.clarkson.edu/mod");

				System.out.println("executing");
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setUseCaches(false);
				connection.setDoOutput(true); // indicates POST method
				connection.setDoInput(true);
				connection.setRequestMethod("POST");
				connection.setDoOutput(true);
				String boundary = "===" + System.currentTimeMillis() + "===";
				connection.setRequestProperty("Content-Type",
						"multipart/form-data; boundary=" + boundary);
				OutputStream os = connection.getOutputStream();
				PrintWriter writer = new PrintWriter(new OutputStreamWriter(os,
						"UTF-8"), true);
				writer.append("--" + boundary).append(LINE_FEED);
				writer.append(
						"Content-Disposition: form-data; name=\"" + "aid"
								+ "\"").append(LINE_FEED);
				writer.append("Content-Type: text/plain; charset=" + "UTF-8")
						.append(LINE_FEED);
				writer.append(LINE_FEED);
				writer.append(params[0]).append(LINE_FEED);
				writer.append("--" + boundary).append(LINE_FEED);
				writer.append(
						"Content-Disposition: form-data; name=\"" + "amt"
								+ "\"").append(LINE_FEED);
				writer.append("Content-Type: text/plain; charset=" + "UTF-8")
						.append(LINE_FEED);
				writer.append(LINE_FEED);
				writer.append(params[1]).append(LINE_FEED);
				writer.flush();
				writer.append(LINE_FEED).flush();
				writer.append("--" + boundary + "--").append(LINE_FEED);
				writer.close();
				os.close();
				connection.getResponseCode();
				connection.disconnect();

			} catch (Exception e) {
				return null;
			}
			return null;

		}

		@Override
		protected void onPostExecute(Void v) {
			new GetFsu().execute();
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}

	private class SubFsu extends AsyncTask<String, Void, Void> {

		private static final String LINE_FEED = "\r\n";

		@Override
		protected Void doInBackground(String... params) {
			try {
				URL url = new URL("http://fsuvius.cslabs.clarkson.edu/mod");

				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setUseCaches(false);
				connection.setDoOutput(true); // indicates POST method
				connection.setDoInput(true);
				connection.setRequestMethod("POST");
				connection.setDoOutput(true);
				String boundary = "===" + System.currentTimeMillis() + "===";
				connection.setRequestProperty("Content-Type",
						"multipart/form-data; boundary=" + boundary);
				OutputStream os = connection.getOutputStream();
				PrintWriter writer = new PrintWriter(new OutputStreamWriter(os,
						"UTF-8"), true);
				writer.append("--" + boundary).append(LINE_FEED);
				writer.append(
						"Content-Disposition: form-data; name=\"" + "aid"
								+ "\"").append(LINE_FEED);
				writer.append("Content-Type: text/plain; charset=" + "UTF-8")
						.append(LINE_FEED);
				writer.append(LINE_FEED);
				writer.append(params[0]).append(LINE_FEED);
				writer.flush();
				writer.append("--" + boundary).append(LINE_FEED);
				writer.append(
						"Content-Disposition: form-data; name=\"" + "amt"
								+ "\"").append(LINE_FEED);
				writer.append("Content-Type: text/plain; charset=" + "UTF-8")
						.append(LINE_FEED);
				writer.append(LINE_FEED);
				writer.append(params[1]).append(LINE_FEED);
				writer.flush();
				writer.append("--" + boundary).append(LINE_FEED);
				writer.append(
						"Content-Disposition: form-data; name=\"" + "dock"
								+ "\"").append(LINE_FEED);
				writer.append("Content-Type: text/plain; charset=" + "UTF-8")
						.append(LINE_FEED);
				writer.append(LINE_FEED);
				writer.append("1").append(LINE_FEED);
				writer.flush();
				writer.append(LINE_FEED).flush();
				writer.append("--" + boundary + "--").append(LINE_FEED);
				writer.close();
				os.close();
				connection.getResponseCode();
				connection.disconnect();
			} catch (Exception e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			new GetFsu().execute();
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}
	private class RenameFsu extends AsyncTask<String, Void, Void> {

		private static final String LINE_FEED = "\r\n";

		@Override
		protected Void doInBackground(String... params) {
			try {
				URL url = new URL("http://fsuvius.cslabs.clarkson.edu/mv");

				System.out.println("executing");
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setUseCaches(false);
				connection.setDoOutput(true); // indicates POST method
				connection.setDoInput(true);
				connection.setRequestMethod("POST");
				connection.setDoOutput(true);
				String boundary = "===" + System.currentTimeMillis() + "===";
				connection.setRequestProperty("Content-Type",
						"multipart/form-data; boundary=" + boundary);
				OutputStream os = connection.getOutputStream();
				PrintWriter writer = new PrintWriter(new OutputStreamWriter(os,
						"UTF-8"), true);
				writer.append("--" + boundary).append(LINE_FEED);
				writer.append(
						"Content-Disposition: form-data; name=\"" + "aid"
								+ "\"").append(LINE_FEED);
				writer.append("Content-Type: text/plain; charset=" + "UTF-8")
						.append(LINE_FEED);
				writer.append(LINE_FEED);
				writer.append(params[0]).append(LINE_FEED);
				writer.append("--" + boundary).append(LINE_FEED);
				writer.append(
						"Content-Disposition: form-data; name=\"" + "name"
								+ "\"").append(LINE_FEED);
				writer.append("Content-Type: text/plain; charset=" + "UTF-8")
						.append(LINE_FEED);
				writer.append(LINE_FEED);
				writer.append(params[1])/*.append(LINE_FEED)*/;
				writer.flush();
				writer.append(LINE_FEED).flush();
				writer.append("--" + boundary + "--").append(LINE_FEED);
				writer.close();
				os.close();
				connection.getResponseCode();
				connection.disconnect();

			} catch (Exception e) {
				return null;
			}
			return null;

		}

		@Override
		protected void onPostExecute(Void v) {
			new GetFsu().execute();
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}
	@Override
	public void dockFsu(String name, String amt) {
		new SubFsu().execute(name, amt);

	}

	@Override
	public void boostFsu(String name, String amt) {
		new AddFsu().execute(name, amt);

	}
	@Override
	public void rename(String aid, String name) {
		new RenameFsu().execute(aid, name);
	}
}
