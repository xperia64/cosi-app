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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

public class FsuviusListAdapter extends BaseAdapter implements ListAdapter {

	private String[] names;
	private String[] ids;
	private String[] fsu;
	private final Activity context;
	private final FsuCallback fsc;

	public FsuviusListAdapter(Context context, String[] names, String[] fsu,
			String[] ids, FsuCallback fsc) {
		this.context = (Activity) context;
		this.names = names;
		this.fsu = fsu;
		this.fsc = fsc;
		this.ids = ids;
	}

	public void update(String[] names, String[] fsu, String[] ids) {
		this.names = names;
		this.fsu = fsu;
		this.ids = ids;
		context.runOnUiThread(new Runnable() {
			public void run() {
				notifyDataSetChanged();
			}
		});
	}

	@SuppressLint("ViewHolder")
	public View getView(final int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.fsuvius_item, parent, false);
		Button dock = (Button) rowView.findViewById(R.id.dock);
		TextView name = (TextView) rowView.findViewById(R.id.name);
		Button boost = (Button) rowView.findViewById(R.id.boost);

		dock.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				fsc.dockFsu(ids[position]);
			}

		});
		name.setText(String.format("%s  %s: %s fsu", ids[position],
				names[position], fsu[position]));
		boost.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				fsc.boostFsu(ids[position]);
			}

		});
		return rowView;

	}

	@Override
	public int getCount() {
		return names.length;
	}

	@Override
	public Object getItem(int position) {
		String[] stuff = { names[position], fsu[position] };
		return stuff;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	};

}
