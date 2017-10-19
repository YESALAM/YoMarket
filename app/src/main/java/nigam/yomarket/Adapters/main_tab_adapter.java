package nigam.yomarket.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import nigam.yomarket.Home_frag;
import nigam.yomarket.Post_Frag;
import nigam.yomarket.notification_frag;
import nigam.yomarket.phonebook_frag;

/**
 * Created by alokit nigam on 5/7/2017.
 */

public class main_tab_adapter extends FragmentPagerAdapter {
    int mNumOfTabs;
    public main_tab_adapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Post_Frag tab1 = new Post_Frag();
                return tab1;
            case 2:
                notification_frag tab2 = new notification_frag();
                return tab2;
            case 1:
                phonebook_frag tab3 = new phonebook_frag();
                return tab3;
            case 3:
                Home_frag tab4 = new Home_frag();
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0:
                return "Home";
            case 2:
                return "notification";
            case 1:
                return "Phonebook";
            case 3:
                return "MyPosts";

        }
        return null;
    }
}
