package um.feri.mihael.wi_finder;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.security.PublicKey;

public class EditHotSpotActivity extends AppCompatActivity {

    private TextView textSSID;
    private TextView textSecKey;
    private TextView textUserName;
    private TextView textEmail;
    private Button saveBtn;
    private Button deleteBtn;

    private Bundle extras;

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hot_spot);

        textSSID = (TextView) findViewById(R.id.editTextSSID);
        textSecKey = (TextView) findViewById(R.id.editTextSecKey);
        textUserName = (TextView) findViewById(R.id.editTextUserName);
        textEmail = (TextView) findViewById(R.id.editTextUserEmail);

        extras = getIntent().getExtras();

        if(extras != null)
        {
            position = extras.getInt(CallCodes.EXTRA_HOTSPOT_POS);
            textSSID.setText(extras.getString(CallCodes.EXTRA_HOTSPOT_SSID));
            textSecKey.setText(extras.getString(CallCodes.EXTRA_HOTSPOT_SEC_KEY));
            textUserName.setText(extras.getString(CallCodes.EXTRA_USER_NAME));
            textEmail.setText(extras.getString(CallCodes.EXTRA_USER_EMAIL));
        }

    }

    public void btnSaveClick(View v)
    {
        Intent saveIntent = new Intent();

        saveIntent.putExtra(CallCodes.RETURN_HOTSPOT_POS, position);
        saveIntent.putExtra(CallCodes.RETURN_EDIT_ACTION, CallCodes.ACTION_SAVE);
        saveIntent.putExtra(CallCodes.RETURN_HOTSPOT_SSID, textSSID.getText().toString());
        saveIntent.putExtra(CallCodes.RETURN_HOTSPOT_SEC_KEY, textSecKey.getText().toString());
        saveIntent.putExtra(CallCodes.RETURN_USER_NAME, textUserName.getText().toString());
        saveIntent.putExtra(CallCodes.RETURN_USER_EMAIL, textEmail.getText().toString());
        setResult(Activity.RESULT_OK, saveIntent);

        finish();
    }

    public void btnDeleteClick(View v)
    {
        Intent deleteIntent = new Intent();

        deleteIntent.putExtra(CallCodes.RETURN_EDIT_ACTION, CallCodes.ACTION_DELETE);
        deleteIntent.putExtra(CallCodes.RETURN_HOTSPOT_POS, position);
        setResult(Activity.RESULT_OK, deleteIntent);

        finish();
    }
}
