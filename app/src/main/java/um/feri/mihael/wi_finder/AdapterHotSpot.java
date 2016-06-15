package um.feri.mihael.wi_finder;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Mihael on 30. 05. 2016.
 */

public class AdapterHotSpot extends RecyclerView.Adapter<AdapterHotSpot.ViewHolder> {

    private DataAll dataSet;
    Activity ac;

    public AdapterHotSpot(DataAll dataSet, Activity ac)
    {
        this.dataSet = dataSet;
        this.ac = ac;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView txtSSID;
        public TextView txtSecKey;
        public ImageView iv;
       // public Button showOnMapButton;

        public ViewHolder(View itemView) {
            super(itemView);

            txtSSID = (TextView) itemView.findViewById(R.id.listSSID);
            txtSecKey = (TextView) itemView.findViewById(R.id.listSecKey);
            iv = (ImageView) itemView.findViewById(R.id.listIcon);
     //       showOnMapButton = (Button) itemView.findViewById(R.id.buttonShowOnMap);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            HotSpot hotSpot = dataSet.getHotSpot(getAdapterPosition());
            User finder = hotSpot.getUser();

            Intent interactActivity = new Intent (ac, EditHotSpotActivity.class);

            interactActivity.putExtra(Utilities.EXTRA_HOTSPOT_POS, getAdapterPosition());
            interactActivity.putExtra(Utilities.EXTRA_HOTSPOT_SSID, this.txtSSID.getText());
            interactActivity.putExtra(Utilities.EXTRA_HOTSPOT_SEC_KEY, this.txtSecKey.getText());
            interactActivity.putExtra(Utilities.EXTRA_HOTSPOT_LATITUDE, hotSpot.getLatitude());
            interactActivity.putExtra(Utilities.EXTRA_HOTSPOT_LONGITUDE, hotSpot.getLongitude());
            interactActivity.putExtra(Utilities.EXTRA_HOTSPOT_ACCESS, hotSpot.getAccessLevel().name());
            interactActivity.putExtra(Utilities.EXTRA_USER_NAME, finder.getName());
            interactActivity.putExtra(Utilities.EXTRA_USER_EMAIL, finder.getEmail());

            //ac.startActivity(interactActivity);
            ac.startActivityForResult(interactActivity, Utilities.REQ_EDIT_OR_DEL_ITEM);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout, parent, false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(AdapterHotSpot.ViewHolder holder, int position) {
        HotSpot current = dataSet.getHotSpot(position);

        holder.txtSSID.setText(current.getSsid());
        holder.txtSecKey.setText(current.getSecurityKey());

        holder.iv.setImageResource(R.drawable.wifi_signal_normal_temp);
/*
        holder.showOnMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewMap = new Intent(ac, ViewLocationActivity.class);
                ac.startActivity(viewMap);
            }
        });
        */

    }

    @Override
    public int getItemCount() {
        return dataSet.hotSpotSize();
    }

    public void updateItem(int pos, String newSSID, String newSecKey, double latitude, double longitude, HotSpot.Accessibility accessibility)
    {
        dataSet.updateHotSpot(pos, newSSID, newSecKey, latitude, longitude, accessibility);
        notifyItemChanged(pos);
    }

    public void deleteItem(int pos)
    {
        dataSet.deleteHotSpot(pos);
        notifyItemRemoved(pos);
    }

    public void addItem(HotSpot hs)
    {
        dataSet.addHotSpot(hs);
        notifyItemInserted(dataSet.hotSpotSize() - 1);
    }
}
