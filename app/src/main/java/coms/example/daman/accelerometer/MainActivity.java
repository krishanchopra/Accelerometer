package coms.example.daman.accelerometer;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

class queue{
    String queue[] = new String[20];
    int front = -1 ;
    int rear = -1;

    public void enqueue(String data){
        rear=(rear+1)%20;
        queue[rear] = data;
        if(front == -1)
            front = 0;
    }

    public String[] getdata(){
        return queue;
    }
}

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager mSensormanager;
    Sensor mAccelerometer;
    queue accl = new queue();
    queue grav = new queue();
    queue no_g = new queue();
    String acc[] = new String[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensormanager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensormanager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        checkSensorAvailable();
        mSensormanager.registerListener(this , mAccelerometer , SensorManager.SENSOR_DELAY_NORMAL );
    }

    //Function to check if Accelerometer is available.
    public void checkSensorAvailable(){
        if(mAccelerometer == null){
            Toast.makeText(this , "Your Device Doesn't have an Accelerometer." , Toast.LENGTH_LONG).show();
           // Log.d("test" , "okay");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x1 = event.values[0];
        float y1 = event.values[1];
        float z1 = event.values[2];
        String x_cord = acc[0] = String.format(java.util.Locale.US , "%.4f" , x1);
        String y_cord = acc[1] = String.format(java.util.Locale.US , "%.4f" , y1);
        String z_cord = acc[2] = String.format(java.util.Locale.US , "%.4f" , z1);

        String acc_data = x_cord+" , "+y_cord+" , "+z_cord;
        accl.enqueue(acc_data);
        //TextView tests = (TextView)findViewById(R.id.test);
        //tests.setText(acc_data);

        TextView mTextView_x = (TextView)findViewById(R.id.x_value);
        if(mTextView_x != null)
            mTextView_x.setText(x_cord);
        TextView mTextView_y = (TextView)findViewById(R.id.y_value);
        if(mTextView_y != null)
            mTextView_y.setText(y_cord);
        TextView mTextView_z = (TextView)findViewById(R.id.z_value);
        if(mTextView_z != null)
            mTextView_z.setText(z_cord);

        final float alpha = 0.98f;

        float gravity[] = new float[3] ;
        float linear_acceleration[] = new float[3];
        gravity[0] = alpha * gravity[0] + (1 - alpha)*event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha)*event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha)*event.values[2];

        String x_g = String.format(java.util.Locale.US , "%.4f" , gravity[0]);
        String y_g = String.format(java.util.Locale.US , "%.4f" , gravity[1]);
        String z_g = String.format(java.util.Locale.US , "%.4f" , gravity[2]);

        String grav_data = x_g+" , "+y_g+" , "+z_g;
        grav.enqueue(grav_data);

        TextView mTextView_x_G = (TextView)findViewById(R.id.x_value_G);
        if(mTextView_x_G != null)
            mTextView_x_G.setText(x_g);
        TextView mTextView_y_G = (TextView)findViewById(R.id.y_value_G);
        if(mTextView_y_G != null)
            mTextView_y_G.setText(y_g);
        TextView mTextView_z_G = (TextView)findViewById(R.id.z_value_G);
        if(mTextView_z_G != null)
            mTextView_z_G.setText(z_g);

        linear_acceleration[0] = event.values[0] - gravity[0];
        linear_acceleration[1] = event.values[1] - gravity[1];
        linear_acceleration[2] = event.values[2] - gravity[2];

        String x_nog = String.format(java.util.Locale.US , "%.4f" , linear_acceleration[0]);
        String y_nog = String.format(java.util.Locale.US , "%.4f" , linear_acceleration[1]);
        String z_nog = String.format(java.util.Locale.US , "%.4f" , linear_acceleration[2]);

        String nog_data = x_nog+" , "+y_nog+" , "+z_nog;
        no_g.enqueue(nog_data);

        TextView mTextView_x_noG = (TextView)findViewById(R.id.x_value_noG);
        if(mTextView_x_noG != null)
            mTextView_x_noG.setText(x_nog);
        TextView mTextView_y_noG = (TextView)findViewById(R.id.y_value_noG);
        if(mTextView_y_noG != null)
            mTextView_y_noG.setText(y_nog);
        TextView mTextView_z_noG = (TextView)findViewById(R.id.z_value_noG);
        if(mTextView_z_noG != null)
            mTextView_z_noG.setText(z_nog);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void onButtonClick(View view) throws IOException {
        TextView putHeading= (TextView)findViewById(R.id.heading);
        TextView list = (TextView)findViewById(R.id.test);
        FileOutputStream f = null;

        if(view.getId() == R.id.button1){
            String a[] = accl.getdata();
            String filename  = new SimpleDateFormat("yyyyMMddhhmmss'.txt'").format(new Date());
            String putText ="Acceleration Readings"+"\n\n"+a[0]+"\n"+a[1]+"\n"+a[2]+"\n"+a[3]+"\n"+a[4]+"\n"+a[5]+"\n"+a[6]+"\n"+a[7]+
                    "\n"+a[8]+"\n"+a[9]+"\n"+a[10]+"\n"+a[11]+"\n"+a[11]+"\n"+a[12]+"\n"+a[13]+"\n"+a[14]
                    +"\n"+a[15]+"\n"+a[16]+"\n"+a[17]+"\n"+a[18]+"\n"+a[19]+"\n";
            filename = "/mnt/sdcard/acc_"+filename;
            Log.d("test" , filename );
            try {
                f = new FileOutputStream(filename);
                byte[] buffer = putText.getBytes();
                f.write(buffer, 0, buffer.length);
                f.close();
                }
            catch (FileNotFoundException e) {
                e.printStackTrace();
                }
            finally{
                if(f != null)
                    f.close();
                }
            Toast.makeText(this , "File Saved as "+filename , Toast.LENGTH_LONG).show();

            putHeading.setText(R.string.string5);
            //Log.d("test" , a[19]);
            list.setText(a[0]+"\n"+a[1]+"\n"+a[2]+"\n"+a[3]+"\n"+a[4]+"\n"+a[5]+"\n"+a[6]+"\n"+a[7]+
                    "\n"+a[8]+"\n"+a[9]+"\n"+a[10]+"\n"+a[11]+"\n"+a[11]+"\n"+a[12]+"\n"+a[13]+"\n"+a[14]
                    +"\n"+a[15]+"\n"+a[16]+"\n"+a[17]+"\n"+a[18]+"\n"+a[19]+"\n"+"Press Record Button again to refresh.");
        }
        else if(view.getId() == R.id.button2){
            String filename  = new SimpleDateFormat("yyyyMMddhhmmss'.txt'").format(new Date());
            String a[] = no_g.getdata();
            filename = "/mnt/sdcard/grav_"+filename;
            Log.d("test" , filename );
            String putText ="Gravity Readings"+"\n\n"+a[0]+"\n"+a[1]+"\n"+a[2]+"\n"+a[3]+"\n"+a[4]+"\n"+a[5]+"\n"+a[6]+"\n"+a[7]+
                    "\n"+a[8]+"\n"+a[9]+"\n"+a[10]+"\n"+a[11]+"\n"+a[11]+"\n"+a[12]+"\n"+a[13]+"\n"+a[14]
                    +"\n"+a[15]+"\n"+a[16]+"\n"+a[17]+"\n"+a[18]+"\n"+a[19]+"\n";

            try {
                f = new FileOutputStream(filename);
                byte[] buffer = putText.getBytes();
                f.write(buffer, 0, buffer.length);
                f.close();
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            finally{
                if(f != null)
                    f.close();
            }
            putHeading.setText(R.string.string6);
            Toast.makeText(this , "File Saved as "+filename , Toast.LENGTH_LONG).show();
            //Log.d("test" , a[19]);
            list.setText(a[0]+"\n"+a[1]+"\n"+a[2]+"\n"+a[3]+"\n"+a[4]+"\n"+a[5]+"\n"+a[6]+"\n"+a[7]+
                    "\n"+a[8]+"\n"+a[9]+"\n"+a[10]+"\n"+a[11]+"\n"+a[11]+"\n"+a[12]+"\n"+a[13]+"\n"+a[14]
                    +"\n"+a[15]+"\n"+a[16]+"\n"+a[17]+"\n"+a[18]+"\n"+a[19]+"\n"+"Press Record Button again to refresh.");
        }
        else if(view.getId() == R.id.button3) {

            String filename  = new SimpleDateFormat("yyyyMMddhhmmss'.txt'").format(new Date());
            String a[] = grav.getdata();
            filename = "/mnt/sdcard/acc_noG_"+filename;
            Log.d("test" , filename );

            String putText ="Acceleration without Gravity Readings"+"\n\n"+a[0]+"\n"+a[1]+"\n"+a[2]+"\n"+a[3]+"\n"+a[4]+"\n"+a[5]+"\n"+a[6]+"\n"+a[7]+
                    "\n"+a[8]+"\n"+a[9]+"\n"+a[10]+"\n"+a[11]+"\n"+a[11]+"\n"+a[12]+"\n"+a[13]+"\n"+a[14]
                    +"\n"+a[15]+"\n"+a[16]+"\n"+a[17]+"\n"+a[18]+"\n"+a[19]+"\n";

            try {
                f = new FileOutputStream(filename);
                byte[] buffer = putText.getBytes();
                f.write(buffer, 0, buffer.length);
                f.close();
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            finally{
                if(f != null)
                    f.close();
            }

            putHeading.setText(R.string.string7);
            Toast.makeText(this , "File Saved as "+filename , Toast.LENGTH_LONG).show();
            //Log.d("test" , a[19]);
            list.setText(a[0]+"\n"+a[1]+"\n"+a[2]+"\n"+a[3]+"\n"+a[4]+"\n"+a[5]+"\n"+a[6]+"\n"+a[7]+
                    "\n"+a[8]+"\n"+a[9]+"\n"+a[10]+"\n"+a[11]+"\n"+a[11]+"\n"+a[12]+"\n"+a[13]+"\n"+a[14]
                    +"\n"+a[15]+"\n"+a[16]+"\n"+a[17]+"\n"+a[18]+"\n"+a[19]+"\n"+"Press Record Button again to refresh.");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensormanager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensormanager.unregisterListener(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        TextView text = (TextView)findViewById(R.id.Orientation_value);

        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            text.setText("Potrait");
        }
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            text.setText("Landscape");
        }
    }
}
