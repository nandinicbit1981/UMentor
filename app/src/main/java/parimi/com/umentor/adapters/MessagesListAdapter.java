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

import parimi.com.umentor.R;
import parimi.com.umentor.models.Message;

/**
 * Created by nandpa on 10/2/17.
 */

public class MessagesListAdapter extends BaseAdapter {

    Context context;
    List<Message> messageList = new ArrayList<>();


    public MessagesListAdapter(Context context) {
        this.context = context;
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
            listView = inflater.inflate(R.layout.messages_list_item, null);
            final TextView senderText = (TextView) listView.findViewById(R.id.user_name);
            senderText.setText(messageList.get(i).getSenderName().toString());
            final Button messageButton = (Button) listView.findViewById(R.id.mentor_message);
            messageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        } else {
            listView = (View) convertView;
        }

        return listView;
    }
}
