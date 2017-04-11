package Scrapers.TripAdvisor.Responses;

import Scrapers.TripAdvisor.Responses.Common.Normalization;
import Scrapers.TripAdvisor.Responses.Common.Query;
import Scrapers.TripAdvisor.Responses.Common.Result.Result;

/**
 * Created  : nicholai on 3/28/17.
 * Author   : Nicholai Mitchko
 * Package  : Scrapers.TripAdvisor.Responses
 * Project Name : DistributedScraper
 */
public class TypeAheadSearch {
    private boolean       partial_content;
    private Result[]      results;
    private Normalization normalized;
    private Query         query;

    public Result[] getResults(){
        return results;
    }

    public Query getQuery(){
        return query;
    }

    public Normalization getNormalized() {
        return normalized;
    }

    @Override
    public String toString(){
        return "";
    }
}
