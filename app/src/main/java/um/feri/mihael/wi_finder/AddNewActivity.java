package um.feri.mihael.wi_finder;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    private Spinner spinnerAccessibility;

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
                    Intent scanIntent = new Intent(Utilities.REQ_SCAN);
                    scanIntent.putExtra(Utilities.SCAN_MODE, Utilities.QR_CODE_MODE);
                    startActivityForResult(scanIntent, Utilities.ACTION_SCAN);
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
                addNewIntent.putExtra(Utilities.RETURN_HOTSPOT_SSID, addSSID.getText().toString());
                addNewIntent.putExtra(Utilities.RETURN_HOTSPOT_SEC_KEY, addSecKey.getText().toString());
                addNewIntent.putExtra(Utilities.RETURN_HOTSPOT_ACCESS, accessLevel);

                setResult(Activity.RESULT_OK, addNewIntent);
                finish();
            }
        });

        spinnerAccessibility = (Spinner) findViewById(R.id.activityAddNewSpinnerAccess);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.accessibility, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAccessibility.setAdapter(adapter);
        spinnerAccessibility.setOnItemSelectedListener(this);
    }

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == Utilities.ACTION_SCAN)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                addSecKey.setText(data.getStringExtra(Utilities.SCAN_RESULT));
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {
        String selectedItem = adapterView.getSelectedItem().toString();

        if(selectedItem.equals(res.getString(R.string.publicAccess)))
        {
            accessLevel = HotSpot.Accessibility.PUBLIC.name();
        }
        else if(selectedItem.equals(res.getString(R.string.loginAccess)))
        {
            accessLevel = HotSpot.Accessibility.LOGIN.name();
        }
        else if(selectedItem.equals(res.getString(R.string.secureAccess)))
        {
            accessLevel = HotSpot.Accessibility.SECURE.name();
        }
        else if(selectedItem.equals(res.getString(R.string.inaccessibleAccess)))
        {
            accessLevel = HotSpot.Accessibility.INACCESSIBLE.name();
        }
        else if(selectedItem.equals(res.getString(R.string.privateAccess)))
        {
            accessLevel = HotSpot.Accessibility.PRIVATE.name();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}