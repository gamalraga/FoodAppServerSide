package gamal.myappnew.serverforclientapp.Common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.Locale;

import gamal.myappnew.serverforclientapp.Moduel.Request;
import gamal.myappnew.serverforclientapp.Moduel.User;
import gamal.myappnew.serverforclientapp.Remote.APIService;
import gamal.myappnew.serverforclientapp.Remote.ClientRetrofiltNotfiy;
import gamal.myappnew.serverforclientapp.Remote.IGoCoordinates;

public class Common {
    public static final String GETAGORY_REF ="Category" ;
    public static final String ORDER_NEW_SGIP_TABLE ="OrderNeedShip" ;
    public static final String FOODS ="Foods" ;
    public static final String REQUEST = "Request";
    public static final String MOSTPOPULAR ="MostPopular" ;
    public static final String DESTDEAL ="BestDeals";
    public static final String UPDATECATEGORY ="Update" ;
    public static final String DELETECATEGORY ="Delete" ;
    public static final String TOKENS ="Tokens" ;
    public static final String SHIPPER ="Shippers" ;
    public static final String RATING ="Rating" ;
    public static Request CURRENTREQUEST = null;
    public static  String USERS_REF="Users";
    public static User CURRENT_USER=null;
    public static String PHONE="";
    public static final  String baseurl="https://maps.googleapis.com";
    public static String CURRENT_ID= FirebaseAuth.getInstance().getCurrentUser().getUid();
    public static  String IMAGE_URL="https://firebasestorage.googleapis.com/v0/b/gamaldox-15260.appspot.com/o/placeprofile.png?alt=media&token=555836cd-4ac5-4772-8cde-c53b240ac51a";
    public static String ConvertStatusts(String status) {
        if (status.equals("0"))
            return "Placed";
        else if (status.equals("1"))
            return "On My Way";
        else if (status.equals("2"))
            return "Shipping";
        else
            return "Shipped";
    }
//  public static IGoCoordinates getGeoCodeServices()
//  {
//      return RetrofitClient.getClient(baseurl).create(IGoCoordinates.class);
//  }
  public static Bitmap scalesBitmap(Bitmap bitmap,int newithd,int nehight)
  {
      Bitmap scalebitmap=Bitmap.createBitmap(newithd,nehight,Bitmap.Config.ARGB_8888);
      float scalx=newithd/(float)bitmap.getWidth();
      float scaly=nehight/(float)bitmap.getHeight();
      float pivoitex=0,povitey=0;
      Matrix scalmatriax=new Matrix();
      scalmatriax.setScale(scalx,scaly,pivoitex,povitey);
      Canvas canvas=new Canvas(scalebitmap);
      canvas.setMatrix(scalmatriax);
      canvas.drawBitmap(bitmap,0,0,new Paint(Paint.FILTER_BITMAP_FLAG));
      return scalebitmap;




  }
  public static boolean isconnectiontointernet(Context context)
  {
      ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
 if (connectivityManager!=null)
 {
     NetworkInfo[] infos=connectivityManager.getAllNetworkInfo();
     if (infos!=null)
     {
         for (int i=0;i<infos.length;i++)
         {
             if (infos[i].getState()==NetworkInfo.State.CONNECTED)
                 return true;
         }


     }
 }


 return false;
  }
    private static final String BASE_URL="https://fcm.googleapis.com/";
    public static APIService getFCMServec()
    {
        return ClientRetrofiltNotfiy.getClient(BASE_URL).create(APIService.class);
    }
    public static String getDate(long time)
    {
        Calendar calendar=Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        StringBuilder date=new StringBuilder(android.text.format.DateFormat.format("dd-MM-yyy HH:MM",
                calendar).toString());
        return date.toString();


    }
}
