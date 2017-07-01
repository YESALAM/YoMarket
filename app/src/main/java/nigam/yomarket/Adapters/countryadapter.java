package nigam.yomarket.Adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import nigam.yomarket.getset.countrypojo;

/**
 * Created by Freeware Sys on 5/24/2017.
 */

public class countryadapter extends ArrayAdapter<countrypojo>{

    public countryadapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }
}
