package com.example.gabriel.stettinerwatchfaceiliyanpopov;

import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.watchface.WatchFaceStyle;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by Gabriel on 24.11.2015 г..
 */
public class StettinerWatchFaceService extends CanvasWatchFaceService {
    private static final String TAG = "MegaDroidWatchSvc";
    private static final long INTERACTIVE_UPDATE_RATE_MS =
            TimeUnit.SECONDS.toMillis(1);
    private static final int MSG_UPDATE_TIME = 0;

    @Override
    public Engine onCreateEngine() {
// create and return the watch face engine
        return new MegaDroidEngine(this);
    }

    /* implement service callback methods */
    private class MegaDroidEngine extends CanvasWatchFaceService.Engine {
        private boolean mAmbient;
        private boolean lowBitAmbient;

        private final Service service;

        Bitmap backgroundScaledBitmap;
        Bitmap backgroundGrayScaledBitmap;

        private Bitmap backgroundBitmap;
        private Bitmap mGrayBackgroundBitmap;
        private Bitmap backgroundBitmapMonday;
        private Bitmap mGrayBackgroundBitmapMonday;
        private Bitmap backgroundBitmapTuesday;
        private Bitmap mGrayBackgroundBitmapTuesday;
        private Bitmap backgroundBitmapWednesday;
        private Bitmap mGrayBackgroundBitmapWednesday;
        private Bitmap backgroundBitmapThursday;
        private Bitmap mGrayBackgroundBitmapThursday;
        private Bitmap backgroundBitmapFriday;
        private Bitmap mGrayBackgroundBitmapFriday;
        private Bitmap backgroundBitmapSaturday;
        private Bitmap mGrayBackgroundBitmapSaturday;
        private Bitmap backgroundBitmapSunday;
        private Bitmap mGrayBackgroundBitmapSunday;

        private Paint mMinutePaint;
        private Paint mHourPaint;
        private Paint mSecondPaint;
        Paint mTickPaint;

        private Time time;
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);


        public MegaDroidEngine(Service service) {
            this.service = service;
        }

        /**
         * initialize your watch face
         */
        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);
            setWatchFaceStyle(new WatchFaceStyle.Builder(service)
                    .setCardPeekMode(WatchFaceStyle.PEEK_MODE_SHORT)
                    .setStatusBarGravity(Gravity.RIGHT | Gravity.TOP)
                    .setHotwordIndicatorGravity(Gravity.LEFT | Gravity.TOP)
                    .setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
                    .setShowSystemUiTime(false)
                    .build());
            Resources resources = service.getResources();


            //test for changing the background in different days of week

            Drawable backgroundDrawableMonday = resources.getDrawable(R.drawable.bgmonday);
            this.backgroundBitmapMonday = ((BitmapDrawable) backgroundDrawableMonday).getBitmap();
            this.mGrayBackgroundBitmapMonday = ((BitmapDrawable) resources.getDrawable(
                    R.drawable.bgmonday_gray)).getBitmap();
            Drawable backgroundDrawableTuesday = resources.getDrawable(R.drawable.bgtuesday);
            this.backgroundBitmapTuesday = ((BitmapDrawable) backgroundDrawableTuesday).getBitmap();
            this.mGrayBackgroundBitmapTuesday = ((BitmapDrawable) resources.getDrawable(
                    R.drawable.bgtuesday_gray)).getBitmap();
            Drawable backgroundDrawableWednesday = resources.getDrawable(R.drawable.bgwednesday);
            this.backgroundBitmapWednesday = ((BitmapDrawable) backgroundDrawableWednesday).getBitmap();
            this.mGrayBackgroundBitmapWednesday = ((BitmapDrawable) resources.getDrawable(
                    R.drawable.bgwednesday_gray)).getBitmap();
            Drawable backgroundDrawableThursday = resources.getDrawable(R.drawable.bgthursday);
            this.backgroundBitmapThursday = ((BitmapDrawable) backgroundDrawableThursday).getBitmap();
            this.mGrayBackgroundBitmapThursday = ((BitmapDrawable) resources.getDrawable(
                    R.drawable.bgthursday_gray)).getBitmap();
            Drawable backgroundDrawableFriday = resources.getDrawable(R.drawable.bgfriday);
            this.backgroundBitmapFriday = ((BitmapDrawable) backgroundDrawableFriday).getBitmap();
            this.mGrayBackgroundBitmapFriday = ((BitmapDrawable) resources.getDrawable(
                    R.drawable.bgfriday_gray)).getBitmap();
            Drawable backgroundDrawableSaturday = resources.getDrawable(R.drawable.bgsaturday);
            this.backgroundBitmapSaturday = ((BitmapDrawable) backgroundDrawableSaturday).getBitmap();
            this.mGrayBackgroundBitmapSaturday = ((BitmapDrawable) resources.getDrawable(
                    R.drawable.bgsaturday_gray)).getBitmap();
            Drawable backgroundDrawableSunday = resources.getDrawable(R.drawable.bgsunday);
            this.backgroundBitmapSunday = ((BitmapDrawable) backgroundDrawableSunday).getBitmap();
            this.mGrayBackgroundBitmapSunday = ((BitmapDrawable) resources.getDrawable(
                    R.drawable.bgsunday_gray)).getBitmap();

            // test ends here
            mHourPaint = new Paint();
            mHourPaint.setAntiAlias(true);
            mHourPaint.setStrokeCap(Paint.Cap.ROUND);
            mHourPaint.setARGB(255, 255, 255, 255);
            mHourPaint.setAntiAlias(true);
            mMinutePaint = new Paint();
            mMinutePaint.setAntiAlias(true);
            mMinutePaint.setStrokeCap(Paint.Cap.ROUND);
            mMinutePaint.setARGB(255, 255, 255, 255);

            mSecondPaint = new Paint();
            mSecondPaint.setARGB(255, 255, 0, 0);
            mSecondPaint.setStrokeCap(Paint.Cap.ROUND);
            mSecondPaint.setStrokeWidth(2.f);
            mSecondPaint.setAntiAlias(true);

            this.time = new Time();
            this.time.switchTimezone("Europe/Sofia");
        }

        /**
         * called when system properties are changed
         * use this to capture low-bit ambient.
         */
        @Override
        public void onPropertiesChanged(Bundle properties) {
            super.onPropertiesChanged(properties);
            this.lowBitAmbient = properties.getBoolean(
                    PROPERTY_LOW_BIT_AMBIENT, false);
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "onPropertiesChanged: low-bit ambient = " + lowBitAmbient);
            }
        }


        /**
         * This is called by the runtime on every minute tick
         */
        @Override
        public void onTimeTick() {
            super.onTimeTick();
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "onTimeTick: ambient = " + isInAmbientMode());
            }
            invalidate();
        }

        /**
         * Called when there's a switched in/out of ambient mode
         */
        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "onAmbientModeChanged: " + inAmbientMode);
            }
            // next line is important,
            mAmbient = inAmbientMode;
//            if (mAmbient) {
//                //initGrayBackgroundBitmap();
//                mGrayBackgroundBitmap = ((BitmapDrawable) service.getResources().getDrawable(
//                   R.drawable.bg_gray)).getBitmap();
//                hourHand = ((BitmapDrawable) service.getResources().getDrawable(
//                        R.drawable.hour_hand_bw)).getBitmap();
//                minuteHand = ((BitmapDrawable) service.getResources()
//                        .getDrawable(R.drawable.minute_hand_bw)).getBitmap();
//            } else {
//                backgroundBitmap = ((BitmapDrawable) service.getResources().getDrawable(
//                        R.drawable.bg)).getBitmap();
//                hourHand = ((BitmapDrawable) service.getResources()
//                        .getDrawable(R.drawable.hour_hand)).getBitmap();
//                minuteHand = ((BitmapDrawable) service.getResources()
//                        .getDrawable(R.drawable.minute_hand)).getBitmap();
//            }
            if (lowBitAmbient) {
                boolean antiAlias = !inAmbientMode;
                mSecondPaint.setAntiAlias(antiAlias);
            }
            invalidate();
            // Whether the timer should be running depends on whether
            //we're in ambient mode (as well
            // as whether we're visible), so we may need to start
            //or stop the timer.
            updateTimer();

        }


        private void updateTimer() {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "updateTimer");
            }
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            if (shouldTimerBeRunning()) {
                mUpdateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
            }
        }


        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            //Draw the watch face here

            time.setToNow();
            //chaning to my timeZone
            time.switchTimezone("Europe/Sofia");
            int width = bounds.width();
            int height = bounds.height();
            // Draw the background, scaled to fit.

            switch (day) {
                case Calendar.MONDAY:
                    if (backgroundScaledBitmap == null
                            || backgroundScaledBitmap.getWidth() != width
                            || backgroundScaledBitmap.getHeight() != height) {
                        backgroundScaledBitmap = Bitmap.createScaledBitmap(backgroundBitmapMonday,
                                width, height, true /* filter */);
                    }
                    canvas.drawBitmap(backgroundScaledBitmap, 0, 0, null);

                    if (backgroundGrayScaledBitmap == null
                            || backgroundGrayScaledBitmap.getWidth() != width
                            || backgroundGrayScaledBitmap.getHeight() != height) {
                        backgroundGrayScaledBitmap = Bitmap.createScaledBitmap(mGrayBackgroundBitmapMonday,
                                width, height, true /* filter */);
                    }

                    if (mAmbient) {
                        canvas.drawBitmap(backgroundGrayScaledBitmap, 0, 0, null);
                    }

                case Calendar.TUESDAY:
                    if (backgroundScaledBitmap == null
                            || backgroundScaledBitmap.getWidth() != width
                            || backgroundScaledBitmap.getHeight() != height) {
                        backgroundScaledBitmap = Bitmap.createScaledBitmap(backgroundBitmapTuesday,
                                width, height, true /* filter */);
                    }
                    canvas.drawBitmap(backgroundScaledBitmap, 0, 0, null);

                    if (backgroundGrayScaledBitmap == null
                            || backgroundGrayScaledBitmap.getWidth() != width
                            || backgroundGrayScaledBitmap.getHeight() != height) {
                        backgroundGrayScaledBitmap = Bitmap.createScaledBitmap(mGrayBackgroundBitmapTuesday,
                                width, height, true /* filter */);
                    }

                    if (mAmbient) {
                        canvas.drawBitmap(backgroundGrayScaledBitmap, 0, 0, null);
                    }

                case Calendar.WEDNESDAY:
                    if (backgroundScaledBitmap == null
                            || backgroundScaledBitmap.getWidth() != width
                            || backgroundScaledBitmap.getHeight() != height) {
                        backgroundScaledBitmap = Bitmap.createScaledBitmap(backgroundBitmapWednesday,
                                width, height, true /* filter */);
                    }
                    canvas.drawBitmap(backgroundScaledBitmap, 0, 0, null);

                    if (backgroundGrayScaledBitmap == null
                            || backgroundGrayScaledBitmap.getWidth() != width
                            || backgroundGrayScaledBitmap.getHeight() != height) {
                        backgroundGrayScaledBitmap = Bitmap.createScaledBitmap(mGrayBackgroundBitmapWednesday,
                                width, height, true /* filter */);
                    }

                    if (mAmbient) {
                        canvas.drawBitmap(backgroundGrayScaledBitmap, 0, 0, null);
                    }

                case Calendar.THURSDAY:
                    if (backgroundScaledBitmap == null
                            || backgroundScaledBitmap.getWidth() != width
                            || backgroundScaledBitmap.getHeight() != height) {
                        backgroundScaledBitmap = Bitmap.createScaledBitmap(backgroundBitmapThursday,
                                width, height, true /* filter */);
                    }
                    canvas.drawBitmap(backgroundScaledBitmap, 0, 0, null);

                    if (backgroundGrayScaledBitmap == null
                            || backgroundGrayScaledBitmap.getWidth() != width
                            || backgroundGrayScaledBitmap.getHeight() != height) {
                        backgroundGrayScaledBitmap = Bitmap.createScaledBitmap(mGrayBackgroundBitmapThursday,
                                width, height, true /* filter */);
                    }

                    if (mAmbient) {
                        canvas.drawBitmap(backgroundGrayScaledBitmap, 0, 0, null);
                    }

                case Calendar.FRIDAY:
                    if (backgroundScaledBitmap == null
                            || backgroundScaledBitmap.getWidth() != width
                            || backgroundScaledBitmap.getHeight() != height) {
                        backgroundScaledBitmap = Bitmap.createScaledBitmap(backgroundBitmapFriday,
                                width, height, true /* filter */);
                    }
                    canvas.drawBitmap(backgroundScaledBitmap, 0, 0, null);

                    if (backgroundGrayScaledBitmap == null
                            || backgroundGrayScaledBitmap.getWidth() != width
                            || backgroundGrayScaledBitmap.getHeight() != height) {
                        backgroundGrayScaledBitmap = Bitmap.createScaledBitmap(mGrayBackgroundBitmapFriday,
                                width, height, true /* filter */);
                    }

                    if (mAmbient) {
                        canvas.drawBitmap(backgroundGrayScaledBitmap, 0, 0, null);
                    }

                case Calendar.SATURDAY:
                    if (backgroundScaledBitmap == null
                            || backgroundScaledBitmap.getWidth() != width
                            || backgroundScaledBitmap.getHeight() != height) {
                        backgroundScaledBitmap = Bitmap.createScaledBitmap(backgroundBitmapSaturday,
                                width, height, true /* filter */);
                    }
                    canvas.drawBitmap(backgroundScaledBitmap, 0, 0, null);

                    if (backgroundGrayScaledBitmap == null
                            || backgroundGrayScaledBitmap.getWidth() != width
                            || backgroundGrayScaledBitmap.getHeight() != height) {
                        backgroundGrayScaledBitmap = Bitmap.createScaledBitmap(mGrayBackgroundBitmapSaturday,
                                width, height, true /* filter */);
                    }

                    if (mAmbient) {
                        canvas.drawBitmap(backgroundGrayScaledBitmap, 0, 0, null);
                    }

                case Calendar.SUNDAY:
                    if (backgroundScaledBitmap == null
                            || backgroundScaledBitmap.getWidth() != width
                            || backgroundScaledBitmap.getHeight() != height) {
                        backgroundScaledBitmap = Bitmap.createScaledBitmap(backgroundBitmapSunday,
                                width, height, true /* filter */);
                    }
                    canvas.drawBitmap(backgroundScaledBitmap, 0, 0, null);

                    if (backgroundGrayScaledBitmap == null
                            || backgroundGrayScaledBitmap.getWidth() != width
                            || backgroundGrayScaledBitmap.getHeight() != height) {
                        backgroundGrayScaledBitmap = Bitmap.createScaledBitmap(mGrayBackgroundBitmapSunday,
                                width, height, true /* filter */);
                    }

                    if (mAmbient) {
                        canvas.drawBitmap(backgroundGrayScaledBitmap, 0, 0, null);
                    }
                    // etc ...
            }

//            if (backgroundScaledBitmap == null
//                    || backgroundScaledBitmap.getWidth() != width
//                    || backgroundScaledBitmap.getHeight() != height) {
//                backgroundScaledBitmap = Bitmap.createScaledBitmap(backgroundBitmap,
//                        width, height, true /* filter */);
//            }
//            canvas.drawBitmap(backgroundScaledBitmap, 0, 0, null);
//
//            if (backgroundGrayScaledBitmap == null
//                    || backgroundGrayScaledBitmap.getWidth() != width
//                    || backgroundGrayScaledBitmap.getHeight() != height) {
//                backgroundGrayScaledBitmap = Bitmap.createScaledBitmap(mGrayBackgroundBitmap,
//                        width, height, true /* filter */);
//            }
//
//            if (mAmbient) {
//                canvas.drawBitmap(backgroundGrayScaledBitmap, 0, 0, null);
//            }


            float secRot = time.second / 30f * (float) Math.PI;
            int minutes = time.minute;
            float minRot = minutes / 30f * (float) Math.PI;
            float hrRot = ((time.hour + (minutes / 60f)) / 6f) * (float) Math.PI;
            // Find the center. Ignore the window insets so that, on round
            //watches with a "chin", the watch face is centered on the
            //entire screen, not just the usable portion.
            float centerX = width / 2f;
            float centerY = height / 2f;
            Matrix matrix = new Matrix();
//            int minuteHandX = ((width - mMinutePaint.getWidth()) / 2)
//                    - (minuteHand.getWidth() / 2);
//            int minuteHandY = (height - mMinutePaint.getHeight()) / 2;
            // matrix.setTranslate(mMinuteHandX - 20, minuteHandY);
            //  float degrees = minRot * (float) (180.0 / Math.PI);
            // matrix.postRotate(degrees + 90, centerX, centerY);
            // canvas.drawBitmap(mMinutePaint, matrix, null);
            // matrix = new Matrix();
            //int rightArmX = ((width - hourHand.getWidth()) / 2)
            //         + (hourHand.getWidth() / 2);
            // int rightArmY = (height - hourHand.getHeight()) / 2;
            // matrix.setTranslate(rightArmX + 20, rightArmY);
            //  degrees = hrRot * (float) (180.0 / Math.PI);
            // matrix.postRotate(degrees - 90, centerX, centerY);
            // canvas.drawBitmap(hourHand, matrix, null);

            float hourLenght = centerX - 60;
            float minuteLenght = centerX - 40;
            float secLength = centerX - 20;



            if ( isInAmbientMode()  ){
                mHourPaint.setStrokeWidth(5.f);
                mMinutePaint.setStrokeWidth(5.f);
                mSecondPaint.setStrokeWidth(3.f);
                float hourX = (float) Math.sin(hrRot) * hourLenght;
                float hourY = (float) -Math.cos(hrRot) * hourLenght;
                canvas.drawLine(centerX, centerY, centerX + hourX,
                        centerY + hourY, mHourPaint);

                float minuteX = (float) Math.sin(minRot) * minuteLenght;
                float minuteY = (float) -Math.cos(minRot) * minuteLenght;
                canvas.drawLine(centerX, centerY, centerX + minuteX,
                        centerY + minuteY, mMinutePaint);
            }

            if (!isInAmbientMode()) {
                mHourPaint.setStrokeWidth(4.f);
                mMinutePaint.setStrokeWidth(4.f);
                mSecondPaint.setStrokeWidth(2.f);
                float hourX = (float) Math.sin(hrRot) * hourLenght;
                float hourY = (float) -Math.cos(hrRot) * hourLenght;
                canvas.drawLine(centerX, centerY, centerX + hourX,
                        centerY + hourY, mHourPaint);

                float minuteX = (float) Math.sin(minRot) * minuteLenght;
                float minuteY = (float) -Math.cos(minRot) * minuteLenght;
                canvas.drawLine(centerX, centerY, centerX + minuteX,
                        centerY + minuteY, mMinutePaint);

                float secX = (float) Math.sin(secRot) * secLength;
                float secY = (float) -Math.cos(secRot) * secLength;
                canvas.drawLine(centerX, centerY, centerX + secX,
                        centerY + secY, mSecondPaint);
            }


        }

        /**
         * Called when the watch face becomes visible or invisible
         */
        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "onVisibilityChanged: " + visible);
            }
            if (visible) {
                registerReceiver();
                // Update time zone in case it changed while we weren't visible.
                time.clear(TimeZone.getDefault().getID());
                time.setToNow();
            } else {
                unregisterReceiver();
            }
            // Whether the timer should be running depends on whether
            //we're visible (as well as
            // whether we're in ambient mode), so we may need to start
            //or stop the timer.
            updateTimer();
        }


        /**
         * Handler to update the time once a second in interactive mode.
         */
        final Handler mUpdateTimeHandler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                switch (message.what) {
                    case MSG_UPDATE_TIME:
                        if (Log.isLoggable(TAG, Log.VERBOSE)) {
                            Log.v(TAG, "updating time");
                        }
                        invalidate();
                        if (shouldTimerBeRunning()) {
                            long timeMs = System.currentTimeMillis();
                            long delayMs = INTERACTIVE_UPDATE_RATE_MS
                                    - (timeMs % INTERACTIVE_UPDATE_RATE_MS);
                            mUpdateTimeHandler.sendEmptyMessageDelayed(
                                    MSG_UPDATE_TIME, delayMs);
                        }
                        break;
                }
            }
        };

        final BroadcastReceiver mTimeZoneReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                time.clear(intent.getStringExtra("time-zone"));
                time.setToNow();
            }
        };
        boolean mRegisteredTimeZoneReceiver = false;

        private void registerReceiver() {
            if (mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = true;
            IntentFilter filter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
            service.registerReceiver(mTimeZoneReceiver, filter);
        }

        private void unregisterReceiver() {
            if (!mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = false;
            service.unregisterReceiver(mTimeZoneReceiver);
        }

        private boolean shouldTimerBeRunning() {
            return isVisible() && !isInAmbientMode();
        }

        @Override
        public void onDestroy() {
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            super.onDestroy();
        }


    }
}
