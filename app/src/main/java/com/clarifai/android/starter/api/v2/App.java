package com.clarifai.android.starter.api.v2;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class App extends Application {

  // In a real app, rather than attaching singletons (such as the API client instance) to your Application instance,
  // it's recommended that you use something like Dagger 2, and inject your client instance.
  // Since that would be a distraction here, we will just use a regular singleton.
  private static App INSTANCE;

  @NonNull
  public static App get() {
    final App instance = INSTANCE;
    if (instance == null) {
      throw new IllegalStateException("App has not been created yet!");
    }
    return instance;
  }

  @Nullable
  private ClarifaiClient client;

  @Override
  public void onCreate() {
    INSTANCE = this;

     //Context temp_context = Context();

      Log.d("Alert Dialogue", "displaying message upon startup");

    try{

        DBHandler db = new DBHandler(getApplicationContext());

        Context temp_context = getApplicationContext();
        Log.d("Alert Dialogue", "made temp");
        AlertDialog.Builder builder1 = new AlertDialog.Builder(temp_context);
        Log.d("Alert Dialogue", "made builder");
        builder1.setMessage("Write your message here.");
        builder1.setCancelable(true);

        Log.d("Alert Dialogue", "set message");

        /*builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }); */

        AlertDialog alert11 = builder1.create();
        Log.d("Alert Dialogue", "created");
        alert11.show();
        Log.d("Alert Dialogue", "shown");
    }
    catch (Exception e){
        Log.d("Alert Dialogue", "failed to display");
    }

    client = new ClarifaiBuilder("qeNitO9lCdNO7k7UixA6yUXKIIX3-MolxrXUL4Oq", "zOxPlyS5BfqFCp_CVW1Y2ZkZyNW81IMQ6sAOkodR")
        // Optionally customize HTTP client via a custom OkHttp instance
        .client(new OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS) // Increase timeout for poor mobile networks

            // Log all incoming and outgoing data
            // NOTE: You will not want to use the BODY log-level in production, as it will leak your API request details
            // to the (publicly-viewable) Android log
            .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
              @Override public void log(String logString) {
                Timber.e(logString);
              }
            }).setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
        )
        .buildSync(); // use build() instead to get a Future<ClarifaiClient>, if you don't want to block this thread
    super.onCreate();

    // Initialize our logging
    Timber.plant(new Timber.DebugTree());
  }

  @NonNull
  public ClarifaiClient clarifaiClient() {
    final ClarifaiClient client = this.client;
    if (client == null) {
      throw new IllegalStateException("Cannot use Clarifai client before initialized");
    }
    return client;
  }
}
