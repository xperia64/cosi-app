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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebFragment extends Fragment {

	private String curURL;
	WebView webview;

	public WebFragment(String url) {
		curURL = url;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);

	}

	public boolean goBack() {
		if (webview.canGoBack()) {
			webview.goBack();
			return false;
		} else {
			return true;
		}
	}

	@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.web_layout, container, false);

		if (curURL != null) {

			webview = (WebView) view.findViewById(R.id.web);
			webview.getSettings().setJavaScriptEnabled(true);
			webview.setWebViewClient(new webClient());
			webview.getSettings().setBuiltInZoomControls(true);
			try {
				webview.getSettings().setDisplayZoomControls(false);
			} catch (Exception e) {
			}
			webview.getSettings().setUseWideViewPort(true);
			webview.getSettings().setLoadWithOverviewMode(true);
			webview.loadUrl(curURL);

		}

		return view;

	}

	@SuppressLint("SetJavaScriptEnabled")
	public void updateUrl(String url) {
		curURL = url;
		WebView webview = (WebView) getView().findViewById(R.id.web);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.loadUrl(url);
	}

	private class webClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			return false;

		}

	}

}
