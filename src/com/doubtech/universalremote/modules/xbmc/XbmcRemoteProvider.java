package com.doubtech.universalremote.modules.xbmc;

import java.util.ArrayList;

import org.xbmc.android.util.ClientFactory;
import org.xbmc.android.util.HostFactory;
import org.xbmc.api.business.DataResponse;
import org.xbmc.api.business.IEventClientManager;
import org.xbmc.api.business.INotifiableManager;
import org.xbmc.api.data.IControlClient;
import org.xbmc.api.object.Host;
import org.xbmc.api.presentation.INotifiableController;
import org.xbmc.eventclient.ButtonCodes;
import org.xbmc.httpapi.WifiStateException;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.doubtech.universalremote.modules.xbmc.business.Command;
import com.doubtech.universalremote.modules.xbmc.business.ManagerFactory;
import com.doubtech.universalremote.modules.xbmc.presentation.controller.AddHostActivity;
import com.doubtech.universalremote.providers.AbstractUniversalRemoteProvider;
import com.doubtech.universalremote.providers.providerdo.Button;
import com.doubtech.universalremote.providers.providerdo.Button.ButtonBuilder;
import com.doubtech.universalremote.providers.providerdo.Parent;
import com.doubtech.universalremote.providers.providerdo.Parent.ParentBuilder;
import com.doubtech.universalremote.utils.ButtonIdentifier;
import com.doubtech.universalremote.utils.ButtonIds;

public class XbmcRemoteProvider extends AbstractUniversalRemoteProvider {
	protected static final String TAG = "UniversalRemote::XbmcRemoteProvider";
	private Handler mHandler;
	private IEventClientManager mEventClientManager;
	private Host mActiveHost;
	private INotifiableController mNotifiableManager = new INotifiableController() {

		public void onWrongConnectionState(int state,
				INotifiableManager manager,
				Command<?> source) {
			// TODO Auto-generated method stub
			
		}

		public void onError(Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}

		public void onMessage(String message) {
			Log.d(TAG, message);
			XbmcRemoteProvider.this.onMessage(message);
		}

		public void runOnUI(Runnable action) {
			// TODO Auto-generated method stub
			
		}
	};
	private IControlClient mControlClient;

	public XbmcRemoteProvider() {
		mHandler = new Handler(Looper.getMainLooper());
	}
	
	@Override
	public String getAuthority() {
		return "com.doubtech.universalremote.modules.xbmc.RemoteProvider";
	}
	
	@Override
	public String getProviderDescription() {
		return "Control XBMC devices";
	}
	
	@Override
	public String getProviderName() {
		return "XBMC";
	}
	
	@Override
	public boolean isProviderEnabled() {
		return true;
	}
	
	@Override
	public int getProviderIcon() {
		return R.drawable.icon;
	}
	
	@Override
	public int getIconId(Parent button) {
		if(isAddButton(button)) {
			return R.drawable.menu_add_host;
		}
		return super.getIconId(button);
	}

	@Override
	public Parent[] getChildren(Parent parent) {
		if(isRoot(parent)) {
			final ArrayList<Host> hosts = HostFactory.getHosts(getContext().getApplicationContext());
			Parent[] nodes = new Parent[hosts.size() + 1];
			nodes[0] = new Button.ParentBuilder(getAuthority(), new String[] { "add" })
						.setName("Add Host")
						.setHasButtonSets(false)
						.build();
			for(int i = 0; i < hosts.size(); i++) {
				Host host = hosts.get(i);
				nodes[i + 1] = new Parent.ParentBuilder(getAuthority(), new String[] { Integer.toString(host.id) })
					.setName(host.name)
					.setHasButtonSets(true)
					.setLevelName(host.name)
					.setDescription("Control devices on " + host.addr)
					.build();
			}
			return nodes;
		} else if(isAddButton(parent)) {
			addHost();
		} else if(parent.getPath().length > 0){
			ButtonBuilder builder = new Button.ButtonBuilder(getAuthority(), parent.getPath());
			builder.setLevelName("Buttons");
			Parent[] buttons = new Parent[] {
					createButton(parent, "REW", ButtonCodes.REMOTE_REVERSE, ButtonIds.BUTTON_REW),
					createButton(parent, "PLAY", ButtonCodes.REMOTE_PLAY, ButtonIds.BUTTON_PLAY),
					createButton(parent, "FFWD", ButtonCodes.REMOTE_FORWARD, ButtonIds.BUTTON_FFWD),
					createButton(parent, "PREVIOUS", ButtonCodes.REMOTE_SKIP_MINUS, ButtonIds.BUTTON_PREVIOUS),
					createButton(parent, "STOP", ButtonCodes.REMOTE_STOP, ButtonIds.BUTTON_STOP),
					createButton(parent, "PAUSE", ButtonCodes.REMOTE_PAUSE, ButtonIds.BUTTON_PAUSE),
					createButton(parent, "NEXT", ButtonCodes.REMOTE_SKIP_PLUS, ButtonIds.BUTTON_NEXT),
					createButton(parent, "UP", ButtonCodes.REMOTE_UP, ButtonIds.BUTTON_UP),
					createButton(parent, "DOWN", ButtonCodes.REMOTE_DOWN, ButtonIds.BUTTON_DOWN),
					createButton(parent, "LEFT", ButtonCodes.REMOTE_LEFT, ButtonIds.BUTTON_LEFT),
					createButton(parent, "RIGHT", ButtonCodes.REMOTE_RIGHT, ButtonIds.BUTTON_RIGHT),
					createButton(parent, "BACK", ButtonCodes.REMOTE_BACK, ButtonIds.BUTTON_BACK),
					createButton(parent, "OK", ButtonCodes.REMOTE_SELECT, ButtonIds.BUTTON_OK),
					createButton(parent, "MENU", ButtonCodes.REMOTE_MENU, ButtonIds.BUTTON_MENU),
					createButton(parent, "0", ButtonCodes.REMOTE_0, ButtonIds.BUTTON_0),
					createButton(parent, "1", ButtonCodes.REMOTE_1, ButtonIds.BUTTON_1),
					createButton(parent, "2", ButtonCodes.REMOTE_2, ButtonIds.BUTTON_2),
					createButton(parent, "3", ButtonCodes.REMOTE_3, ButtonIds.BUTTON_3),
					createButton(parent, "4", ButtonCodes.REMOTE_4, ButtonIds.BUTTON_4),
					createButton(parent, "5", ButtonCodes.REMOTE_5, ButtonIds.BUTTON_5),
					createButton(parent, "6", ButtonCodes.REMOTE_6, ButtonIds.BUTTON_6),
					createButton(parent, "7", ButtonCodes.REMOTE_7, ButtonIds.BUTTON_7),
					createButton(parent, "8", ButtonCodes.REMOTE_8, ButtonIds.BUTTON_8),
					createButton(parent, "9", ButtonCodes.REMOTE_9, ButtonIds.BUTTON_9),
					createButton(parent, "PGUP", ButtonCodes.REMOTE_PAGE_PLUS, ButtonIds.BUTTON_PAGE_UP),
					createButton(parent, "PGDN", ButtonCodes.REMOTE_PAGE_MINUS, ButtonIds.BUTTON_PAGE_DOWN),
					createButton(parent, "CH+", ButtonCodes.REMOTE_CHANNEL_PLUS, ButtonIds.BUTTON_CH_UP),
					createButton(parent, "CH-", ButtonCodes.REMOTE_CHANNEL_MINUS, ButtonIds.BUTTON_CH_DOWN),
					createButton(parent, "VOL+", ButtonCodes.REMOTE_VOLUME_PLUS, ButtonIds.BUTTON_VOLUME_UP),
					createButton(parent, "VOL-", ButtonCodes.REMOTE_VOLUME_MINUS, ButtonIds.BUTTON_VOLUME_DOWN),
					createButton(parent, "RECORD", ButtonCodes.REMOTE_RECORD, ButtonIds.BUTTON_RECORD),
					createButton(parent, "POWER", ButtonCodes.REMOTE_POWER, ButtonIds.BUTTON_POWER_TOGGLE),
					createButton(parent, "GUIDE", ButtonCodes.REMOTE_GUIDE),
					createButton(parent, "DISPLAY", ButtonCodes.REMOTE_DISPLAY),
					createButton(parent, "TITLE", ButtonCodes.REMOTE_TITLE),
					createButton(parent, "INFO", ButtonCodes.REMOTE_INFO),
					createButton(parent, "VIDEO", ButtonCodes.REMOTE_MY_VIDEOS),
					createButton(parent, "MUSIC", ButtonCodes.REMOTE_MY_MUSIC),
					createButton(parent, "IMAGES", ButtonCodes.REMOTE_MY_PICTURES),
					createButton(parent, "TV", ButtonCodes.REMOTE_MY_TV),
					createButton(parent, "LIVE TV", ButtonCodes.REMOTE_LIVE_TV),
			};
			return buttons;
		}
		return new Parent[0];
	}

	private Parent createButton(Parent parent, String name, String code, int buttonIdentifier) {
		ParentBuilder builder = new Button.ButtonBuilder(getAuthority(), 
				new String[] { parent.getPath()[0], code })					
			.setButtonIdentifier(buttonIdentifier)
			.setName(name)
			.setLevelName("Buttons");
		return builder.build();
	}

	private Parent createButton(Parent parent, String name, String code) {
		ParentBuilder builder = new Button.ButtonBuilder(getAuthority(), 
				new String[] { parent.getPath()[0], code })
			.setName(name)
			.setLevelName("Buttons");
		return builder.build();
	}

	@Override
	public Parent getDetails(Parent parent) {
		String function = parent.getPath()[parent.getPath().length - 1];
		Log.d("AARON", "Function: " + function);
		
		return new ButtonBuilder(getAuthority(), new String[] { parent.getPath()[0], function })
			.setButtonIdentifier(ButtonIdentifier.getKnownButton(function))
			.setName(function)
			.setLevelName("Buttons")
			.build();
	}

	@Override
	public Button[] sendButtons(Button[] buttons) {
		if(isAddButton(buttons[0])) {
			addHost();
		} else {
			for(Button button : buttons) {
				String function = button.getPath()[button.getPath().length - 1];
				int id = Integer.parseInt(button.getPath()[0]);
				Log.d("AARON", "Sending function: " + function);
				if(null == mActiveHost || id != mActiveHost.id) {
					mActiveHost = HostFactory.readHost(getContext(), id);
					if(null != mActiveHost) {
						if(null == mControlClient) {
							try {
								Log.d("AARON", "Initializing control client");
								mControlClient = ClientFactory.getControlClient(new INotifiableManager() {
									
									public void retryAll() {
										Log.d(TAG, "retryAll()");
									}
									
									public void onWrongConnectionState(int state, Command<?> cmd) {

										Log.d(TAG, "onWrongConnectionState( " + state + "," + cmd +")");
									}
									
									public void onMessage(int code, String message) {
										Log.d(TAG, message);
									}
									
									public void onMessage(String message) {
										Log.d(TAG, message);
									}
									
									public void onFinish(DataResponse<?> response) {
										Log.d(TAG, "onFinish(" + response + ")");
									}
									
									public void onError(Exception e) {
										Log.d(TAG, e.getMessage(), e);
									}
								}, getContext());
							} catch (WifiStateException e) {
								Log.d(TAG, e.getMessage(), e);
								Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
							}
						}
						Log.d("AARON", "Resetting client...");
						ClientFactory.resetClient(mActiveHost);
						Log.d("AARON", "Client set, getting event client manager.");
						mEventClientManager = ManagerFactory.getEventClientManager(mNotifiableManager);
					}
				}
				if(null != mEventClientManager) {
					mEventClientManager.sendButton("R1", function, false, true, true, (short)0, (byte)0);
				}
			}
		}
		return new Button[0];
	}

	private void addHost() {
		mHandler.post(new Runnable() {
			
			public void run() {
				Toast.makeText(getContext(), "Adding host", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(getContext(), AddHostActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getContext().startActivity(intent);
			}
		});
	}

	private boolean isAddButton(Parent button) {
		return "add".equals(button.getPath()[0]);
	}

	@Override
	public String getDescription(Parent node) {
		return "XBMC Hosts";
	}

	public void onWrongConnectionState(int state, INotifiableManager manager,
			Command<?> source) {
		// TODO Auto-generated method stub
		
	}

	public void onMessage(final String message) {
		runOnUI(new Runnable() {
			public void run() {
				Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
			}
		});
	}

	public void runOnUI(Runnable action) {
		mHandler.post(action);
	}
}
