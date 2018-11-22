package com.example.hrida.digitalclock;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.BatteryManager;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    /*You can stop the painted clock and check the xml Text clock by commenting the following*/

    /*here*/

    Time mTime;

    Handler handler;
    Runnable r;

    /*till here*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*here*/

        mTime = new Time();

        r = new Runnable(){
            @Override
            public void run(){
                mTime.setToNow();
                drawingView dv =  new drawingView(MainActivity.this, mTime.hour, mTime.minute, mTime.second, mTime.weekDay, mTime.monthDay, getBatteryLevel());

                setContentView(dv);
                handler.postDelayed(r, 1000);
            }
        };

        handler = new Handler();
        handler.postDelayed(r,1000);

        /*till here*/
    }

    /*here*/

    public float getBatteryLevel(){
        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        if(level == 1 || scale == -1){
            return 50.0f;
        }

        return ((float) level / (float) scale) * 100.0f;
    }

    public class drawingView extends View {

        Paint mBackgroundPaint, myTextPaint, myTextPaintBack;

        Typeface tf;

        int hours, minutes, seconds, weekday, date;
        float battery;

        public drawingView(Context context, int hours, int minutes, int seconds, int weekday, int date, float battery) {
            super(context);

            tf = Typeface.createFromAsset(getAssets(),"font.ttf");

            mBackgroundPaint = new Paint();
            mBackgroundPaint.setColor(ContextCompat.getColor(getApplicationContext(), R.color.background));

            myTextPaint = new Paint();
            myTextPaint.setColor(ContextCompat.getColor(getApplicationContext(), R.color.text));
            myTextPaint.setAntiAlias(true);
            myTextPaint.setTextAlign(Paint.Align.CENTER);
            myTextPaint.setTextSize(getResources().getDimension(R.dimen.text_size));
            myTextPaint.setTypeface(tf);

            myTextPaintBack = new Paint();
            myTextPaintBack.setColor(ContextCompat.getColor(getApplicationContext(), R.color.text_back));
            myTextPaintBack.setAntiAlias(true);
            myTextPaintBack.setTextAlign(Paint.Align.CENTER);
            myTextPaintBack.setTextSize(getResources().getDimension(R.dimen.text_size));
            myTextPaintBack.setTypeface(tf);

            this.hours = hours;
            this.minutes = minutes;
            this.seconds = seconds;
            this.weekday = weekday;
            this.date = date;
            this.battery = battery;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            float width = canvas.getWidth();
            float height = canvas.getHeight();

            canvas.drawRect(0,0, width, height, mBackgroundPaint);

            float centerX = width / 2f;
            float centerY = height / 2f;

            int cur_hour = hours;
            String cur_ampm = "AM";

            if(cur_hour == 0){
                cur_hour = 12;
            }
            if(cur_hour > 12){
                cur_hour = cur_hour - 12;
                cur_ampm = "PM";
            }

            String text = String.format("%02d:%02d:%02d:", cur_hour, minutes, seconds);
            String day_of_week = "";

            if (weekday == 1){
                day_of_week = "MON";
            }else if (weekday == 2){
                day_of_week = "TEU";
            }else if (weekday == 3){
                day_of_week = "WED";
            }else if (weekday == 4){
                day_of_week = "THU";
            }else if (weekday == 5){
                day_of_week = "FRI";
            }else if (weekday == 6){
                day_of_week = "SAT";
            }else if (weekday == 7){
                day_of_week = "SUN";
            }

            String text2 = String.format("DATE: %s %d", day_of_week, date);

            String batteryLevel = "BATTERY: " + (int) battery + "%";

            canvas.drawText("00 00 00", centerX, centerY, myTextPaintBack);

            myTextPaint.setColor(ContextCompat.getColor(getApplicationContext(), R.color.text));
            myTextPaint.setTextSize(getResources().getDimension(R.dimen.text_size));
            canvas.drawText(text, centerX, centerY, myTextPaint);

            myTextPaint.setColor(ContextCompat.getColor(getApplicationContext(), R.color.text));
            myTextPaint.setTextSize(getResources().getDimension(R.dimen.text_size_small));
            canvas.drawText(batteryLevel + " " + text2, centerX, centerY + getResources().getDimension(R.dimen.text_size_small), myTextPaint);
        }
    }

    /*till here*/

}


