package nyc.c4q.take2.accessfood;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;


public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    List<Information> data = Collections.emptyList();

    public NavigationDrawerAdapter(Context context, List<Information> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    //delete the item in the recyclerView.
    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.navigation_drawer_row_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Information current = data.get(position);
        holder.title.setText(current.title);
        holder.icon.setImageResource(current.iconId);
        holder.navigationIcon = current.iconId;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        ImageView icon;
        int navigationIcon;

        MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.listText);
            icon = (ImageView) itemView.findViewById(R.id.listIcon);
            icon.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (navigationIcon) {
                case R.drawable.ic_profile:
                    //replace the container with profile fragment
                    Toast.makeText(v.getContext(), "profile clicked in navigation drawer", Toast.LENGTH_SHORT).show();
                    break;

                case R.drawable.ic_yourplace:
                    //replace container with yourplace fragment
                    Toast.makeText(v.getContext(), "yourplace clicked in navigation drawer", Toast.LENGTH_SHORT).show();
                    break;

                case R.drawable.ic_settings:
                    //replace container with settings fragment
                    Toast.makeText(v.getContext(), "settings clicked in navigation drawer", Toast.LENGTH_SHORT).show();
                    break;

                case R.drawable.ic_logout:
                    //logout from the servers etc, and replace with home screen fragment
                    Toast.makeText(v.getContext(), "logout clicked in navigation drawer", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    }

    public static class Information {

        int iconId;
        String title;
    }

}
