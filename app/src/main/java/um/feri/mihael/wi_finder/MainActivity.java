package um.feri.mihael.wi_finder;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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

    //private Toast notifyToast;

    private String notifyToastText;

    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        res = getResources();

        setContentView(R.layout.activity_main);

        listAllButton = (Button)findViewById(R.id.buttonListAll);
        listAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent openListAll = new Intent(MainActivity.this, ListAllActivity.class);
                startActivity(openListAll);
            }
        });

        signInUser = (SignInButton)findViewById(R.id.btnSignInUser);
        signInUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                /*
                * Pri prijavi sprva preverimo, ce imamo spletno povezano, ce jo imamo izvedemo login, drugace uporabnika
                * obvestimo, da mora vzpostaviti povezavo s spletom!
                */

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if(networkInfo != null && networkInfo.isConnectedOrConnecting())
                {
                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
                    startActivityForResult(signInIntent, Utilities.REQ_SIGN_IN);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), res.getString(R.string.connectionRequired), Toast.LENGTH_LONG).show();
                }
            }
        });

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Utilities.REQ_SIGN_IN)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if(result.isSuccess())
            {
                accountInfo = result.getSignInAccount();
                notifyToastText = res.getString(R.string.welcome) + accountInfo.getDisplayName() + " !";
            }
            else
            {
                notifyToastText = res.getString(R.string.loginFailure);
            }

            Toast.makeText(getApplicationContext(), notifyToastText, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        Toast.makeText(this, res.getString(R.string.loginFailure) + ": " + connectionResult.getErrorMessage(), Toast.LENGTH_LONG)
                .show();
    }
}
