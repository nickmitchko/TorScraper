package Scrapers.TripAdvisor.Responses.Common.Result;

/**
 * Created  : nicholai on 3/28/17.
 * Author   : Nicholai Mitchko
 * Package  : Scrapers.TripAdvisor.Responses.Common
 * Project Name : DistributedScraper
 */
public class QueryResult {
    private int           indexInType;
    private String        lookbackServlet;
    private String        autobroadened;
    private String        title;
    private String        type;
    private String        url;
    private ResponseUrl[] urls;
    private String        highlighted_name;
    private String        scope;
    private String        name;
    private String        data_type;
    private String        airportCode;
    private String        value;
    private String        coords;

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getAirportCode() {
        return airportCode;
    }

    public String getCoords() {
        return coords;
    }

    @Override
    public String toString() {
        return this.getName() + " @ " + this.getUrl();
    }

    public Object getValue() {
        return value;
    }
}
