package Scrapers.TripAdvisor;

import Network.NetworkHandler;
import Network.Types.TorConnection;
import Scrapers.ScraperDefault;
import Scrapers.TripAdvisor.Responses.TypeAheadQuery;
import Scrapers.TripAdvisor.Responses.TypeAheadSearch;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created  : nicholai on 4/11/17.
 * Author   : Nicholai Mitchko
 * Package  : Scrapers.TripAdvisor
 * Project Name : DistributedScraper
 */
class TripAdvisorScraperTest {

    private TripAdvisorScraper scraper;
    private NetworkHandler<TorConnection> torConnectionNetworkHandler = new NetworkHandler<>(1, TorConnection.class);
    private ArrayList<String> attractionList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        try {
            torConnectionNetworkHandler.startConnections();
            Utilities.setNetworkHandler(torConnectionNetworkHandler);
            TypeAheadSearch searchResult = Utilities.locationSearch("Boston, MA");
            long            geoCode      = searchResult.getResults()[0].getValue();
            TypeAheadQuery  queryResult  = Utilities.querySearch("Hotels", "" + geoCode);
            String          url          = queryResult.getResults()[4].getUrl();
            scraper = new TripAdvisorScraper(url);
            attractionList = Utilities.extractListingUrls(url, 0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        scraper.endScraping(false);
        try {
            torConnectionNetworkHandler.endAllConnections();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void startScraping() {
        scraper.setOutputFormat(ScraperDefault.OUTPUT_FORMAT.CSV);
        scraper.setOutputFile(new File("test.csv"));
        scraper.startScraping();
        try {
            scraper.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void saveFile() {
    }

}