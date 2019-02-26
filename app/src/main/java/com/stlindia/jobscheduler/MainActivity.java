package com.stlindia.jobscheduler;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    RadioGroup networkOptions;
    Button scheduleJob;
    private JobScheduler mScheduler;
    private static final int JOB_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


/*
        try {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:com.stlindia.jobscheduler"));
            startActivity(intent);

        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }*/

        scheduleJob = findViewById(R.id.schedule_job);

        scheduleJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                networkOptions = findViewById(R.id.networkOptions);
                int selectedNetworkID = networkOptions.getCheckedRadioButtonId();
                int selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE;
                mScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

                switch(selectedNetworkID){
                    case R.id.rad_nonetwork:
                        selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE;
                        break;
                    case R.id.rad_any_network:
                        selectedNetworkOption = JobInfo.NETWORK_TYPE_ANY;
                        break;
                    case R.id.rad_wifi_network:
                        selectedNetworkOption = JobInfo.NETWORK_TYPE_UNMETERED;
                        break;
                }

                ComponentName serviceName = new ComponentName(getPackageName(),
                        NotificationJobService.class.getName());

                boolean constraintSet = selectedNetworkOption != JobInfo.NETWORK_TYPE_NONE;

                if(constraintSet) {
                    JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, serviceName);
                    builder.setRequiredNetworkType(selectedNetworkOption).build();
                    //Schedule the job and notify the user
                   JobInfo myJobInfo = builder.build();
                    mScheduler.schedule(myJobInfo);
                    Toast.makeText(MainActivity.this, "Job Scheduled, job will run when " +
                            "the constraints are met.", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "Please set at least one constraint",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
