package parimi.com.umentor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import parimi.com.umentor.R;
import parimi.com.umentor.models.User;

/**
 * Created by nandpa on 9/23/17.
 */

public class MentorListAdapter extends BaseAdapter {

    Context context;
    List<User> usersLlist = new ArrayList<>();

    public MentorListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return usersLlist.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void setUsersLlist(List<User> usersLlist) {
        this.usersLlist = usersLlist;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listView;

        if (convertView == null) {
            listView = inflater.inflate(R.layout.users_list_item, null);
            final TextView nameText = (TextView) listView.findViewById(R.id.name);
            final TextView experienceText = (TextView) listView.findViewById(R.id.experience);
            nameText.setText(usersLlist.get(i).getName().toString());
            experienceText.setText(String.valueOf(usersLlist.get(i).getExperience()));
        } else {
            listView = (View) convertView;
        }

        return listView;
    }
}
