package Network;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created  : nicholai on 3/27/17.
 * Author   : Nicholai Mitchko
 * Package  : Network
 * Project Name : DistributedScraper
 */
public class NetworkHandler<T extends NetworkConnection> {

    // Configuration Options
    private int          SOCKS_PORT_START           = 9050;  // Port to make the first tor process on
    private int          CONTROL_PORT_START         = 8118;  // Port that controls the first tor process
    private int          MAX_NUMBER_CONNECTIONS     = 5;     // Maximum number of processes that are running leave low to reduce tor overhead
    private int          CURRENT_NUMBER_CONNECTIONS = 0;     // Current Number of tor connections active
    private boolean      CONNECTED                  = false; // Whether any connections have started...
    private ArrayList<T> connectionPool             = new ArrayList<>(); // List of Tor connections available TODO: Convert this field to hashMap for performance

    public NetworkHandler(int connectionLimit) {
        this.setConnectionLimit(connectionLimit);
    }

    /**
     * Specify the number of connections to max at. (5 is okay for a laptop)
     *
     * @param limit The <b>number</b> of connections to create as a maximum
     */
    public void setConnectionLimit(int limit) {
        if (!this.CONNECTED || limit >= this.connectionPool.size()) {
            this.MAX_NUMBER_CONNECTIONS = limit;
        } else {
            throw new RuntimeException("You are " + (this.CONNECTED ? "connected but" + (limit >= this.connectionPool.size() ? " your new connection limit is too small" : " for some reason we can't add a connection.") : "not connected"));
        }
    }

    /**
     * @return The <b>maximum number</b> of connections the <u>NetworkHandler</u> will create
     */
    public int getConnectionLimit() {
        return this.MAX_NUMBER_CONNECTIONS;
    }

    /**
     * @return The <b>number</b> of connections <b>under the connection limit</b> the <u>NetworkHandler</u> can create
     */
    public int getAvailableConnections() {
        return this.MAX_NUMBER_CONNECTIONS - this.connectionPool.size();
    }

    public T createNextConnection(Class<T> type) throws IllegalAccessException, InstantiationException, IOException, InterruptedException {
        if (this.connectionPool.size() < this.MAX_NUMBER_CONNECTIONS) {
            T connection = type.newInstance();
            if (connection.getType() == NetworkConnection.CONNECTION_TYPE.TOR) {
                connection.setControlPort(this.CONTROL_PORT_START + this.CURRENT_NUMBER_CONNECTIONS);
                connection.setSocksPort(this.SOCKS_PORT_START + this.CURRENT_NUMBER_CONNECTIONS);
            }
            connection.startConnection();
            this.connectionPool.add(connection);
            ++this.CURRENT_NUMBER_CONNECTIONS;
            if (!this.CONNECTED) {
                this.CONNECTED = true;
            }
            return connection;
        }
        throw new IOException("There are not open connection slots");
    }

    public T getConnection(int connectionNumber) throws RuntimeException {
        if (this.CONNECTED && connectionNumber < this.connectionPool.size() && connectionNumber >= 0) {
            T connection = this.connectionPool.get(connectionNumber);
            if (connection != null) {
                return connection;
            }
        }
        throw new RuntimeException("Cannot get that connection, there is none or null at that index");
    }

    public void endAllConnections() throws IOException {
        for (NetworkConnection nc : this.connectionPool){
            if(nc.alive()){
                nc.endConnection();
            }
        }
    }

    public void endConnection(int connectionNumber) throws IOException {
        if (this.CONNECTED && connectionNumber < this.connectionPool.size() && connectionNumber >= 0) {
            this.connectionPool.get(connectionNumber).endConnection();
            this.connectionPool.remove(connectionNumber);
            if (this.connectionPool.size() == 0) {
                this.CONNECTED = false;
            }
        } else if (!this.CONNECTED) {
            throw new RuntimeException("Currently Not Connected, Try: createNextConnection()");
        } else {
            throw new RuntimeException("Cannot end that connection with that index: " + connectionNumber);
        }
    }

}
