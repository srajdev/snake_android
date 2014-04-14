package com.android.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.model.ScoreModel;
import com.zing.snakeclassic.R;

public class SpecialAdapter extends BaseAdapter {

	//Defining the background color of rows. The row will alternate between green light and green dark.
	private String[] colors = new String[] { "#C3E4B2", "#FFDDB2" };
	private LayoutInflater mInflater;

	// The variable that will hold our text data to be tied to list.
	private ArrayList<ScoreModel> data;

	public SpecialAdapter(Context context, ArrayList<ScoreModel> items) {
		mInflater = LayoutInflater.from(context);
		this.data = items;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// A ViewHolder keeps references to children views to avoid unneccessary
		// calls
		// to findViewById() on each row.
		ViewHolder holder;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.customlist, null);

			holder = new ViewHolder();
			holder.txtplayerRank = (TextView) convertView.findViewById(R.id.txtPlayerRank);
			holder.txtplayerName = (TextView) convertView.findViewById(R.id.txtPlayerName);
			holder.txtgameMode = (TextView) convertView.findViewById(R.id.txtGameMode);
			holder.txtscore = (TextView) convertView.findViewById(R.id.txtScore);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// Bind the data efficiently with the holder.
		holder.txtplayerRank.setText(String.valueOf(data.get(position).getPlayerRank())+".");
		holder.txtplayerName.setText(data.get(position).getPlayerName());
		holder.txtgameMode.setText(data.get(position).getGameMode());
		holder.txtscore.setText(String.valueOf(data.get(position).getGameScore()));

		// Set the background color depending of odd/even colorPos result
		int colorPos = position % colors.length;
		//convertView.setBackgroundColor(colors[colorPos]);
		convertView.setBackgroundColor(Color.parseColor(colors[colorPos]));

		return convertView;
	}
	
	static class ViewHolder {
	    TextView txtplayerRank;
	    TextView txtplayerName;
	    TextView txtgameMode;
	    TextView txtscore;
	    
	}

}
