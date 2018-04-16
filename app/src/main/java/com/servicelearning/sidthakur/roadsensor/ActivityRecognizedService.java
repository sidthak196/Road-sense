package com.servicelearning.sidthakur.roadsensor;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ActivityRecognizedService extends IntentService {

    public ActivityRecognizedService() {
        super("ActivityRecognizedService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            if (ActivityRecognitionResult.hasResult(intent)) {
                ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
                handleDetectedActivities(result.getProbableActivities());

            }
        }


    }

    private void handleDetectedActivities(List<DetectedActivity> probableActivities) {
        final Intent intent = new Intent(this, SensorService.class);
        for (DetectedActivity activity : probableActivities) {
            switch (activity.getType()) {
                case DetectedActivity.IN_VEHICLE: {

                    Log.e("ActivityRecogition", "In Vehicle: " + activity.getConfidence());
                    if (activity.getConfidence() > 75) {
                        Toast.makeText(getBaseContext(), "In Vehicle", Toast.LENGTH_LONG).show();

                        startService(intent);
                    }
                    break;
                }
                case DetectedActivity.UNKNOWN: {
                    Log.e("ActivityRecogition", "Unknown: " + activity.getConfidence());
                    break;
                }
                default:
                    Log.e("ActivityRecogition", activity.getType() + ":" + activity.getConfidence());
                    if (activity.getConfidence() > 75) {

                        stopService(intent);
                    }
                    break;

            }
        }
    }

}
