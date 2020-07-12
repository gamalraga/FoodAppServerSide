package gamal.myappnew.serverforclientapp.Server;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import gamal.myappnew.serverforclientapp.Common.Common;
import gamal.myappnew.serverforclientapp.MainActivity;
import gamal.myappnew.serverforclientapp.Moduel.Request;
import gamal.myappnew.serverforclientapp.Notification.NotificationHelper;

public class Lisiner extends Service implements ChildEventListener {
    DatabaseReference reference;
    public Lisiner() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        reference= FirebaseDatabase.getInstance().getReference(Common.REQUEST)

              ;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        reference   .orderByChild("status").equalTo("0") .addChildEventListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        Request request=snapshot.getValue(Request.class);
        NotificationHelper notificationHelper=new NotificationHelper(getBaseContext());
        notificationHelper.sendhightproirityNotification("new Order from "+request.getName(),"to "+request.getAdress(), MainActivity.class);
    }


    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }


    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }


    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}
