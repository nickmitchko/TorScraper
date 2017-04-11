package Scrapers.TripAdvisor;

import Network.NetworkHandler;
import Network.Types.DirectConnection;
import Network.Types.TorConnection;
import Scrapers.TripAdvisor.Responses.Common.Result.QueryResult;
import Scrapers.TripAdvisor.Responses.Common.Result.Result;
import Scrapers.TripAdvisor.Responses.TypeAheadQuery;
import Scrapers.TripAdvisor.Responses.TypeAheadSearch;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.Assert.fail;

/**
 * Created  : nicholai on 3/27/17.
 * Author   : Nicholai Mitchko
 * Package  : Scrapers.TripAdvisor
 * Project  : DistributedScraper
 */
class UtilitiesTest {
    private static NetworkHandler<TorConnection>    torHandler;
    private static NetworkHandler<DirectConnection> directHandler;

    @BeforeEach
    void setUp() {
        torHandler = new NetworkHandler<>(1, TorConnection.class);
        directHandler = new NetworkHandler<>(1, DirectConnection.class);
        try {
            torHandler.startConnections();
            System.out.println("Started Tor instances");
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
        try {
            torHandler.endAllConnections();
            directHandler.endAllConnections();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void locationSearch() {
        String[] searches = new String[]{"New York", "Los Angeles", "Boston"};
        for (String s : searches) {
            try {
                TypeAheadSearch search  = Utilities.locationSearch(s, torHandler);
                Result[]        results = search.getResults();
                for (Result result : results) {
                    if (result.getUrl() == null) {
                        fail();
                    }
                    System.out.println(result.getValue() + ": " + result.getName());
                }
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }
    }

    @Test
    void querySearch(){
        long geoCode = 0; // GEO Code
        try {
            TypeAheadSearch location = Utilities.locationSearch("New York City", torHandler);
            Result[] results = location.getResults();
            geoCode = results[0].getValue();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
        String[] searches = new String[]{"Hotels", "Restaurants", "Tours"};
        for(String s : searches){
            try{
                TypeAheadQuery search  = Utilities.querySearch(s, "" + geoCode, torHandler);
                QueryResult[]  results = search.getResults();
                if (results[0].getValue() != null){
                    System.out.println(results[0].getUrl());
                } else if (results[0].getUrl() == null){
                    fail();
                }
            } catch (IOException e){
                e.printStackTrace();
                fail();
            }
        }
    }

    @Test
    void locationSearch1() {
    }

}