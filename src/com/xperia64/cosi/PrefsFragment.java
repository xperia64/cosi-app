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
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.support.v4.preference.PreferenceFragment;

@SuppressLint("NewApi")
public class PrefsFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		CheckBoxPreference cp = (CheckBoxPreference) this
				.findPreference("cosiNotify");
		cp.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {

				if ((Boolean) newValue) {
					((CosiActivity) getActivity()).scheduleAlarm();
				} else {
					((CosiActivity) getActivity()).cancelAlarm();
				}
				return true;
			}

		});
		CheckBoxPreference cp2 = (CheckBoxPreference) this
				.findPreference("darkTheme");
		cp2.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {
				getActivity().recreate();
				return true;
			}

		});
	}
}
