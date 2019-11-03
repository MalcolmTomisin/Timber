package com.skillslevel.joules.utils;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.google.gson.Gson;

public class ServiceScheduler {
    public static void scheduleJob(Context context, Intent intent) {
        Bundle bundle = new Bundle();
        Gson g = new Gson();
        String json = g.toJson(intent);
        String tag = "intentAction";
        PersistableBundle persistableBundle = new PersistableBundle();
        persistableBundle.putString("intentObject", json);
        bundle.putParcelable(tag, intent);
        ComponentName serviceComponent = new ComponentName(context, MediaButtonService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(1000);
        builder.setOverrideDeadline(3 * 1000);
        builder.setRequiresDeviceIdle(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setTransientExtras(bundle);
        }
        builder.setExtras(persistableBundle);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
            jobScheduler.schedule(builder.build());
        }
    }
}
