package innopolis.aleksandr.projectorcontrol;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public final String PROJECTORCONTROL = "ProjectorControl";
    ToggleButton toogleButtonProjector;
    ToggleButton toogleButtonRoom;
    TextView device;
    TextView date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toogleButtonProjector = (ToggleButton) findViewById(R.id.tooglebutton_projector_state);
        toogleButtonRoom = (ToggleButton) findViewById(R.id.toogleButton_room_state);
        device = (TextView) findViewById(R.id.device_text);
        date = (TextView) findViewById(R.id.date_text);
        registerReceiver(broadcastReceiver, new IntentFilter("NOTIFICATION_RECIEVED"));

        toogleButtonProjector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toogleButtonProjector.isChecked()) {
                    ParseObject changeState = new ParseObject(PROJECTORCONTROL);
                    changeState.put("status", "On");
                    changeState.put("updatedFrom", "Mobile");
                    changeState.put("lockState", "Unlocked");
                    changeState.saveInBackground();
                } else {
                    ParseObject changeState = new ParseObject(PROJECTORCONTROL);
                    changeState.put("status", "Off");
                    changeState.put("updatedFrom", "Mobile");
                    changeState.put("lockState", "Unlocked");
                    changeState.saveInBackground();
                }
            }
        });
        refreshState();
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshState();
        }
    };

    public void refreshState() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PROJECTORCONTROL);
        query.orderByDescending("createdAt");
        final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage(getString(R.string.progress_loading));
        dialog.show();
        query.getFirstInBackground(new GetCallback<ParseObject>() { //we need only one object
            public void done(ParseObject object, ParseException e) {
                dialog.dismiss();
                if (object == null) {
                    Log.d("DBG", e.toString());
                    Toast.makeText(MainActivity.this, "Problems with connection, try later", Toast.LENGTH_SHORT).show();
                } else {
                    if (object.getString("status").equals("On") && toogleButtonProjector.isChecked() == false)
                        toogleButtonProjector.setChecked(true);
                    else if (object.getString("status").equals("Off") && toogleButtonProjector.isChecked() == true)
                        toogleButtonProjector.setChecked(false);

                    if (object.getString("lockState").equals("Locked")) {
                        toogleButtonRoom.setChecked(false);
                        toogleButtonProjector.setEnabled(false);
                    } else if (object.getString("lockState").equals("Unlocked")) {
                        toogleButtonRoom.setChecked(true);
                        toogleButtonProjector.setEnabled(true);
                    }
                    DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                    device.setText(object.getString("updatedFrom"));
                    date.setText(df.format(object.getUpdatedAt()));
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshState();
    }

}
