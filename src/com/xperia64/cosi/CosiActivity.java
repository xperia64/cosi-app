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
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Random;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class CosiActivity extends AppCompatActivity implements MainCallback {

	private int frag = 0;
	public static boolean shouldDoDocs = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme((PreferenceManager.getDefaultSharedPreferences(this).getBoolean("darkTheme", false))?R.style.Theme_AppCompat:
			R.style.Theme_AppCompat_Light_DarkActionBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cosi);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new MainFragment()).commit();
		}else{
			frag = savedInstanceState.getInt("fragNum",0);
			if(frag!=0)
			{
				getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
		shouldDoDocs = (new Random().nextInt(10000)<9998); // Should we load docs or docs.cl?
		Bundle extras = getIntent().getExtras();
		if(extras!=null)
		{
			if(extras.containsKey("cosiFullMsg"))
			{
				new AlertDialog.Builder(this)
			    .setTitle("COSI Alert")
			    .setMessage(extras.getString("cosiFullMsg"))
			    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			        }
			     })
			    .setIcon(R.drawable.ic_launcher)
			     .show();
			}
		}
		if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("cosiNotify", false))
		{
			scheduleAlarm();
		}else{
			cancelAlarm();
		}
		
	}
	public boolean checkAlarm()
	{
		return (PendingIntent.getBroadcast( this.getApplicationContext(), NagReceiver.REQUEST_CODE, new Intent ( this, NagReceiver.class ), 0 ) 
				== null);
	}
	public void cancelAlarm() {
		Intent alarmIntent = null;
		 PendingIntent pendingIntent = null;
		 AlarmManager alarmManager = null;

		     // OnCreate()
		     alarmIntent = new Intent ( this, NagReceiver.class );
		     pendingIntent = PendingIntent.getBroadcast( this.getApplicationContext(), NagReceiver.REQUEST_CODE, alarmIntent, 0 );
		     alarmManager = ( AlarmManager ) getSystemService( ALARM_SERVICE );
		     alarmManager.cancel(pendingIntent);
	}
	 public void scheduleAlarm() {
		 if(checkAlarm())
		 {
			 return;
		 }
		 final int interval = 60*30;
		 Intent alarmIntent = null;
		 PendingIntent pendingIntent = null;
		 AlarmManager alarmManager = null;

		     // OnCreate()
		     alarmIntent = new Intent ( this, NagReceiver.class );
		     pendingIntent = PendingIntent.getBroadcast( this.getApplicationContext(), NagReceiver.REQUEST_CODE, alarmIntent, 0 );
		     alarmManager = ( AlarmManager ) getSystemService( ALARM_SERVICE );
		     alarmManager.setRepeating( AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),( interval * 1000 ), pendingIntent );
		  }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cosi, menu);
		return true;
	}

	@Override
	public void onBackPressed()
	{
		if(frag==2||frag==5)
		{
			frag = 0;
			loadFragment(frag);
		}else if(frag==1||frag==3||frag==4)
		{
			WebFragment wf = ((WebFragment)getSupportFragmentManager().getFragments().get(0));
			if(wf.goBack())
			{
				frag = 0;
				loadFragment(frag);
			}
		}else{
			this.finish();
		}
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            if(frag!=0)
            {
            	onBackPressed();
            }
            return true;
        case R.id.action_settings:
            frag = 5;
            loadFragment(frag);
            return true;
        case R.id.action_not:
        	new Nag2Task().execute();
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	private class Nag2Task extends AsyncTask<Void, Void, String>
    {

    	@Override
		protected String doInBackground(Void... params) {

		        String sss = "::: Error retrieving announcement";
		        try {
		        	URL url = new URL("http://cslabs.clarkson.edu/announcement.txt");
		        	//URL url = new URL("https://dl.dropboxusercontent.com/u/29967887/nag");
		        	HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					InputStream issss = (InputStream)connection.getInputStream();
					int num;
					StringBuilder sb = new StringBuilder();
					while((num=issss.read())!=-1)
					{
						sb.append((char)num);
					}
					sss = sb.toString();
				} catch (Exception e) {
					e.printStackTrace();
				}
		        
		        if(sss==null||!sss.contains("::: "))
		        	sss = "::: Error retrieving announcement";
			String cosiMsg=sss.substring(sss.indexOf("::: ")+4);
			
			return cosiMsg;
		}
    	@Override
    	protected void onPostExecute(String s)
    	{
    		new AlertDialog.Builder(CosiActivity.this)
		    .setTitle("COSI Alert")
		    .setMessage(s)
		    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		        }
		     })
		    .setIcon(R.drawable.ic_launcher)
		     .show();
    	}
    	
    }
	
	
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	    // Save the user's current game state
	    savedInstanceState.putInt("fragNum", frag);
	    
	    // Always call the superclass so it can save the view hierarchy state
	    super.onSaveInstanceState(savedInstanceState);
	}
	
	public String getLocalIpAddress() {
	    try {
	        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
	            NetworkInterface intf = en.nextElement();
	            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
	                InetAddress inetAddress = enumIpAddr.nextElement();
	                if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
	                    return inetAddress.getHostAddress();
	                }
	            }
	        }
	    } catch (SocketException ex) {
	        ex.printStackTrace();
	    }
	    return null;
	}
	public boolean isCosiIP()
	{
		String ip = getLocalIpAddress();
		if(ip!=null&&(ip.startsWith("128.153.")||ip.startsWith("128.153.")))
		{
			return true;
		}
		return false;
	}
	@Override
	public void loadFragment(int which) {
		Fragment f = null;
		
		switch(which)
		{
		case 0:
			frag = which;
			f = new MainFragment();
			getSupportActionBar().setTitle("COSI");
			getSupportActionBar().setDisplayShowTitleEnabled(true);
			getSupportActionBar().setHomeButtonEnabled(false);
			getSupportActionBar().setDisplayHomeAsUpEnabled(false);
			getSupportActionBar().setDisplayShowHomeEnabled(false);
			getSupportActionBar().setDisplayUseLogoEnabled(false);
			break;
		case 1:
			frag = which;
			f = new WebFragment(shouldDoDocs?"http://docs.cslabs.clarkson.edu":"http://docs.cl");
			getSupportActionBar().setTitle("Docs");
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			break;
		case 2:
			if(!isCosiIP())
			{
				Toast.makeText(this, "Error: Must be on COSI network to use Fsuvius", Toast.LENGTH_LONG).show();
				return;
			}
			frag = which;
			f = new FsuviusFragment();
			getSupportActionBar().setTitle("Fsuvius");
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			break;
		case 3:
			if(!isCosiIP())
			{
				Toast.makeText(this, "Error: Must be on COSI network to use print", Toast.LENGTH_LONG).show();
				return;
			}
			frag = which;
			f = new WebFragment("http://print.cslabs.clarkson.edu");
			getSupportActionBar().setTitle("Print");
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			break;
		case 4:
			frag = which;
			f = new WebFragment("http://cosi-lab.github.io/meeting-minutes");
			getSupportActionBar().setTitle("Minutes");
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			break;
		case 5:
			frag = which;
			f = new PrefsFragment();
			getSupportActionBar().setTitle("Preferences");
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			break;
			
		}
		if(f!=null)
			getSupportFragmentManager().beginTransaction().replace(R.id.container, f).commit();
		
	}
}
