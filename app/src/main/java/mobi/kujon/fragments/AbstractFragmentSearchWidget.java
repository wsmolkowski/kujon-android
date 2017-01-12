package mobi.kujon.fragments;

import android.animation.LayoutTransition;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.LinearLayout;

import com.github.underscore.$;
import com.github.underscore.Predicate;

import java.util.ArrayList;
import java.util.List;

import mobi.kujon.R;

/**
 *
 */

public abstract class AbstractFragmentSearchWidget<T> extends ListFragment {
    protected List<T> dataFromApi = new ArrayList<>();
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();
        LinearLayout searchBar = (LinearLayout) search.findViewById(R.id.search_bar);
        searchBar.setLayoutTransition(new LayoutTransition());
        Drawable drawable = menu.findItem(R.id.search).getIcon();
        if(drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search.clearFocus();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length()==0){
                    setDataToAdapter(dataFromApi);
                    return true;
                }
                setDataToAdapter(performFiltering(newText));
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    protected List<T> performFiltering(String query) {
        Predicate<T> pred =createPredicate(query);
        return $.filter(dataFromApi, pred);
    }

    protected abstract void setDataToAdapter(List<T> filter);

    protected abstract Predicate<T> createPredicate(String query);
}
