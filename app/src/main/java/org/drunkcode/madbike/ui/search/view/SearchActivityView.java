package org.drunkcode.madbike.ui.search.view;

import org.drunkcode.madbike.base.BaseView;
import org.drunkcode.madbike.ui.search.response.SearchResponse;

public interface SearchActivityView extends BaseView{

    void getSearchResponse(SearchResponse searchResponse);
}
