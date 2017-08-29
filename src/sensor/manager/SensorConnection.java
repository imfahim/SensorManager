package sensor.manager;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.IBinder;
import android.widget.Toast;

public class SensorConnection extends Service implements SensorEventListener{
	
	SensorManager sensorManager;
	AudioManager audioManager;
	Sensor lightSensor,accelerometerSensor,proximitySensor;
	boolean Light=false; 
	boolean frontDown=false;
	boolean timesup=false;
	boolean upsideDown=false;
	public void onCreate(){
		
		audioManager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        
        lightSensor=sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        
        checkSensors(accelerometerSensor,proximitySensor,lightSensor);
        
	}
	
	
	public void checkSensors(Sensor ac,Sensor ps,Sensor ls){
		if (ac == null){
			Toast.makeText(this,"Accelerometer Sensor Not Found",Toast.LENGTH_SHORT).show();   
        }
        else {
        	sensorManager.registerListener(this,accelerometerSensor,SensorManager.SENSOR_DELAY_UI);
        }
        if (ps == null){
        	Toast.makeText(this,"Proximity Sensor Not Found",Toast.LENGTH_SHORT).show();
        }
        else {
        	sensorManager.registerListener(this,proximitySensor,SensorManager.SENSOR_DELAY_UI);
        	
        }
        if (ls == null){
          	Toast.makeText(this,"Light Sensor Not Found",Toast.LENGTH_SHORT).show();
        }
        else {
        	sensorManager.registerListener(this,lightSensor,SensorManager.SENSOR_DELAY_UI);
        }
		
	}
	
	
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	public void onSensorChanged(SensorEvent e) {
		// TODO Auto-generated method stub
		/*if (e.sensor.getType() == Sensor.TYPE_LIGHT ) {
            if (e.values[0] ==0) {
            	Light=false;     
            }
            else
            {
            	Light=true;
            }
        }*/
		
		
		if (e.sensor.getType() == Sensor.TYPE_ACCELEROMETER ) {
            if(e.values[1] <= -9){
            	upsideDown=true;
            }
            else {
            	upsideDown =false;
            }
            if(e.values[2]<= -9){
            	frontDown=true;
            }
            else{
            	frontDown=false;
            }
        }
		
		
		if (e.sensor.getType() == Sensor.TYPE_PROXIMITY ) {
			if (e.values[0]!= proximitySensor.getMaximumRange()) {
				Light=true;
            }
            else{
            	Light=false;
            }
        }
		
		if(getTime()=="12:00:00"){
			timesup=true;
		}
		if(getTime()=="06:00:00"){
			timesup=false;
		}
		execute();
	}
	public void execute(){

		if (frontDown){
			audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
			Toast.makeText(this,"FrontDown true light true",Toast.LENGTH_SHORT).show();
		}
		if (!Light && frontDown){
			audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
			Toast.makeText(this,"Light & frondDOwn false",Toast.LENGTH_SHORT).show();
		}
		if(upsideDown && !Light){
			Toast.makeText(this,"upsideDown true light false",Toast.LENGTH_SHORT).show();
			audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
		}
		if(timesup){
			Toast.makeText(this,"timesup true",Toast.LENGTH_SHORT).show();
			audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		}
		
	}
	
	public String getTime(){
		Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        String strDate=mdformat.format(calendar.getTime());
        return strDate;
	}
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int onStartCommand(Intent intent,int flags,int Id)
    {
        return 0;
    }
    public void onDestroy()

    {
        sensorManager.unregisterListener(this);

    }
}
