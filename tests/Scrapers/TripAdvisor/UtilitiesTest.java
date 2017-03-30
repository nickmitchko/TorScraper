package Scrapers.TripAdvisor;

import Network.NetworkConnection;
import Network.NetworkHandler;
import Network.Types.DirectConnection;
import Network.Types.TorConnection;
import Scrapers.TripAdvisor.Responses.Common.Result.Result;
import Scrapers.TripAdvisor.Responses.LocationSearch;
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
    private NetworkHandler<TorConnection> torHandler;
    private NetworkHandler<DirectConnection> directHandler;

    @BeforeEach
    void setUp() {
        torHandler = new NetworkHandler<>(1);
        directHandler = new NetworkHandler<>(1);
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
        try {
            NetworkConnection proxy = torHandler.createNextConnection(TorConnection.class);
            LocationSearch search = Utilities.locationSearch("New York", proxy.getConnection());
            Result[] results = search.getResults();
            for (Result result : results) {
                if (result.getUrl() == null) {
                    fail();
                }
            }
            // If you didn't error the test passed
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void locationSearch1() {
    }

}