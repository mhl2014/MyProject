package um.feri.mihael.wi_finder;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.support.v4.app.AppLaunchChecker;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/*
 * Created by Mihael on 21. 06. 2016.
 */
public class AdapterNewHotSpot extends RecyclerView.Adapter<AdapterNewHotSpot.ViewHolder> {

    private List<ScanResult> hotSpots;
    private Activity ac;
    private ApplicationMy app;

    public AdapterNewHotSpot(List<ScanResult> hotSpots, Activity ac)
    {
        this.hotSpots = hotSpots;
        this.ac = ac;
        this.app = (ApplicationMy) ac.getApplication();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView textViewSSID;
        public ImageView iv;
        public ImageButton imageButtonAddNew;

        public ViewHolder(View itemView)
        {
            super(itemView);

            textViewSSID = (TextView) itemView.findViewById(R.id.listNewSSID);
            iv = (ImageView) itemView.findViewById(R.id.listNewIcon);
            imageButtonAddNew = (ImageButton) itemView.findViewById(R.id.buttonNewAdd);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_hotspot_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(AdapterNewHotSpot.ViewHolder holder, final int position) {
        final ScanResult current = hotSpots.get(position);

        holder.textViewSSID.setText(current.SSID);

        holder.imageButtonAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addDetectedIntent = new Intent(ac, AddNewActivity.class);
                addDetectedIntent.putExtra(Utilities.EXTRA_HOTSPOT_SSID, current.SSID);
                addDetectedIntent.putExtra(Utilities.EXTRA_HOTSPOT_POS, position);
                addDetectedIntent.putExtra(Utilities.EXTRA_USER_ID, app.getSignInResult().getSignInAccount().getId());

                ac.startActivityForResult(addDetectedIntent, Utilities.REQ_ADD_ITEM);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hotSpots.size();
    }

    public void removeItem(int position)
    {
        hotSpots.remove(position);
        notifyItemRemoved(position);
    }
}
