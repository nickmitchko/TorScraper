package Network.Types;

import Network.NetworkConnection;

import java.io.IOException;
import java.net.Proxy;

/**
 * Created  : nicholai on 3/27/17.
 * Author   : Nicholai Mitchko
 * Package  : Network
 * Project Name : DistributedScraper
 */
public class DirectConnection implements NetworkConnection {
    @Override
    public Proxy getConnection() {
        return Proxy.NO_PROXY;
    }

    @Override
    public void startConnection() throws IOException, InterruptedException {
    }

    @Override
    public void endConnection() throws IOException {
    }

    @Override
    public void setSocksPort(int port) {
    }

    @Override
    public void setControlPort(int port) {
    }

    @Override
    public void setTorNumber(int number) {
    }

    @Override
    public boolean alive() {
        return true;
    }

    @Override
    public CONNECTION_TYPE getType() {
        return CONNECTION_TYPE.DIRECT;
    }
}
