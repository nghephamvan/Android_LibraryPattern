package pvnghe.patternslibrary.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import pvnghe.patternslibrary.R;
import pvnghe.patternslibrary.designpatterns.Task;
import pvnghe.patternslibrary.util.TextUtils;

public class MainActivity extends AppCompatActivity {

    Button buttonCall;
    EditText editTextPhone;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonCall = (Button) findViewById(R.id.button_call);
        editTextPhone = (EditText) findViewById(R.id.phone_number);
        textView = (TextView) findViewById(R.id.text_view_result);

        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textInput = editTextPhone.getText().toString().trim();
                if (textInput.isEmpty() || textInput.length() <= 0) {
                    return;
                }

                textView.setText(TextUtils.addLinkPhoneNumber(textInput));
            }
        });


        Task.register(this, CallPhoneNumber.class, new Task.Assigner<CallPhoneNumber>() {
            @Override
            public void onRun(CallPhoneNumber object) {
                try {
                    Log.i(CallPhoneNumber.class.getSimpleName(), object.phonenumber);
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse(object.phonenumber));
                    startActivity(callIntent);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }



    public static class CallPhoneNumber implements Task.ActionTask {

        String phonenumber;

        public CallPhoneNumber(String phonenumber) {
            String stringTel = "tel:";
            this.phonenumber = stringTel.concat(phonenumber);
        }
    }
}
