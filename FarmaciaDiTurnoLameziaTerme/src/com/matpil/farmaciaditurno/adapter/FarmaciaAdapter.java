package com.matpil.farmaciaditurno.adapter;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.matpil.farmaciaditurno.R;
import com.matpil.farmaciaditurno.model.Farmacia;

public class FarmaciaAdapter extends BaseAdapter {

	private Context mContext;
	private final ArrayList<Map.Entry<String, Farmacia>> mData;

	public FarmaciaAdapter(Context context, Map<String, Farmacia> mapPharm) {
		super();
		this.mContext = context;
		this.mData = new ArrayList<Map.Entry<String, Farmacia>>();
		this.mData.addAll(mapPharm.entrySet());
	}



	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.list_view_main, null);
		TextView tipoView = (TextView) convertView.findViewById(R.id.tipoPharm);
		TextView pharmView = (TextView) convertView.findViewById(R.id.descPharm);		
		Map.Entry<String, Farmacia> item = getItem(position);
		String tipo = item.getKey().startsWith("_") ? item.getKey().substring(1) : item.getKey();
		Farmacia pharm = item.getValue();
		tipoView.setText(tipo);
		pharmView.setText(pharm.getName());
		return convertView;
	}

	@Override
	public int getCount() {
		return this.mData.size();
	}

	 @Override
	    public Map.Entry<String, Farmacia> getItem(int position) {
	        return (Entry<String, Farmacia>) mData.get(position);
	    }

	@Override
	public long getItemId(int position) {
		return position;
	}
}
