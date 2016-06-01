package um.feri.mihael.wi_finder;

import android.app.Application;

/**
 * Created by Mihael on 30. 05. 2016.
 */
public class ApplicationMy{
    private DataAll all;
    private Application app;

    public ApplicationMy()
    {
        this.app = new Application();
        this.all = new DataAll();
    }

    public ApplicationMy(DataAll all, Application app)
    {
        this.all = all;
        this.app = app;
    }

    public DataAll getAll()
    {
        return all;
    }

    private void setAll(DataAll all)
    {
        this.all = all;
    }

    public Application getApp() {
        return app;
    }

    public void setApp(Application app) {
        this.app = app;
    }
}
