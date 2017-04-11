package Network;

import java.io.IOException;
import java.net.Proxy;

/**
 * Created  : nicholai on 3/27/17.
 * Author   : Nicholai Mitchko
 * Package  : Network
 * Project Name : DistributedScraper
 */
public interface NetworkConnection {

    enum CONNECTION_TYPE {
        TOR,
        DIRECT
    }

    Proxy getProxy();
    void startConnection() throws IOException, InterruptedException;
    void endConnection() throws IOException;
    void setSocksPort(int port);
    void setControlPort(int port);
    void setTorNumber(int number);
    boolean alive();
    CONNECTION_TYPE getType();
}