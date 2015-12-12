package innopolis.aleksandr.projectorcontrol;
import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.parse.ParsePushBroadcastReceiver;
/**
 * Created by Aleksandr on 11.12.2015.
 */
public class MyParseBroadCastReciever extends ParsePushBroadcastReceiver {
    public static final String TAG = "DBG";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        context.sendBroadcast(new Intent("NOTIFICATION_RECIEVED"));

    }


}
