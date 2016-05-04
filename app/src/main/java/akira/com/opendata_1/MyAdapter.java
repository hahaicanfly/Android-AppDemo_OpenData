package akira.com.opendata_1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Akira on 2016/4/26.
 */
public class MyAdapter extends BaseAdapter {

    private LayoutInflater myInflater;
    private ArrayList<HashMap<String, String>> myList;

    public MyAdapter(Context context, ArrayList<HashMap<String, String>> myList) {
        this.myInflater = LayoutInflater.from(context);
        this.myList = myList;
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Object getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return myList.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        viewHolder holder = null;
        if (convertView == null) {
            convertView = myInflater.inflate(R.layout.list_item, null);
            holder = new viewHolder(
                    (TextView) convertView.findViewById(R.id.tvTitle),
                    (TextView) convertView.findViewById(R.id.tvAddr),
                    (TextView) convertView.findViewById(R.id.tvTel)

            );
            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();

        }

        String title = myList.get(position).get("title");
        String Addr = myList.get(position).get("address");
        String Tel = myList.get(position).get("tel");

        holder.tvTitle.setText(title);
        holder.tvAddr.setText(Addr);
        holder.tvTel.setText(Tel);

        return convertView;
    }

    private class viewHolder {
        TextView tvTitle;
        TextView tvAddr;
        TextView tvTel;

        public viewHolder(TextView tvTitle, TextView tvAddr, TextView tvTel) {
            this.tvTitle = tvTitle;
            this.tvAddr = tvAddr;
            this.tvTel = tvTel;
        }

    }
}
