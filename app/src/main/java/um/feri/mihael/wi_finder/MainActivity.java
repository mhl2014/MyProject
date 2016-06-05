package um.feri.mihael.wi_finder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private ApplicationMy appContext;
    private Button listAllButton;
    private SignInButton signInUser;

    private GoogleApiClient apiClient;
    private GoogleSignInOptions gso;
    private GoogleSignInAccount accountInfo;

    private Toast notifyToast;

    private String notifyToastText;

    private static final int REQ_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataAll all = DataAll.getScenarij1Data(); //testni podatki

        //TextView testText = (TextView) findViewById(R.id.TestText);
        //testText.setText(all.toString());

        //ApplicationMy app = (ApplicationMy) getApplication();

        listAllButton = (Button)findViewById(R.id.buttonListAll);
        signInUser = (SignInButton)findViewById(R.id.btnSignInUser);
        appContext = new ApplicationMy(all, getApplication());

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
                startActivityForResult(signInIntent, REQ_SIGN_IN);
            }
        });

    }

    public void btnListAllClick(View v)
    {
        Intent openListAll = new Intent(this, ListAllActivity.class);
        startActivity(openListAll);
    }

   // public void btnSignInUser(View v)
   // {
   //
   // }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_SIGN_IN)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if(result.isSuccess())
            {
                accountInfo = result.getSignInAccount();
                //((TextView)findViewById(R.id.TestText)).setText(accountInfo.getDisplayName().toString());

                notifyToastText = "Welcome " + accountInfo.getDisplayName() + " !";
            }
            else
            {
                notifyToastText = "Login failed!";
            }

            notifyToast = Toast.makeText(getApplicationContext(), notifyToastText, Toast.LENGTH_SHORT);
            notifyToast.show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {

    }
}
