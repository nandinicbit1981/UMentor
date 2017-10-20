package parimi.com.umentor.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import parimi.com.umentor.R;
import parimi.com.umentor.helper.UMentorHelper;
import parimi.com.umentor.models.Message;

/**
 * Created by nandpa on 9/23/17.
 */

public class MessageAdapter extends ArrayAdapter<Message> {

    Context context;
    List<Message> messageList = new ArrayList<>();
    String currentUserId;


    public MessageAdapter(Context context, List<Message> messages) {
        super(context, 0, messages);
        this.messageList = messages;
        this.context = context;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    @Override
    public int getCount() {
        return this.messageList.size();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        MessagesViewHolder viewHolder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.message_list_item, viewGroup, false);
                viewHolder = new MessagesViewHolder();
                viewHolder.senderMessageTxt = (TextView) convertView.findViewById(R.id.sender_message);
                viewHolder.messageItem = convertView.findViewById(R.id.message_item);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (MessagesViewHolder) convertView.getTag();
            }

            viewHolder.senderMessageTxt.setText(String.valueOf(this.messageList.get(i).getMessage()).toString());

            if(this.messageList.get(i).getSenderId().equals(currentUserId)) {
                viewHolder.senderMessageTxt.setBackground(ContextCompat.getDrawable(context, R.drawable.chat_background));
                viewHolder.messageItem.setGravity(Gravity.RIGHT);
            } else {
                viewHolder.senderMessageTxt.setBackground(ContextCompat.getDrawable(context, R.drawable.chat_background_other));
                viewHolder.messageItem.setGravity(Gravity.LEFT);
            }


            final View finalConvertView = convertView;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UMentorHelper.hideKeyboard(context, finalConvertView);
                }
            });


        return convertView;
    }

    public class MessagesViewHolder {
        TextView senderMessageTxt;
        LinearLayout messageItem;
    }
}
