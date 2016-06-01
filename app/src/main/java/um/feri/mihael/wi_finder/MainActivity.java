package um.feri.mihael.wi_finder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ApplicationMy appContext;
    private Button listAllButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataAll all = DataAll.getScenarij1Data(); //testni podatki

        //TextView testText = (TextView) findViewById(R.id.TestText);
        //testText.setText(all.toString());

        //ApplicationMy app = (ApplicationMy) getApplication();

        listAllButton = (Button)findViewById(R.id.buttonListAll);
        appContext = new ApplicationMy(all, getApplication());
    }

    public void btnListAllClick(View v)
    {
        Intent openListAll = new Intent(this, ListAllActivity.class);
        startActivity(openListAll);
    }
}
