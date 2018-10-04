package ch.hsr.winescore.ui.adapters;

import android.arch.paging.DataSource;
import ch.hsr.winescore.model.Wine;

public class WineDataSourceFactory extends DataSource.Factory<Integer, Wine> {

    @Override
    public DataSource<Integer, Wine> create() {
        return new WinePositionalDataSource();
    }
}
