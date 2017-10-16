package parimi.com.umentor.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import parimi.com.umentor.R;
import parimi.com.umentor.models.Message;

/**
 * Created by nandpa on 9/23/17.
 */

public class MessageAdapter extends BaseAdapter {

    Context context;
    List<Message> messageList = new ArrayList<>();
    String currentUserId;


    public MessageAdapter(Context context, String currentUserId) {

        this.context = context;
        this.currentUserId = currentUserId;
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void setMessageLlist(List<Message> messageList) {

        notifyDataSetInvalidated();
        this.messageList = messageList;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listView;

        if (convertView == null) {
            listView = inflater.inflate(R.layout.message_list_item, null);
            final LinearLayout messageItem = listView.findViewById(R.id.message_item);
            if(messageList.get(i).getSenderId().equals(currentUserId)) {
                messageItem.setGravity(Gravity.RIGHT);
            } else {
                messageItem.setGravity(Gravity.LEFT);
            }
            final TextView senderMessageText = (TextView) listView.findViewById(R.id.sender_message);
            senderMessageText.setText(String.valueOf(messageList.get(i).getMessage()).toString());

        } else {
            listView = (View) convertView;
        }

        return listView;
    }
}
