package com.example.networkinfoapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class InfoAdapter extends ArrayAdapter<String> {

    private final String[] values;
    private final String[] wifiLabels = {"Connection type: ", "IP Address: ", "Link Speed: ", "Network Id: ",
    "RSSI: ", "SSID: ", "Hidden SSID: ", " BSSID: ", "Max Download Speed: ", "Max Upload Speed: "};
    private final String[] mobileLabels = {"Connection type: ", "Cell id: ", "Cell MCC: ", "Cell MNC: ",
            "Cell PCI: ", "Cell TAC: ", "RSRP: ", "Max Download Speed: ", "Max Upload Speed: "};
    public InfoAdapter(@NonNull Context context, int resource, @NonNull String[] values) {
        super(context, resource, values);
        this.values = values;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return values[position];
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.wifi_layout, parent, false);
        TextView label = convertView.findViewById(R.id.label);
        TextView value = convertView.findViewById(R.id.value);
        if(values[0].equals("WIFI")) {
            label.setText(wifiLabels[position]);
        }
        else if(values[0].equals("MOBILE")){
            label.setText(mobileLabels[position]);
        }
        value.setText(getItem(position));
        return convertView;

    }
}
