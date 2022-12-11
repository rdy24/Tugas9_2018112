package com.example.healthcare.worker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.healthcare.R;

public class MyWorker extends Worker {
  public MyWorker(@NonNull Context context, @NonNull
          WorkerParameters workerParams) {
    super(context, workerParams);
  }
  @NonNull
  @Override
  public ListenableWorker.Result doWork() {
    displayNotification("Haii", "Berita baru telah tersedia");

    return ListenableWorker.Result.success();
  }
  public void displayNotification(String task, String desc) {
    NotificationManager manager =
            (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
    NotificationChannel channel = new
            NotificationChannel("Hai", "notifBerita",
            NotificationManager.IMPORTANCE_HIGH);
    manager.createNotificationChannel(channel);
    NotificationCompat.Builder builder = new

            NotificationCompat.Builder(getApplicationContext(),
            "Hai")
            .setContentTitle(task)
            .setContentText(desc)
            .setSmallIcon(R.drawable.ic_baseline_newspaper_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

            .setCategory(NotificationCompat.CATEGORY_MESSAGE);
    manager.notify(1, builder.build());
  }
}
