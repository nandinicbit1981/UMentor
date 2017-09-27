package parimi.com.umentor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import parimi.com.umentor.ButtonClickInterface;
import parimi.com.umentor.R;
import parimi.com.umentor.helper.Constants;
import parimi.com.umentor.helper.NotificationType;
import parimi.com.umentor.models.Notification;

/**
 * Created by nandpa on 9/27/17.
 */

public class NotificationAdapter extends BaseAdapter {

    private final Context context;
    List<Notification> notifications = new ArrayList<>();
    ButtonClickInterface buttonClickListener;

    public NotificationAdapter(Context context, List<Notification> notifications, ButtonClickInterface buttonClickListener) {
        this.notifications = notifications;
        this.context = context;
        this.buttonClickListener = buttonClickListener;
    }

    @Override
    public int getCount() {
        return notifications.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listView;
        if (convertView == null) {
            listView = inflater.inflate(R.layout.notifications_list_item, null);
            final TextView titleText = (TextView) listView.findViewById(R.id.title);
            titleText.setText(notifications.get(i).getTitle());
            if(notifications.get(i).getNotificationType().equals(NotificationType.REQUEST)) {
                listView.findViewById(R.id.notification_actions).setVisibility(View.VISIBLE);
            }
            Button acceptButton = listView.findViewById(R.id.accept);
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buttonClickListener.onRequestAccepted(notifications.get(i), Constants.ACCEPT);
                }
            });
        } else {
            listView = (View) convertView;
        }
        return listView;
    }

    public void setNotificationsList(List<Notification> notifications) {
        this.notifications = notifications;
        notifyDataSetChanged();
    }
}
