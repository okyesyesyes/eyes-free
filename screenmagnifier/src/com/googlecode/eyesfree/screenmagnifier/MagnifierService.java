/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.googlecode.eyesfree.screenmagnifier;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

import com.googlecode.eyesfree.screenmagnifier.ToggleOverlay.ToggleListener;
import com.googlecode.eyesfree.utils.FeedbackController;
import com.googlecode.eyesfree.utils.ScreenshotUtil;
import com.googlecode.eyesfree.widget.SimpleOverlay;
import com.googlecode.eyesfree.widget.SimpleOverlay.SimpleOverlayListener;

public class MagnifierService extends AccessibilityService {
    private static final int SHOW_MAGNIFIER = 1;

    private FeedbackController mFeedbackController;
    private MagnifierOverlay mMagnifierOverlay;
    private ToggleOverlay mToggleOverlay;

    @Override
    public void onCreate() {
        super.onCreate();

        if (!ScreenshotUtil.hasScreenshotPermission(this)) {
            showNoPermissionDialog();
            return;
        }

        mFeedbackController = new FeedbackController(this);

        mMagnifierOverlay = new MagnifierOverlay(this);
        mMagnifierOverlay.setListener(mMagnifierOverlayListener);

        mToggleOverlay = new ToggleOverlay(this);
        mToggleOverlay.setToggleListener(mToggleOverlayListener);

        mToggleOverlay.show();

        // We have to manually listen for when to stop the service.
        final Uri uri = Settings.Secure.getUriFor(Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        getContentResolver().registerContentObserver(uri, false, mContentObserver);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // Do nothing.
    }

    @Override
    public void onInterrupt() {
        // Do nothing.
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        shutdown();

        getContentResolver().unregisterContentObserver(mContentObserver);
    }

    /**
     * Shuts down the feedback controller. Hides and removes all overlays.
     */
    private void shutdown() {
        if (mMagnifierOverlay != null) {
            mMagnifierOverlay.hide();
            mMagnifierOverlay = null;
        }

        if (mToggleOverlay != null) {
            mToggleOverlay.hide();
            mToggleOverlay = null;
        }

        if (mFeedbackController != null) {
            mFeedbackController.shutdown();
            mFeedbackController = null;
        }
    }

    @Override
    protected void onServiceConnected() {
        final AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = 0; // We don't want any events!
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_VISUAL;

        setServiceInfo(info);
    }

    /**
     * Shows the "Device not supported" dialog, which is an easier to understand
     * way of saying "TalkBack must be installed on the system partition to
     * obtain the required permissions."
     */
    private void showNoPermissionDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this).setTitle(
                R.string.magnifier_name).setMessage(R.string.device_not_supported)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(R.string.manage_services, mOnClickListener).create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_MAGNIFIER:
                    mMagnifierOverlay.show();
                    break;
            }
        }
    };

    private final SimpleOverlayListener mMagnifierOverlayListener = new SimpleOverlayListener() {
        @Override
        public void onShow(SimpleOverlay overlay) {
            mFeedbackController.playSound(R.raw.chime_up);
        }

        @Override
        public void onHide(SimpleOverlay overlay) {
            mFeedbackController.playSound(R.raw.chime_down);
            mToggleOverlay.setState(true);
        }
    };

    private final OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            final Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        }
    };

    private final ToggleListener mToggleOverlayListener = new ToggleListener() {
        @Override
        public void onToggled(boolean state) {
            if (!state) {
                mHandler.sendEmptyMessage(SHOW_MAGNIFIER);
            }
        }
    };

    private final ContentObserver mContentObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            final ContentResolver resolver = getContentResolver();
            final String enabledServices = Settings.Secure.getString(
                    resolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);

            if (!enabledServices.contains(MagnifierService.class.getName())) {
                shutdown();
                stopSelf();
            }
        }
    };
}
