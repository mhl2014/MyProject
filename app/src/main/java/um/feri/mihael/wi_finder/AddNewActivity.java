package um.feri.mihael.wi_finder;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddNewActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Resources res;

    private TextView addSSID;
    private TextView addSecKey;

    private Button btnScan;
    private Button btnAdd;

    private Spinner spinnerAccessability;

    public String accessLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        res = getResources();

        setContentView(R.layout.activity_add_new);

        addSSID = (TextView) findViewById(R.id.addEditTextSSID);
        addSecKey = (TextView) findViewById(R.id.addEditTextSecKey);

        btnScan = (Button) findViewById(R.id.buttonScan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {
                    Intent scanIntent = new Intent(CallCodes.REQ_SCAN);
                    scanIntent.putExtra(CallCodes.SCAN_MODE, CallCodes.QR_CODE_MODE);
                    startActivityForResult(scanIntent, CallCodes.ACTION_SCAN);
                }
                catch (ActivityNotFoundException anfe)
                {
                    showDownloadDialog(AddNewActivity.this, res.getString(R.string.zxingNotFound),
                            res.getString(R.string.zxingDownload),
                            res.getString(R.string.yes),
                            res.getString(R.string.no))
                            .show();
                }
            }
        });

        btnAdd = (Button) findViewById(R.id.buttonAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addNewIntent = new Intent();
                addNewIntent.putExtra(CallCodes.RETURN_HOTSPOT_SSID, addSSID.getText().toString());
                addNewIntent.putExtra(CallCodes.RETURN_HOTSPOT_SEC_KEY, addSecKey.getText().toString());
                addNewIntent.putExtra(CallCodes.RETURN_HOTSPOT_ACCESS, );

                setResult(Activity.RESULT_OK, addNewIntent);
                finish();
            }
        });

        spinnerAccessability = (Spinner) findViewById(R.id.activityAddNewSpinnerAccess);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.accessibility, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAccessability.setAdapter(adapter);
        spinnerAccessability.setOnItemSelectedListener(this);
    }

    /*
    public void btnScanClick(View v)
    {
    }
    */

    private AlertDialog showDownloadDialog(final Activity ac, String title, String msg, String btnYes, String btnNo) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(ac);
        dialog.setTitle(title);
        dialog.setMessage(msg);

        dialog.setPositiveButton(btnYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(res.getString(R.string.marketUri) + res.getString(R.string.zxingUri)));

                try
                {
                    ac.startActivity(viewIntent);
                }
                catch (ActivityNotFoundException afne)
                {
                    Toast.makeText(ac, res.getString(R.string.activityNotFound), Toast.LENGTH_LONG).show();
                }
            }
        });

        dialog.setNegativeButton(btnNo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        return dialog.show();
    }

    /*
    public void btnAddClick(View v)
    {

    }
    */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == CallCodes.ACTION_SCAN)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                addSecKey.setText(data.getStringExtra(CallCodes.SCAN_RESULT));
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Object obj = adapterView.getSelectedItem();
        if(obj.toString().equals())
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}