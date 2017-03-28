package Network.Types;

import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * Created  : nicholai on 3/27/17.
 * Author   : Nicholai Mitchko
 * Package  : Network.Types
 * Project  : DistributedScraper
 */
class TorConnectionTest {
    @Test
    void getConnection() {

    }

    @Test
    void startConnection() {
        TorConnection tor = new TorConnection(9050, 8118);
        try {
            tor.startConnection();
            tor.endConnection();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void endConnection() {
        TorConnection tor = new TorConnection(9050, 8118);
        try {
            tor.startConnection();
            tor.endConnection();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void setSocksPort() {
        TorConnection tor = new TorConnection(9050, 8118);
        tor.setSocksPort(9060);
    }

    @Test
    void setTorNumber() {
    }

}