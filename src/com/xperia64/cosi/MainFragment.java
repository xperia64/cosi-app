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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainFragment extends Fragment {
	
		
		public MainFragment() {
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
			AutofitRecyclerView rv = (AutofitRecyclerView)rootView.findViewById(R.id.rv);
			initializeData();
			RVAdapter adapter = new RVAdapter(mainmen, getActivity(), (MainCallback)getActivity());
			rv.setAdapter(adapter);
			return rootView;
		}
		class MainMenuItem {
			String name;
			int imageId;
			MainMenuItem(String name, int photoId)
			{
				this.name = name;
				this.imageId = photoId;
			}
		}
		private List<MainMenuItem> mainmen;
		private void initializeData()
		{
			mainmen = new ArrayList<MainMenuItem>();
			mainmen.add(new MainMenuItem("Docs",(CosiActivity.shouldDoDocs?R.drawable.docs:R.drawable.docs_alt)));
			mainmen.add(new MainMenuItem("Fsuvius",R.drawable.fsu));
			mainmen.add(new MainMenuItem("Print",R.drawable.print));
			mainmen.add(new MainMenuItem("Minutes",R.drawable.minutes));
		}
		
		
	    

}
