package gamal.myappnew.serverforclientapp.Remote;



import gamal.myappnew.serverforclientapp.Server.MyResponse;
import gamal.myappnew.serverforclientapp.Server.Sender;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAxoqSoUE:APA91bGKRAJifgv8CveiHtFfbWGzild8LdD5xGy4MJoMhnb9ky4BL2-Yy4VIyOcGfgqu7sO_ENgtt77hsGH4Vs75F9w2x_h4Kwxa5pcoBGCZnTF_VwGI00afTA5rxcb52U57dyopYDuD"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);


}
