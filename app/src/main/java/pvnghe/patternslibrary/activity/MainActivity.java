package pvnghe.patternslibrary.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import pvnghe.patternslibrary.R;
import pvnghe.patternslibrary.designpatterns.Task;

public class MainActivity extends AppCompatActivity {

    Button buttonCall;
    EditText editTextPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonCall = (Button) findViewById(R.id.button_call);
        editTextPhone = (EditText) findViewById(R.id.phone_number);

        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = editTextPhone.getText().toString().trim();
                if (phoneNumber.isEmpty() || phoneNumber.length() <= 0) {
                    return;
                }

                Task.publish(new CallPhoneNumber(phoneNumber));
            }
        });


        Task.register(this, CallPhoneNumber.class, new Task.Assigner<CallPhoneNumber>() {
            @Override
            public void onRun(CallPhoneNumber object) {

                Log.i(CallPhoneNumber.class.getSimpleName(), object.phonenumber);
                Intent callIntent = new Intent(Intent.ACTION_NEW_OUTGOING_CALL);
                callIntent.setData(Uri.parse(object.phonenumber));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);

            }
        });
    }



    public static class CallPhoneNumber implements Task.ActionTask {

        String phonenumber;

        public CallPhoneNumber(String phonenumber) {
            this.phonenumber = phonenumber;
        }
    }
}
