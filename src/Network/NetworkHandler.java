package Network;

import Common.Structures.Tuple;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.StringJoiner;

/**
 * Created  : nicholai on 3/27/17.
 * Author   : Nicholai Mitchko
 * Package  : Network
 * Project Name : DistributedScraper
 */
public final class NetworkHandler<T extends NetworkConnection> {

    // Configuration Options
    private static int                          SOCKS_PORT_START           = 9050;  // Port to make the first tor process on
    private static int                          CONTROL_PORT_START         = 8118;  // Port that controls the first tor process
    private static int                          MAX_NUMBER_CONNECTIONS     = 1;     // Maximum number of processes that are running leave low to reduce tor overhead
    private static int                          CURRENT_NUMBER_CONNECTIONS = 0;     // Current Number of tor connections active
    private static int                          CURRENT_CONNECTION         = 0;    // Index of the current connection
    private static boolean                      CONNECTED                  = false; // Whether any connections have started...
    private static ArrayList<NetworkConnection> connectionPool             = new ArrayList<>(); // List of Tor connections available TODO: Convert this field to hashMap for performance
    private static Class type;

    public NetworkHandler(int connectionLimit, Class<T> type) {
        NetworkHandler.setConnectionLimit(connectionLimit);
        NetworkHandler.type = type;
    }

    /**
     * Specify the number of connections to max at. (5 is okay for a laptop)
     *
     * @param limit The <b>number</b> of connections to create as a maximum
     */
    public static void setConnectionLimit(int limit) {
        if (!NetworkHandler.CONNECTED || limit >= NetworkHandler.connectionPool.size()) {
            NetworkHandler.MAX_NUMBER_CONNECTIONS = limit;
        } else {
            throw new RuntimeException("You are " + (NetworkHandler.CONNECTED ? "connected but" + (limit >= NetworkHandler.connectionPool.size() ? " your new connection limit is too small" : " for some reason we can't add a connection.") : "not connected"));
        }
    }

    /**
     * @return The <b>maximum number</b> of connections the <u>NetworkHandler</u> will create
     */
    public int getConnectionLimit() {
        return NetworkHandler.MAX_NUMBER_CONNECTIONS;
    }

    /**
     * @return The <b>number</b> of connections <b>under the connection limit</b> the <u>NetworkHandler</u> can create
     */
    public int getAvailableConnections() {
        return NetworkHandler.MAX_NUMBER_CONNECTIONS - NetworkHandler.connectionPool.size();
    }

    public static NetworkConnection createNextConnection() throws IllegalAccessException, InstantiationException, IOException, InterruptedException {
        if (NetworkHandler.connectionPool.size() < NetworkHandler.MAX_NUMBER_CONNECTIONS) {
            NetworkConnection connection = (NetworkConnection) NetworkHandler.type.newInstance();
            if (connection.getType() == NetworkConnection.CONNECTION_TYPE.TOR) {
                connection.setControlPort(NetworkHandler.CONTROL_PORT_START + NetworkHandler.CURRENT_NUMBER_CONNECTIONS);
                connection.setSocksPort(NetworkHandler.SOCKS_PORT_START + NetworkHandler.CURRENT_NUMBER_CONNECTIONS);
            }
            connection.startConnection();
            NetworkHandler.connectionPool.add(connection);
            ++NetworkHandler.CURRENT_NUMBER_CONNECTIONS;
            if (!NetworkHandler.CONNECTED) {
                NetworkHandler.CONNECTED = true;
            }
            return connection;
        }
        throw new IOException("There are not open connection slots");
    }

    public static NetworkConnection getConnection(int connectionNumber) throws RuntimeException {
        if (NetworkHandler.CONNECTED && connectionNumber < NetworkHandler.connectionPool.size() && connectionNumber >= 0) {
            NetworkConnection connection = NetworkHandler.connectionPool.get(connectionNumber);
            if (connection != null) {
                return connection;
            }
        }
        throw new RuntimeException("Cannot get that connection, there is none or null at that index");
    }

    public static void endAllConnections() throws IOException {
        for (NetworkConnection nc : NetworkHandler.connectionPool) {
            if (nc.alive()) {
                nc.endConnection();
            }
        }
    }

    public static void endConnection(int connectionNumber) throws IOException {
        if (NetworkHandler.CONNECTED && connectionNumber < NetworkHandler.connectionPool.size() && connectionNumber >= 0) {
            NetworkHandler.connectionPool.get(connectionNumber).endConnection();
            NetworkHandler.connectionPool.remove(connectionNumber);
            if (NetworkHandler.connectionPool.size() == 0) {
                NetworkHandler.CONNECTED = false;
            }
        } else if (!NetworkHandler.CONNECTED) {
            throw new RuntimeException("Currently Not Connected, Try: createNextConnection()");
        } else {
            throw new RuntimeException("Cannot end that connection with that index: " + connectionNumber);
        }
    }

    public static void startConnections() throws InterruptedException, IOException, InstantiationException, IllegalAccessException {
        if (((NetworkConnection)NetworkHandler.type.newInstance()).getType() == NetworkConnection.CONNECTION_TYPE.TOR) {
            for (int i = 0; i < NetworkHandler.MAX_NUMBER_CONNECTIONS; ++i) {
                NetworkHandler.createNextConnection();
            }
        }
    }

    private static NetworkConnection getNextConnection() {
        if (NetworkHandler.CURRENT_CONNECTION == NetworkHandler.MAX_NUMBER_CONNECTIONS) {
            NetworkHandler.CURRENT_CONNECTION = 0;
        }
        NetworkConnection conn = NetworkHandler.getConnection(NetworkHandler.CURRENT_CONNECTION);
        ++NetworkHandler.CURRENT_CONNECTION;
        return conn;
    }

    public static URLConnection getRequest(Tuple<String, String>[] parameters, Tuple<String, String>[] headers, String url) throws IOException {
        NetworkConnection conn       = NetworkHandler.getNextConnection();
        HttpURLConnection connection = (HttpURLConnection) NetworkHandler.makeParametrizedURL(url, parameters).openConnection(conn.getProxy());
        connection.setRequestMethod("GET");
        connection.setInstanceFollowRedirects(true);
        for (Tuple<String, String> header : headers) {
            connection.setRequestProperty(header.x, header.y);
        }
        return connection;
    }

    public static URLConnection postRequest(Tuple<String, String>[] parameters, Tuple<String, String>[] headers, String url) throws IOException {
        NetworkConnection conn = NetworkHandler.getNextConnection();
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection(conn.getProxy());
        byte[] content = NetworkHandler.makePostContent(parameters);
        connection.setRequestMethod("POST");
        connection.setInstanceFollowRedirects(true);
        connection.setFixedLengthStreamingMode(content.length);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
        for(Tuple<String, String> header: headers){
            connection.setRequestProperty(header.x, header.y);
        }
        try(OutputStream outboundConnection = connection.getOutputStream()){
            outboundConnection.write(content);
        }
        return connection;
    }

    public static URL makeParametrizedURL(String url, Tuple<String, String>[] parameters) throws MalformedURLException, UnsupportedEncodingException {
        if (parameters.length > 0) {
            if (!url.endsWith("?")) {
                url += '?';
            }
            StringBuilder urlBuilder = new StringBuilder(url);
            for (Tuple<String, String> entry : parameters) {
                urlBuilder.append(entry.x).append("=").append(URLEncoder.encode(entry.y, "UTF-8")).append("&");
            }
            url = urlBuilder.toString();
            if (url.endsWith("&")) {
                url = url.substring(0, url.length() - 1);
            }
        }
        return new URL(url);
    }

    public static byte[] makePostContent(Tuple<String, String>[] formContents) throws UnsupportedEncodingException {
        StringJoiner postBuilder = new StringJoiner("&");
        for(Tuple<String, String> item: formContents){
            postBuilder.add(URLEncoder.encode(item.x, "UTF-8") + "=" + URLEncoder.encode(item.y, "UTF-8"));
        }
        return postBuilder.toString().getBytes(StandardCharsets.UTF_8);
    }

}
