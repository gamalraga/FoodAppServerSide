package gamal.myappnew.serverforclientapp.Server;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import gamal.myappnew.serverforclientapp.MainActivity;
import gamal.myappnew.serverforclientapp.Notification.NotificationHelper;


public class MyFirebaseMessaging extends FirebaseMessagingService {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        SendNotification(remoteMessage);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void SendNotification(RemoteMessage remoteMessage) {
        RemoteMessage.Notification notification=remoteMessage.getNotification();
        NotificationHelper notificationHelper=new NotificationHelper(getApplicationContext());
        notificationHelper.sendhightproirityNotification(notification.getTitle(),notification.getBody(), MainActivity.class);
    }
}
