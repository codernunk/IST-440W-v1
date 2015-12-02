package com.example.jesse.ist440W;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.jesse.ist440W.models.App;
import com.example.jesse.ist440W.models.Recipe;
import com.example.jesse.ist440W.services.SyncService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar _toolbar;
    private ViewPager _viewPager;
    private TabLayout _tabLayout;
    private Button _addRecipe;

    private RecipeListFragment _recipeListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(_toolbar);

        _addRecipe = (Button) findViewById(R.id.btnAddRecipe);
        _addRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), AddRecipeActivity.class);
                startActivity(i);
            }
        });

        App.getInstance().init(this);

        _viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(_viewPager);

        _tabLayout = (TabLayout) findViewById(R.id.tabs);
        _tabLayout.setupWithViewPager(_viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new RecipeListFragment(), "Recipes");
        adapter.addFragment(new ShoppingListFragment(), "Shopping Lists");

        _recipeListFragment = (RecipeListFragment)adapter.getItem(0);
        viewPager.setAdapter(adapter);
    }

    //http://romannurik.github.io/AndroidAssetStudio/icons-actionbar.html#source.type=clipart&source.space.trim=0&source.space.pad=-0.1&source.clipart=res%2Fclipart%2Ficons%2Faction_search.svg&name=ic_action_action_search&theme=light&color=33b5e5%2C60
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //_recipeListFragment.filterRecipes(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                _recipeListFragment.filterRecipes(newText);
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                _recipeListFragment.resetFilteredRecipes();
                return false;
            }
        });

        MenuItem syncItem = menu.findItem(R.id.action_sync);
        syncItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                SyncService.SyncRecipes(App.getInstance().getRecipes().toArray(new Recipe[App.getInstance().getRecipes().size()]));
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

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