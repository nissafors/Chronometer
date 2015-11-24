package se.iqpussel.chronometer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private msChronometer mChrono;
    private boolean mRunning;
    private long mStopTime;
    private List<String> mLapTimes;

    // Event handler for start button: Start counting up.
    public void startButtonClick(View view) {
        if (!mRunning) {
            mChrono.start(mStopTime);
            mRunning = true;
        }
    }

    // Event handler for stop button: Stop counting up.
    public void stopButtonClick(View view) {
        if (mRunning) {
            mStopTime = mChrono.stop();
            mRunning = false;
        }
    }

    // Event handler for reset button: Zero counter and clear lap times
    public void resetButtonClick(View view) {
        mChrono.reset(0L);
        mStopTime = 0L;
        mRunning = false;
        mLapTimes.clear();
        printLapTimes();
    }

    // Event handler for lap button: Add lap time to list
    public void lapButtonClick(View view) {
        if (mRunning) {
            String lap = mChrono.lapAsString();
            mLapTimes.add(lap);
            printLapTimes();
        }
    }

    // Update ListView showing lap times
    private void printLapTimes() {
        ListView lv = (ListView)findViewById(R.id.lapListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                                                                android.R.layout.simple_list_item_1,
                                                                android.R.id.text1,
                                                                mLapTimes);
        lv.setAdapter(adapter);
    }

    // Init
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mChrono = (msChronometer)findViewById(R.id.msChronometer);
        mStopTime = 0L;
        mRunning = false;
        mLapTimes = new ArrayList<String>();
    }
}
