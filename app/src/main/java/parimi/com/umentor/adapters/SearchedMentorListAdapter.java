package parimi.com.umentor.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import parimi.com.umentor.R;
import parimi.com.umentor.models.User;
import parimi.com.umentor.views.activity.MainActivity;
import parimi.com.umentor.views.fragment.ProfileFragment;

import static parimi.com.umentor.helper.CommonHelper.decodeFromFirebaseBase64;
import static parimi.com.umentor.helper.Constants.USER;

/**
 * Created by nandpa on 9/23/17.
 */

public class SearchedMentorListAdapter extends BaseAdapter {

    Context context;
    List<User> usersLlist = new ArrayList<>();


    public SearchedMentorListAdapter(Context context) {
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
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listView;

        if (convertView == null) {
            listView = inflater.inflate(R.layout.users_list_item, null);
            final TextView nameText = (TextView) listView.findViewById(R.id.name);
            final TextView experienceText = (TextView) listView.findViewById(R.id.experience);
            final TextView jobText = (TextView) listView.findViewById(R.id.job);
            final ImageView imageView = (ImageView) listView.findViewById(R.id.imageView);
            try {

                Bitmap bitmap = decodeFromFirebaseBase64(usersLlist.get(i).getProfilePic());
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            nameText.setText(usersLlist.get(i).getName().toString());
            experienceText.setText(String.valueOf(usersLlist.get(i).getExperience()) + context.getString(R.string.yrs));
            jobText.setText(usersLlist.get(i).getJob());
        } else {
            listView = (View) convertView;
        }

        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ProfileFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(USER, usersLlist.get(i));
                fragment.setArguments(bundle);
                ((MainActivity)context).insertFragment(fragment);
            }
        });

        return listView;
    }
}
