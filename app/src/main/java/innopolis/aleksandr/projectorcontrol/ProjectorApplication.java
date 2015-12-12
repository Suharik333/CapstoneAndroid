package innopolis.aleksandr.projectorcontrol;
import android.app.Application;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
/**
 * Created by Aleksandr on 11.12.2015.
 */
public class ProjectorApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "tXFcf53HS1hezSeE6UOUqpG4szPu0D2vRuoplWEy", "nH8w2bI6gkWY39PKJC250o2UpY4vCCrahsxhMgxF");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParsePush.subscribeInBackground("Projector1");
    }
}
