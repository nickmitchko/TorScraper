package Network;

import Network.Types.DirectConnection;
import Network.Types.TorConnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import static org.junit.Assert.fail;

/**
 * Created  : nicholai on 3/27/17.
 * Author   : Nicholai Mitchko
 * Package  : Network
 * Project  : DistributedScraper
 */
class NetworkHandlerTest {
    private NetworkHandler<TorConnection>    TorHandler;
    private NetworkHandler<DirectConnection> directHandler;

    @BeforeEach
    void setUp() {
        TorHandler = new NetworkHandler<>(5);
        directHandler = new NetworkHandler<>(5);
    }

    @Test
    void createNextConnection() {
        try {
            // TOR tests
            Class             tor      = TorConnection.class;
            NetworkConnection proxy    = TorHandler.createNextConnection(tor);
            URL               torCheck = new URL("https://check.torproject.org");
            HttpURLConnection conn     = (HttpURLConnection) torCheck.openConnection(proxy.getConnection());
            Scanner           sc       = new Scanner(conn.getInputStream()).useDelimiter("\\A");
            String            result   = sc.hasNext() ? sc.next() : "";
            if (result.indexOf("Congratulations. This browser is configured to use Tor.") < 1) {
                fail();
            }
            // Direct Connection tests
            Class             net          = DirectConnection.class;
            NetworkConnection direct       = directHandler.createNextConnection(net);
            HttpURLConnection dirConn      = (HttpURLConnection) torCheck.openConnection(direct.getConnection());
            Scanner           scanner      = new Scanner(dirConn.getInputStream()).useDelimiter("\\A");
            String            directResult = scanner.hasNext() ? scanner.next() : "";
            if (directResult.indexOf("Sorry. You are not using Tor.") < 1) {
                fail();
            }
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
    void endConnection() {
        try {
            Class tor = TorConnection.class;
            TorHandler.createNextConnection(tor);
            TorHandler.endConnection(0);
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

    @AfterEach
    void tearDown() {
        try {
            TorHandler.endAllConnections();
            directHandler.endAllConnections();
        } catch (Exception e) {
        }
    }

}