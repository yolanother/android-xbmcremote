/*
 *      Copyright (C) 2005-2009 Team XBMC
 *      http://xbmc.org
 *
 *  This Program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2, or (at your option)
 *  any later version.
 *
 *  This Program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with XBMC Remote; see the file license.  If not, write to
 *  the Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139, USA.
 *  http://www.gnu.org/copyleft/gpl.html
 *
 */

package com.doubtech.universalremote.modules.xbmc.presentation.controller;

import org.xbmc.android.util.HostFactory;
import org.xbmc.android.util.MacAddressResolver;
import org.xbmc.api.object.Host;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.doubtech.universalremote.modules.xbmc.R;

/**
 * One of those contains name, host, port, user and pass of an XBMC instance.
 * 
 * @author Team XBMC
 */
public class AddHostActivity extends Activity {
	
	private EditText mNameView, mHostView, mPortView, mUserView, mPassView, 
				mEsPortView, mTimeoutView, mAccPointView, mMacAddrView, mWolWaitView, mWolPortView;
	
	private CheckBox mWifiOnlyView;
	
	private Host mHost;
	private Context mContext;
	
	public static final String ID_PREFIX = "settings_host_";
	
	public void setHost(Host host) {
		mHost = host;
		setTitle(host.name);
	}
	
	public Host getHost() {
		return mHost;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.host_preference_menu, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()) {
		case R.id.action_save:
			save();
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preference_host);

		if (mHost != null) {
			ImageView btn = new ImageView(this);
			btn.setImageResource(R.drawable.bubble_del_up);
			btn.setClickable(true);
			btn.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					AlertDialog.Builder builder = new AlertDialog.Builder(AddHostActivity.this);
					builder.setMessage("Are you sure you want to delete the XBMC host \"" + mHost.name + "\"?");
					builder.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							HostFactory.deleteHost(AddHostActivity.this, mHost);
						}
					});
					builder.setNegativeButton("Nah.", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
					builder.create().show();
				}
			});
			//addView(btn);
		}

		mNameView = (EditText)findViewById(R.id.pref_name);
		mHostView = (EditText)findViewById(R.id.pref_host);
		mHostView.setOnFocusChangeListener(new OnFocusChangeListener() {
			Handler handler = new Handler(){
				public void handleMessage(android.os.Message message){
					if(message.getData().containsKey(MacAddressResolver.MESSAGE_MAC_ADDRESS)){
						String mac = message.getData().getString(MacAddressResolver.MESSAGE_MAC_ADDRESS);
						if(!mac.equals("")){
							mMacAddrView.setText(mac);
							Toast toast = Toast.makeText(AddHostActivity.this, "Updated MAC for host: " + mHostView.getText().toString() + "\nto: " + mac, Toast.LENGTH_SHORT);
							toast.show();
						}
						
					}
				}
			};
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)
					return;
				if(mMacAddrView.getText().toString().equals(""))
					handler.post(new MacAddressResolver(mHostView.getText().toString(), handler));
			}
		});
		mPortView = (EditText)findViewById(R.id.pref_port);
		mUserView = (EditText)findViewById(R.id.pref_user);
		mPassView = (EditText)findViewById(R.id.pref_pass);
		mEsPortView = (EditText)findViewById(R.id.pref_eventserver_port);
		mTimeoutView = (EditText)findViewById(R.id.pref_timeout);
		mMacAddrView = (EditText)findViewById(R.id.pref_mac_addr);
		mAccPointView = (EditText)findViewById(R.id.pref_access_point);
		mWifiOnlyView = (CheckBox)findViewById(R.id.pref_wifi_only);
		mWolPortView = (EditText)findViewById(R.id.pref_wol_port);
		mWolWaitView = (EditText)findViewById(R.id.pref_wol_wait);

		if (mHost != null) {
			mNameView.setText(mHost.name);
			mHostView.setText(mHost.addr);
			mPortView.setText(String.valueOf(mHost.port));
			mUserView.setText(mHost.user);
			mPassView.setText(mHost.pass);
			
			mEsPortView.setText(String.valueOf(mHost.esPort));
			mTimeoutView.setText(String.valueOf(mHost.timeout));
			mMacAddrView.setText(mHost.mac_addr);
			mAccPointView.setText(mHost.access_point);
			mWifiOnlyView.setChecked(mHost.wifi_only);
			mWolPortView.setText(String.valueOf(mHost.wol_port));
			mWolWaitView.setText(String.valueOf(mHost.wol_wait));
		} else {
			//set defaults:
			mPortView.setText("" + Host.DEFAULT_HTTP_PORT);
			mEsPortView.setText("" + Host.DEFAULT_EVENTSERVER_PORT);
			mTimeoutView.setText("" + Host.DEFAULT_TIMEOUT);
			mWolPortView.setText("" + Host.DEFAULT_WOL_PORT);
			mWolWaitView.setText("" + Host.DEFAULT_WOL_WAIT);
		}
	}

	public void save() {
		final Host host = new Host();
		host.name = mNameView.getText().toString();
		host.addr = mHostView.getText().toString().trim();
		try {
			host.port = Integer.parseInt(mPortView.getText().toString());
		} catch (NumberFormatException e) {
			host.port = Host.DEFAULT_HTTP_PORT;
		}
		host.user = mUserView.getText().toString();
		host.pass = mPassView.getText().toString();

		try {
			host.esPort = Integer.parseInt(mEsPortView.getText().toString());
		} catch (NumberFormatException e) {
			host.esPort = Host.DEFAULT_EVENTSERVER_PORT;
		}
		try {
			host.timeout = Integer.parseInt(mTimeoutView.getText().toString());
		} catch (NumberFormatException e) {
			host.timeout = Host.DEFAULT_TIMEOUT;
		}
		host.access_point = mAccPointView.getText().toString();
		host.mac_addr = mMacAddrView.getText().toString();
		host.wifi_only = mWifiOnlyView.isChecked();
		try {
			host.wol_port = Integer.parseInt(mWolPortView.getText().toString());
		}catch (NumberFormatException e) {
			host.wol_port = Host.DEFAULT_WOL_PORT;
		}
		try {
			host.wol_wait = Integer.parseInt(mWolWaitView.getText().toString());
		}catch (NumberFormatException e) {
			host.wol_wait = Host.DEFAULT_WOL_WAIT;
		}
		
		
		if (mHost == null) {
			HostFactory.addHost(this, host);
		} else {
			host.id = mHost.id;
			HostFactory.updateHost(this, host);
		}
		setHost(host);
		
		if (HostFactory.host == null) {
			HostFactory.saveHost(mContext, host);
		}
	}
}