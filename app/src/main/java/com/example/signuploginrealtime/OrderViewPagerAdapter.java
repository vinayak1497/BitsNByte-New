package com.example.signuploginrealtime;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class OrderViewPagerAdapter extends FragmentStateAdapter {
    public OrderViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new OngoingOrderFragment();
            case 1: return new OrderAcceptedFragment();
            case 2: return new OrderCompletedFragment();
            default: return new OngoingOrderFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}