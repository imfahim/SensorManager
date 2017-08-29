package sensor.manager;

import profile.manager.y2j.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SensorManagerActivity extends Activity {
    /** Called when the activity is first created. */
	private Button startBtn;
	private Button stopBtn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        startBtn = (Button)findViewById(R.id.startBtn);
        stopBtn = (Button)findViewById(R.id.stopBtn);
        final Intent i=new Intent(getBaseContext(),SensorConnection.class);
        startBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startService(i);
            }
        });
        stopBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopService(i);
            }
        });
    }
}