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
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

	private void renameAlert(final int position)
	{
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
		 alertDialog.setTitle("Rename");
		 alertDialog.setMessage("Rename");

		  final EditText input = new EditText(context);  
		  LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
		                        LinearLayout.LayoutParams.MATCH_PARENT,
		                        LinearLayout.LayoutParams.MATCH_PARENT);
		  input.setLayoutParams(lp);
		  alertDialog.setView(input);
		 alertDialog.setIcon(R.drawable.ic_launcher);

		 alertDialog.setPositiveButton("Ok",
		     new DialogInterface.OnClickListener() {
		         public void onClick(DialogInterface dialog, int which) {
		        	 fsc.rename(ids[position],input.getText().toString().replace("\r", "")
		        			 .replace("\n", "").replace("\"", "\\\""));
		         }
		     });

		 alertDialog.setNegativeButton("Cancel",
		     new DialogInterface.OnClickListener() {
		         public void onClick(DialogInterface dialog, int which) {
		             dialog.cancel();
		         }
		     });

		 alertDialog.show();
	}
	@SuppressLint("ViewHolder")
	public View getView(final int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.fsuvius_item, parent, false);
		
		rowView.setOnLongClickListener(new OnLongClickListener(){

			@Override
			public boolean onLongClick(View v) {
					//
				 renameAlert(position);
				 return true;
				 }

				 });
				
		Button dock = (Button) rowView.findViewById(R.id.dock);
		Button dock10 = (Button) rowView.findViewById(R.id.dock10);
		TextView name = (TextView) rowView.findViewById(R.id.name);
		name.setOnLongClickListener(new OnLongClickListener(){

			@Override
			public boolean onLongClick(View v) {
					//
				 renameAlert(position);
				 return true;
				 }

				 });
		Button boost = (Button) rowView.findViewById(R.id.boost);
		Button boost10 = (Button) rowView.findViewById(R.id.boost10);
		
		dock.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				fsc.dockFsu(ids[position],"1");
			}

		});
		dock10.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				fsc.dockFsu(ids[position],"10");
			}

		});
		name.setText(String.format("%s  %s: %s fsu", ids[position],
				names[position], fsu[position]));
		boost.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				fsc.boostFsu(ids[position],"1");
			}

		});
		boost10.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				fsc.boostFsu(ids[position],"10");
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
