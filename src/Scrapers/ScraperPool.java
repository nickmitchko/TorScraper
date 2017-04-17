package Scrapers;

import java.io.File;
import java.util.ArrayList;

/**
 * Created  : nicholai on 3/27/17.
 * Author   : Nicholai Mitchko
 * Package  : Scrapers
 * Project  : DistributedScraper
 */
public class ScraperPool<T extends ScraperDefault> {

    private final int                poolSize;
    private       ArrayList<String>  queryList;
    private       ArrayList<Long> locationList;

    private final Class<T> type;

    public ScraperPool(int threadSize, Class<T> type) {
        this.poolSize = threadSize;
        this.type = type;
        this.queryList = new ArrayList<>();
        this.locationList = new ArrayList<>();
    }

    public void addQuery(String query) {
        if (query != null) {
            this.queryList.add(query);
        } else {
            throw new NullPointerException("Query cannot be null");
        }
    }

    public void addGeo(long geocode){
        if(geocode > 10000 && geocode < 999999){
            this.locationList.add(geocode);
        }
    }

    public void scrape() throws IllegalAccessException, InstantiationException {
        ScraperDefault scraper = type.newInstance();
        scraper.setOutputFile(new File("out.csv"));
        scraper.startScraping();
    }

}
