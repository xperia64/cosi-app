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
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xperia64.cosi.MainFragment.MainMenuItem;
import com.xperia64.cosi.R;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MainMenuViewHolder>{
			 
			List<MainMenuItem> mainitems;
			ArrayList<Boolean> mSelectedRows = new ArrayList<Boolean>();
			Context cc;
			MainCallback mmm;
			RVAdapter(List<MainMenuItem> mmi, Context c, MainCallback mmm)
			{
				this.mmm = mmm;
				cc = c;
				mainitems = mmi;
			}
		    public class MainMenuViewHolder extends RecyclerView.ViewHolder implements OnClickListener {      
		        CardView cv;
		        TextView mainName;
		        ImageView mainPhoto;
		        int id;
		        MainMenuViewHolder(View itemView) {
		            super(itemView);
		            cv = (CardView)itemView.findViewById(R.id.cv);
		            mainName = (TextView)itemView.findViewById(R.id.thing_name);
		            mainPhoto = (ImageView)itemView.findViewById(R.id.thing_photo);
		            cv.setOnClickListener(this);
		           
		        }
		        @Override
	            public void onClick(View v) {
	                if (v instanceof CardView){
	                   TextView text = (TextView) v.findViewById(R.id.thing_name);
	                   if(text!=null)
	                   {
	                	   if(text.getText().equals("Docs"))
	                	   {
	                		   mmm.loadFragment(1);
	                	   }
	                	   if(text.getText().equals("Fsuvius"))
	                	   {
	                		   mmm.loadFragment(2);
	                	   }
	                	   if(text.getText().equals("Print"))
	                	   {
	                		   mmm.loadFragment(3);
	                	   }
	                	   if(text.getText().equals("Minutes"))
	                	   {
	                		   mmm.loadFragment(4);
	                	   }
	                   }
	                }
	            }
		    }

			@Override
			public int getItemCount() {
				return mainitems.size();
			}
			@Override
			public void onAttachedToRecyclerView(RecyclerView recyclerView) {
			    super.onAttachedToRecyclerView(recyclerView);
			}
			@Override
			public void onBindViewHolder(MainMenuViewHolder personViewHolder, final int i) {
			    personViewHolder.mainName.setText(mainitems.get(i).name);
			    personViewHolder.mainPhoto.setImageResource(mainitems.get(i).imageId);
			    personViewHolder.cv.setSelected(mSelectedRows.contains(i));
			}

			@Override
			public MainMenuViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
			    View v = null;
			    if(PreferenceManager.getDefaultSharedPreferences(cc).getBoolean("darkTheme", false))
			    {
			    	v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dark_cardlayout, viewGroup, false);
			    }else{
			    	v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_cardlayout, viewGroup, false);
			    }
			    MainMenuViewHolder pvh = new MainMenuViewHolder(v);
			    return pvh;
			}
		 
		}