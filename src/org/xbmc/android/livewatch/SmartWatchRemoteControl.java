/*
 Copyright (c) 2011, Sony Ericsson Mobile Communications AB

 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.

 * Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.

 * Neither the name of the Sony Ericsson Mobile Communications AB nor the names
 of its contributors may be used to endorse or promote products derived from
 this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.xbmc.android.livewatch;

import com.sonyericsson.extras.liveware.extension.util.view.ControlExtensionViewGroup;

import org.xbmc.android.remote.R;
import org.xbmc.android.remote.presentation.activity.ConfigurationManager;
import org.xbmc.android.remote.presentation.controller.RemoteController;
import org.xbmc.eventclient.ButtonCodes;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

/**
 * The sample control for SmartWatch handles the control on the accessory.
 * This class exists in one instance for every supported host application that
 * we have registered to
 */
class SmartWatchRemoteControl extends ControlExtensionViewGroup {

	private ConfigurationManager mConfigurationManager;
	private RemoteController mRemoteController;

	/**
     * Create sample control.
     *
     * @param hostAppPackageName Package name of host application.
     * @param context The context.
     * @param handler The handler to use
     */
    SmartWatchRemoteControl(final String hostAppPackageName, int deviceType, final Context context,
            Handler handler) {
        super(context, deviceType, hostAppPackageName);
		mRemoteController = new RemoteController(mContext.getApplicationContext());
		mConfigurationManager = ConfigurationManager.getInstance(mContext);
    }

    @Override
    public void onDestroy() {
        // Stop sensor
    };
    
    @Override
    public void onCreate() {
    	// TODO Auto-generated method stub
    	super.onCreate();

        setContentView(R.layout.watch_layout);
    }

    @Override
    public void onStart() {
        // Nothing to do. Animation is handled in onResume.
    }

    @Override
    public void onStop() {
        // Nothing to do. Animation is handled in onPause.
    }

    @Override
    public void onResume() {
        Log.d(SmartExtensionService.LOG_TAG, "Starting control");
    }

	@Override
    public void onPause() {
    }


	/**
	 * Assigns the button events to the views.
	 */
	private void setupButtons() {

		// display
		mRemoteController.setupButton(findViewById(R.id.RemoteXboxImgBtnDisplay),ButtonCodes.REMOTE_DISPLAY);

		mRemoteController.setupButton(findViewById(R.id.RemoteXboxImgBtnVideo), ButtonCodes.REMOTE_MY_VIDEOS);
		mRemoteController.setupButton(findViewById(R.id.RemoteXboxImgBtnMusic), ButtonCodes.REMOTE_MY_MUSIC);
		mRemoteController.setupButton(findViewById(R.id.RemoteXboxImgBtnImages), ButtonCodes.REMOTE_MY_PICTURES);
		mRemoteController.setupButton(findViewById(R.id.RemoteXboxImgBtnTv), ButtonCodes.REMOTE_MY_TV);

		// seek back
		mRemoteController.setupButton(findViewById(R.id.RemoteXboxImgBtnSeekBack), ButtonCodes.REMOTE_REVERSE);
		// play
		mRemoteController.setupButton(findViewById(R.id.RemoteXboxImgBtnPlay), ButtonCodes.REMOTE_PLAY);
		// seek forward
		mRemoteController.setupButton(findViewById(R.id.RemoteXboxImgBtnSeekForward), ButtonCodes.REMOTE_FORWARD);

		// previous
		mRemoteController.setupButton(findViewById(R.id.RemoteXboxImgBtnPrevious), ButtonCodes.REMOTE_SKIP_MINUS);
		// stop
		mRemoteController.setupButton(findViewById(R.id.RemoteXboxImgBtnStop), ButtonCodes.REMOTE_STOP);
		// pause
		mRemoteController.setupButton(findViewById(R.id.RemoteXboxImgBtnPause), ButtonCodes.REMOTE_PAUSE);
		// next
		mRemoteController.setupButton(findViewById(R.id.RemoteXboxImgBtnNext), ButtonCodes.REMOTE_SKIP_PLUS);

		// title
		mRemoteController.setupButton(findViewById(R.id.RemoteXboxImgBtnTitle), ButtonCodes.REMOTE_TITLE);
		// up
		mRemoteController.setupButton(findViewById(R.id.RemoteXboxImgBtnUp), ButtonCodes.REMOTE_UP);
		// info
		mRemoteController.setupButton(findViewById(R.id.RemoteXboxImgBtnInfo), ButtonCodes.REMOTE_INFO);

		// left
		mRemoteController.setupButton(findViewById(R.id.RemoteXboxImgBtnLeft), ButtonCodes.REMOTE_LEFT);
		// select
		mRemoteController.setupButton(findViewById(R.id.RemoteXboxImgBtnSelect), ButtonCodes.REMOTE_SELECT);
		// right
		mRemoteController.setupButton(findViewById(R.id.RemoteXboxImgBtnRight), ButtonCodes.REMOTE_RIGHT);

		// menu
		mRemoteController.setupButton(findViewById(R.id.RemoteXboxImgBtnMenu), ButtonCodes.REMOTE_MENU);
		// down
		mRemoteController.setupButton(findViewById(R.id.RemoteXboxImgBtnDown), ButtonCodes.REMOTE_DOWN);
		// back
		mRemoteController.setupButton(findViewById(R.id.RemoteXboxImgBtnBack), ButtonCodes.REMOTE_BACK);

		// videos
		mRemoteController.setupButton(findViewById(R.id.RemoteXboxImgBtnVideo), ButtonCodes.REMOTE_MY_VIDEOS);
		// music
		mRemoteController.setupButton(findViewById(R.id.RemoteXboxImgBtnMusic), ButtonCodes.REMOTE_MY_MUSIC);
		// pictures
		mRemoteController.setupButton(findViewById(R.id.RemoteXboxImgBtnImages), ButtonCodes.REMOTE_MY_PICTURES);
		// tv
		mRemoteController.setupButton(findViewById(R.id.RemoteXboxImgBtnTv), ButtonCodes.REMOTE_MY_TV);
		// settings
		mRemoteController.setupButton(findViewById(R.id.RemoteXboxImgBtnPower), ButtonCodes.REMOTE_POWER);
	}
}
