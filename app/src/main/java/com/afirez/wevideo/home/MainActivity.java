package com.afirez.wevideo.home;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.MenuItem;

import com.afirez.wevideo.R;
import com.afirez.wevideo.common.BaseActivity;
import com.afirez.wevideo.common.Fragments;

public class MainActivity extends BaseActivity {


    private DrawerLayout dlDrawer;
    private NavigationView nvNavigation;
    private ActionBarDrawerToggle drawerToggle;
    private MenuItem tabPre;
    private FragmentManager fm;
    private Fragment currentFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.wv_activity_main;
    }

    @Override
    protected void initView() {
        setCommonSupportActionBar();
        setTitle(R.string.wv_main_home_title);
        dlDrawer = (DrawerLayout) findViewById(R.id.wv_main_dl_drawer);
        drawerToggle = new ActionBarDrawerToggle(
                this,
                dlDrawer,
                toolbar,
                R.string.wv_main_drawer_open,
                R.string.wv_main_drawer_close
        );
        dlDrawer.addDrawerListener(drawerToggle);
        setNavigationIcon(R.drawable.wv_main_ic_home);
        drawerToggle.syncState();

        nvNavigation = (NavigationView) findViewById(R.id.wv_main_nv_navigation);
        tabPre = nvNavigation.getMenu().getItem(0);
        tabPre.setChecked(true);
        switchFragment(HomeFragment.class);
        nvNavigation.setNavigationItemSelectedListener(onNavigationItemSelectedListener);
    }

    private NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (tabPre != null) {
                tabPre.setChecked(false);
            }
            int itemId = item.getItemId();
            switch (itemId) {
                case R.id.wv_main_navigation_item_video:
                    switchFragment(HomeFragment.class);
                    if (toolbar != null) {
                        toolbar.setTitle(R.string.wv_main_home_title);
                    }
                    break;
                case R.id.wv_main_navigation_item_blog:
                    switchFragment(BlogFragment.class);
                    if (toolbar != null) {
                        toolbar.setTitle(R.string.wv_main_blog_title);
                    }
                    break;
                case R.id.wv_main_navigation_item_about:
                    switchFragment(AboutFragment.class);
                    if (toolbar != null) {
                        toolbar.setTitle(R.string.wv_main_about_title);
                    }
                    break;
            }

            tabPre = item;
            dlDrawer.closeDrawer(Gravity.START);
            return true;
        }
    };

    private void switchFragment(Class<? extends Fragment> clazz) {
        if (fm == null) {
            fm = getSupportFragmentManager();
        }
        String tag = clazz.getName();
        Fragment fragment = fm.findFragmentByTag(tag);
        FragmentTransaction transaction = fm.beginTransaction();
        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }
        if (fragment == null) {
            transaction.add(
                    R.id.wv_main_fl_content,
                    Fragments.getInstance().newInstance(clazz),
                    tag
            );
        } else {
            transaction.show(fragment);
        }
        transaction.commitAllowingStateLoss();
        currentFragment = fragment;
    }

    @Override
    protected void initData() {

    }
}
