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
import parimi.com.umentor.NotificationsClickInterface;
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
    NotificationsClickInterface notificationsClickInterface;

    public NotificationAdapter(Context context, List<Notification> notifications, ButtonClickInterface buttonClickListener, NotificationsClickInterface notificationsClickInterface) {
        this.notifications = notifications;
        this.context = context;
        this.buttonClickListener = buttonClickListener;
        this.notificationsClickInterface = notificationsClickInterface;
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
        NotificationsViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.notifications_list_item, viewGroup, false);
            viewHolder = new NotificationsViewHolder();
            viewHolder.titleText =  convertView.findViewById(R.id.title);
            viewHolder.messageText = convertView.findViewById(R.id.message);
            viewHolder.acceptButton = convertView.findViewById(R.id.accept);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (NotificationsViewHolder) convertView.getTag();
        }

        viewHolder.titleText.setText(notifications.get(i).getTitle());
        viewHolder.messageText.setText(notifications.get(i).getMessage());

        if(notifications.get(i).getNotificationType().equals(NotificationType.REQUEST)) {
            convertView.findViewById(R.id.notification_actions).setVisibility(View.VISIBLE);
        } else {
            convertView.findViewById(R.id.message).setVisibility(View.VISIBLE);
        }


        viewHolder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClickListener.onRequestAccepted(notifications.get(i), Constants.ACCEPT);
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationsClickInterface.onItemClicked(notifications.get(i).getSender(),
                            notifications.get(i).getNotificationType());
            }
        });

        return convertView;
    }

    public void setNotificationsList(List<Notification> notifications) {
        this.notifications = notifications;
        notifyDataSetChanged();
    }

    public class NotificationsViewHolder {
        TextView titleText;
        TextView messageText;
        Button acceptButton;
    }
}
