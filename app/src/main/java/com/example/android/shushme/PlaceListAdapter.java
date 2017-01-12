package com.example.android.shushme;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.shushme.provider.PlaceContract;

public class PlaceListAdapter extends RecyclerView.Adapter<PlaceListAdapter.PlaceViewHolder> {

    // Holds on to the cursor to display the list of places
    private Cursor mCursor;
    private Context mContext;
    final private ListItemClickListener mOnClickListener;

    /**
     * Constructor using the context and the db cursor
     * @param context the calling context/activity
     */
    public PlaceListAdapter(Context context, ListItemClickListener listener) {
        this.mContext = context;
        mOnClickListener = listener;

    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get the RecyclerView item layout
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_place_card, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        // Move the mCursor to the position of the item to be displayed
        if (!mCursor.moveToPosition(position))
            return; // bail if returned null

        // Update the view holder with the information needed to display
        String name = mCursor.getString(mCursor.getColumnIndex(PlaceContract.PlaceEntry.COLUMN_PLACE_NAME));
        String address = mCursor.getString(mCursor.getColumnIndex(PlaceContract.PlaceEntry.COLUMN_PLACE_ADDRESS));
        long id = mCursor.getLong(mCursor.getColumnIndex(PlaceContract.PlaceEntry._ID));

        // Display the place name and address
        holder.nameTextView.setText(name);
        holder.addressTextView.setText(address);
        // Add the the place id s a tag
        holder.itemView.setTag(id);
    }


    @Override
    public int getItemCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    /**
     * Swaps the Cursor currently held in the adapter with a new one
     * and triggers a UI refresh
     *
     * @param newCursor the new cursor that will replace the existing one
     */
    public void swapCursor(Cursor newCursor) {
        // Always close the previous mCursor first
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    public interface ListItemClickListener {
        void onListItemClick(long placeID);
    }


    class PlaceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nameTextView;
        TextView addressTextView;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.name_text_view);
            addressTextView = (TextView) itemView.findViewById(R.id.address_text_view);
        }

        @Override
        public void onClick(View v) {
            long id = (long) itemView.getTag();
            mOnClickListener.onListItemClick(id);
        }

    }
}