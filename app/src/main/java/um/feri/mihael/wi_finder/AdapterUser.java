package um.feri.mihael.wi_finder;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;

/**
 * Created by Mihael on 21. 06. 2016.
 */
public class AdapterUser extends RecyclerView.Adapter<AdapterUser.ViewHolder> {

    private DataAll dataSet;
    Activity ac;

    private String numOfPoints;

    AdapterUser(DataAll dataSet, Activity ac)
    {
        this.dataSet = dataSet;
        this.ac = ac;

        numOfPoints = ac.getResources().getString(R.string.numOfPoints);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView textUserName;
        public TextView textUserEmail;
        public TextView textUserPoints;
        public ImageView imageUserPicture;

        public ViewHolder(View itemView) {
            super(itemView);

            textUserName = (TextView) itemView.findViewById(R.id.textViewUserName);
            textUserEmail = (TextView) itemView.findViewById(R.id.textViewUserEmail);
            textUserPoints = (TextView) itemView.findViewById(R.id.textViewUserPoints);
            imageUserPicture = (ImageView) itemView.findViewById(R.id.imageViewUserPicture);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_rowlayout, parent, false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final User current = dataSet.getUser(position);

        holder.textUserName.setText(current.getName());
        holder.textUserEmail.setText(current.getEmail());

        String pointsString = numOfPoints + String.valueOf(current.getPoints());
        holder.textUserPoints.setText(pointsString);

        if(current.getUserPhotoUri() == null || current.getUserPhotoUri().equals(""))
        {
            holder.imageUserPicture.setImageResource(R.drawable.ic_account_box_black_48dp);
        }
        else
        {
            GetImageTask getImageTask = new GetImageTask(holder.imageUserPicture);
            getImageTask.execute(current.getUserPhotoUri());
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.userSize();
    }

    private class GetImageTask extends AsyncTask<String, Void, Bitmap>{
        ImageView image;

        public GetImageTask(ImageView image)
        {
            this.image = image;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap profilePic = null;

            try
            {
                InputStream inputStream = new java.net.URL(url).openStream();
                profilePic = BitmapFactory.decodeStream(inputStream);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

            return profilePic;
        }

        protected void onPostExecute(Bitmap bm)
        {
            image.setImageBitmap(bm);
        }
    }
}
