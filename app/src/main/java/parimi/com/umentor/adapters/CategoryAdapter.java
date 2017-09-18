package parimi.com.umentor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import parimi.com.umentor.R;
import parimi.com.umentor.models.Category;

/**
 * Created by nandpa on 9/17/17.
 */

public class CategoryAdapter extends BaseAdapter {

    List<Category> categories;
    Context context;

    @Override
    public int getCount() {
        return categories.size();
    }

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.categories = categoryList;
        this.context = context;
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;

        if (convertView == null) {

            gridView = new View(context);

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.category_list_item, null);

            // set value into textview
            TextView textView = (TextView) gridView.findViewById(R.id.category_name);
            textView.setText(categories.get(i).getCategory().toString());

        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }
}
