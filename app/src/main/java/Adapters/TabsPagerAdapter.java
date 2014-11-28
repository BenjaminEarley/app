package Adapters;


import me.gostalk.stalkme.Tab1Fragment;
import me.gostalk.stalkme.Tab2Fragment;
import me.gostalk.stalkme.Tab3Fragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Notifications fragment activity
                return new Tab1Fragment();
            case 1:
                // Friends fragment activity
                return new Tab2Fragment();
            case 2:
                // Map fragment activity
                return new Tab3Fragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}
