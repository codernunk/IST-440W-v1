package com.example.jesse.ist440W;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.jesse.ist440W.models.App;
import com.example.jesse.ist440W.models.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailsActivity extends AppCompatActivity {

    Toolbar _toolbar;
    TabLayout _tabLayout;
    ViewPager _viewPager;

    private Recipe _currentRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        _toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(_toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        _viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(_viewPager);

        _tabLayout = (TabLayout) findViewById(R.id.tabs);
        _tabLayout.setupWithViewPager(_viewPager);

        // Get the recipe
        Intent intent = getIntent();

        if (intent != null){
            _currentRecipe = (Recipe)intent.getSerializableExtra("Recipe");
            Log.d(App.LOG_TITLE, "Retrieved " + _currentRecipe.getName());
        }

    }

    public Recipe getCurrentRecipe() { return _currentRecipe; }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new RecipeDetailsFragment(), "Overview");
        adapter.addFragment(new RecipeIngredientsFragment(), "Ingredients");
        adapter.addFragment(new CookingDirectionsFragment(), "Directions");
        adapter.addFragment(new RecipeShoppingListFragment(), "Shopping List");
        viewPager.setAdapter(adapter);
    }

    /**
     * Inner class that helps with setting up the tabs
     */
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
