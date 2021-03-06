package um.feri.mihael.wi_finder;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

public class ListAllUsersActivity extends AppCompatActivity {

    private Resources res;
    private ApplicationMy app;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_all_users);

        res = getResources();
        app = (ApplicationMy) getApplication();

        recyclerView = (RecyclerView) findViewById(R.id.listAllUsersView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if(!app.loadData())
        {
            Toast.makeText(this, res.getString(R.string.loadFailure), Toast.LENGTH_SHORT).show();
        }

        adapter = new AdapterUser(app.getAll(), this);
        recyclerView.setAdapter(adapter);
    }
}
