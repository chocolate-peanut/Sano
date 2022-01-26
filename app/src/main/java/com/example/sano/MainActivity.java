package com.example.sano;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

public class MainActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize & assign variable[menu bar]
        MeowBottomNavigation bottomNavigation = findViewById(R.id.bottom_navigation);
        //add menu item
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_icons8_home));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_note));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_goal));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_analysis));
        bottomNavigation.add(new MeowBottomNavigation.Model(5, R.drawable.ic_setting));

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                //initialize fragment
                Fragment fragment;
                //check condition
                switch (item.getId()) {
                    case 1:
                        fragment = new calendarFragment();
                        break;
                    case 2:
                        fragment = new DiaryFragment();
                        break;
                    case 3:
                        fragment = new GoalFragment();
                        break;
                    case 4:
                        fragment = new AnalysisFragment();
                        break;
                    case 5:
                        fragment = new SettingFragment();
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + item.getId());
                }
                loadFragment(fragment);
            }
        });

        bottomNavigation.show(1, true);

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {

            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {

            }
        });
    }//end onCreate

    private void loadFragment(Fragment fragment) {
        //replace fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();
    }//end loadFragment
}