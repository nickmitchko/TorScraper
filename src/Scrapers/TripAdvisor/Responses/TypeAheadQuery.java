package Scrapers.TripAdvisor.Responses;

import Scrapers.TripAdvisor.Responses.Common.Normalization;
import Scrapers.TripAdvisor.Responses.Common.Result.QueryResult;
import Scrapers.TripAdvisor.Responses.Common.SearchQuery;

/**
 * Created  : nicholai on 3/28/17.
 * Author   : Nicholai Mitchko
 * Package  : Scrapers.TripAdvisor.Responses
 * Project Name : DistributedScraper
 */
public class TypeAheadQuery {
    private boolean       partial_content;
    private QueryResult[] results;
    private Normalization normalized;
    private SearchQuery   query;

    public QueryResult[] getResults() {
        return results;
    }

    public SearchQuery getQuery() {
        return query;
    }

    public Normalization getNormalized() {
        return normalized;
    }

    @Override
    public String toString() {
        return "";
    }
}
