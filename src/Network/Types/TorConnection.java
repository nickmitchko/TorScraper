package Network.Types;

import Network.NetworkConnection;

import java.io.File;
import java.io.IOException;
import java.net.*;

/**
 * Created  : nicholai on 3/27/17.
 * Author   : Nicholai Mitchko
 * Package  : Network
 * Project Name : DistributedScraper
 */
public class TorConnection implements NetworkConnection {

    private int     TOR_SOCKS_PORT     = 0;
    private int     TOR_CTRL_PORT      = 0;
    private int     TOR_PROCESS_NUMBER = 0;
    private boolean active             = false;
    private SocketAddress address;
    private Proxy         proxy;
    private Process       torProcess;

    public TorConnection() {
        super();
    }

    public TorConnection(int socksPort, int ctrlPort) {
        super();
        this.TOR_SOCKS_PORT = socksPort;
        this.TOR_CTRL_PORT = ctrlPort;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "Tor Connection : {\r\nSOCKS PORT:\t" + this.TOR_SOCKS_PORT + ",\r\nCONTROL PORT:\t" + this.TOR_CTRL_PORT + "\r\n}";
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public Proxy getConnection() {
        return this.proxy;
    }

    @Override
    public void startConnection() throws IOException, InterruptedException {
        if (!active) {
            int torSuccess = startTorProcess();
            if (torSuccess == 0) {
                this.address = new InetSocketAddress("localhost", this.TOR_SOCKS_PORT);
                this.proxy = new Proxy(Proxy.Type.SOCKS, this.address);
                this.active = true;
            }
        } else {
            throw new RuntimeException("The connection is active, you can't restart the connection");
        }
    }

    @Override
    public void endConnection() throws IOException {
        if (active) {
            this.proxy = null;
            this.address = null;
            this.endTorProcess();
        }
    }

    @Override
    public CONNECTION_TYPE getType() {
        return CONNECTION_TYPE.TOR;
    }

    @Override
    public void setSocksPort(int port) {
        if (!active) {
            this.TOR_SOCKS_PORT = port;
        } else {
            throw new RuntimeException("The connection is already started");
        }
    }

    @Override
    public void setTorNumber(int number) {
        this.TOR_PROCESS_NUMBER = number;
    }

    @Override
    public boolean alive() {
        return this.active;
    }

    @Override
    public void setControlPort(int port) {
        if (!active) {
            this.TOR_CTRL_PORT = port;
        } else {
            throw new RuntimeException("The connection is already started");
        }
    }

    private int startTorProcess() throws IOException, InterruptedException {
        if (!active) {
            this.createTorDataDir();
            this.torProcess = Runtime.getRuntime().exec("tor --RunAsDaemon 1 --CookieAuthentication 0 --HashedControlPassword \"\" --ControlPort " + this.TOR_CTRL_PORT + " --PidFile tor" + this.TOR_PROCESS_NUMBER + ".pid --SocksPort " + this.TOR_CTRL_PORT + " --DataDirectory ./data/tor" + this.TOR_PROCESS_NUMBER);
        }
        return this.torProcess.isAlive() ? 0 : 1;
    }

    private void endTorProcess() throws IOException {
        if (active) {
            Runtime.getRuntime().exec("pkill -F tor" + this.TOR_PROCESS_NUMBER + ".pid");
        }
    }

    private void createTorDataDir() {
        File dataDirectory = new File("./data/tor" + this.TOR_PROCESS_NUMBER + "/");
        if (!dataDirectory.exists()) {
            dataDirectory.mkdirs();
        }
    }
}
