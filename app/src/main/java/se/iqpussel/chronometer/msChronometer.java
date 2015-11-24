package se.iqpussel.chronometer;

import android.content.Context;
import android.os.Message;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Class that implements a simple timer that, as opposed to native chronometer, also shows milliseconds.
 * <p>
 * msChronometer is based on TextView, so use TextView properties to set font, fontsize etc.
 * Public methods are start, stop, lap and reset.
  */
public class msChronometer extends TextView {
    private boolean mRunning;
    private long mCountFrom;
    private long mStartTime;
    private boolean mVisible;

    // Constructors
    public msChronometer(Context context) {
        super(context);
        init();
    }
    public msChronometer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public msChronometer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
//    Present in TextView but not compatible with API level
//    public msChronometer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        init();
//    }

    // Common things to do for constructors
    private void init() {
        mRunning = false;
        mVisible = this.getVisibility() == VISIBLE;
        updateText(0);
    }

    /**
     * Begin counting up.
     *
     * @param countFrom Time (in milliseconds) to count up from.
     */
    public void start(long countFrom) {
        mCountFrom = countFrom;
        mStartTime = SystemClock.elapsedRealtime();
        mRunning = true;
        mHandler.sendMessage(Message.obtain(mHandler, 2));
    }

    /**
     * Stop counting up.
     *
     * @return The value of the timer (in milliseconds) had when it was stopped.
     */
    public long stop() {
        long now = getNow();
        mRunning = false;
        return now;
    }

    /**
     * Get the current value of the timer.
     *
     * @return The current timer value in milliseconds.
     */
    public long lap() {
        return getNow();
    }

    /**
     * Get the current value of the timer.
     *
     * @return A string formatted as (HH):MM:SS:ms.
     */
    public String lapAsString() {
        return getFormattedTime(getNow());
    }

    /**
     * Stop timer and reset to given time.
     *
     * @param resetTo The time in milliseconds that the timer should reset to.
     */
    public void reset(long resetTo) {
        mRunning = false;
        updateText(resetTo);
    }

    // Calculate and return current timer value
    private long getNow() {
        if (mRunning)
            return SystemClock.elapsedRealtime() - mStartTime + mCountFrom;
        else
            return 0L;
    }

    // Return string in format (HH):MM:SS:ms calculated from time in milliseconds
    private String getFormattedTime(long time) {
        long h = time / 1000 / 60 / 60;
        long m = (time / 1000 / 60) % 60;
        long s = (time / 1000) % 60;
        long hs = (time / 10) % 100;
        if (h > 0)
            return String.format("%d:%02d:%02d:%02d", h, m, s, hs);
        else
            return String.format("%02d:%02d:%02d", m, s, hs);
    }

    // Display time
    private synchronized void updateText(long milliSeconds) {
        String text = getFormattedTime(milliSeconds);
        setText(text);
    }

    // Count up timer
    private Handler mHandler = new Handler() {
        public void handleMessage(Message m) {
            if (mRunning && mVisible) {
                updateText(getNow());
                sendMessageDelayed(Message.obtain(this), 10);
            }
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mVisible = false;
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        mVisible = visibility == VISIBLE;
        if (mVisible)
            mHandler.sendMessageDelayed(Message.obtain(mHandler), 10);
    }
}