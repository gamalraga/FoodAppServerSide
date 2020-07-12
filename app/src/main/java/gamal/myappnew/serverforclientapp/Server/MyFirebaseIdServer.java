package gamal.myappnew.serverforclientapp.Server;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import gamal.myappnew.serverforclientapp.Common.Common;

public class MyFirebaseIdServer extends FirebaseMessagingService {
//    @Override
//    public void onTokenRefresh() {
//        super.onTokenRefresh();
//        String tokenRefershed=FirebaseInstanceId.getInstance().getToken();
//        if (Common.CURRENT_USER!=null)
//        UpdateTokenToFirebase(tokenRefershed);
//    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        String tokenRefershed=FirebaseInstanceId.getInstance().getToken();
        if (Common.CURRENT_USER!=null)
            UpdateTokenToFirebase(tokenRefershed);
    }

    private void UpdateTokenToFirebase(String tokenRefershed) {
        FirebaseDatabase db=FirebaseDatabase.getInstance();
        DatabaseReference tokens=db.getReference(Common.TOKENS);
        Token token=new Token(tokenRefershed,true);
        tokens.child(Common.CURRENT_USER.getId()).setValue(token);
    }
}
