package com.skillslevel.joules.utils;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.KeyEvent;

import com.google.gson.Gson;
import com.skillslevel.joules.MusicService;
import com.skillslevel.joules.activities.MainActivity;

import static com.skillslevel.joules.BuildConfig.DEBUG;

public class MediaButtonService extends JobService {
    private static final String TAG = "ButtonIntentReceiver";
    private static final int MSG_LONGPRESS_TIMEOUT = 1;
    private static final int MSG_HEADSET_DOUBLE_CLICK_TIMEOUT = 2;
    private static final int LONG_PRESS_DELAY = 1000;
    private static final int DOUBLE_CLICK = 800;
    private static boolean mLaunched = false;
    private static int mClickCounter = 0;
    private static long mLastClickTime = 0;
    private static boolean mDown = false;
    @SuppressLint("HandlerLeak")
    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                case MSG_LONGPRESS_TIMEOUT:
                    if (DEBUG) Log.v(TAG, "Handling longpress timeout, launched " + mLaunched);
                    if (!mLaunched) {
                        final Context context = (Context) msg.obj;
                        final Intent i = new Intent();
                        i.setClass(context, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(i);
                        mLaunched = true;
                    }
                    break;

                case MSG_HEADSET_DOUBLE_CLICK_TIMEOUT:
                    final int clickCount = msg.arg1;
                    final String command;

                    if (DEBUG) Log.v(TAG, "Handling headset click, count = " + clickCount);
                    switch (clickCount) {
                        case 1:
                            command = MusicService.CMDTOGGLEPAUSE;
                            break;
                        case 2:
                            command = MusicService.CMDNEXT;
                            break;
                        case 3:
                            command = MusicService.CMDPREVIOUS;
                            break;
                        default:
                            command = null;
                            break;
                    }
            }
        }
    };
    Bundle bundle;
    PersistableBundle persistableBundle;
    private String action;
    private String otherAction;

    private static void releaseWakeLockIfHandlerIdle() {
        if (mHandler.hasMessages(MSG_LONGPRESS_TIMEOUT)
                || mHandler.hasMessages(MSG_HEADSET_DOUBLE_CLICK_TIMEOUT)) {
            if (DEBUG) Log.v(TAG, "Handler still has messages pending, not releasing wake lock");
            return;
        }
    }

    private static void startService(Context context, String command) {
        final Intent i = new Intent(context, MusicService.class);
        i.setAction(MusicService.SERVICECMD);
        i.putExtra(MusicService.CMDNAME, command);
        i.putExtra(MusicService.FROM_MEDIA_BUTTON, true);
        context.startService(i);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Intent intentAction = null;

        String json = params.getExtras().getString("intentObject");
        Gson g = new Gson();
        intentAction = g.fromJson(json, Intent.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            bundle = params.getTransientExtras();
            intentAction = bundle.getParcelable(action);
        }

        if (intentAction != null) {
            String action = intentAction.getAction();
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(action)
            ) {
                if (PreferencesUtil.getInstance(this).pauseEnabledOnDetach())
                    startService(this, MusicService.CMDPAUSE);
            } else if (Intent.ACTION_MEDIA_BUTTON.equals(action)) {
                final KeyEvent event = intentAction.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
                if (event == null) {
                    return true;
                }
                final int keycode = event.getKeyCode();
                final int eventAction = event.getAction();
                final long eventtime = event.getEventTime();

                String command = null;
                switch (keycode) {
                    case KeyEvent.KEYCODE_MEDIA_STOP:
                        command = MusicService.CMDSTOP;
                        break;
                    case KeyEvent.KEYCODE_HEADSETHOOK:
                    case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                        command = MusicService.CMDTOGGLEPAUSE;
                        break;
                    case KeyEvent.KEYCODE_MEDIA_NEXT:
                        command = MusicService.CMDNEXT;
                        break;
                    case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                        command = MusicService.CMDPREVIOUS;
                        break;
                    case KeyEvent.KEYCODE_MEDIA_PAUSE:
                        command = MusicService.CMDPAUSE;
                        break;
                    case KeyEvent.KEYCODE_MEDIA_PLAY:
                        command = MusicService.CMDPLAY;
                        break;
                }

                if (command != null) {
                    if (eventAction == KeyEvent.ACTION_DOWN) {
                        if (mDown) {
                            if (MusicService.CMDTOGGLEPAUSE.equals(command)
                                    || MusicService.CMDPLAY.equals(command)) {
                                if (mLastClickTime != 0
                                        && eventtime - mLastClickTime > LONG_PRESS_DELAY) {
                                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_LONGPRESS_TIMEOUT, this), 0);
                                }
                            }
                        } else if (event.getRepeatCount() == 0) {

                            if (keycode == KeyEvent.KEYCODE_HEADSETHOOK) {
                                if (eventtime - mLastClickTime >= DOUBLE_CLICK) {
                                    mClickCounter = 0;
                                }

                                mClickCounter++;
                                if (DEBUG)
                                    Log.v(TAG, "Got headset click, count = " + mClickCounter);
                                mHandler.removeMessages(MSG_HEADSET_DOUBLE_CLICK_TIMEOUT);

                                Message msg = mHandler.obtainMessage(
                                        MSG_HEADSET_DOUBLE_CLICK_TIMEOUT, mClickCounter, 0, this);

                                long delay = mClickCounter < 3 ? DOUBLE_CLICK : 0;
                                if (mClickCounter >= 3) {
                                    mClickCounter = 0;
                                }
                                mLastClickTime = eventtime;
                                mHandler.sendMessageDelayed(msg, delay);
                            } else {
                                startService(this, command);
                            }
                            mLaunched = false;
                            mDown = true;
                        }
                    } else {
                        mHandler.removeMessages(MSG_LONGPRESS_TIMEOUT);
                        mDown = false;
                    }
                    releaseWakeLockIfHandlerIdle();
                    return true;
                }
            }

        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}
