package Scrapers.TripAdvisor;

import Scrapers.TripAdvisor.Responses.LocationSearch;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

/**
 * Created  : nicholai on 3/27/17.
 * Author   : Nicholai Mitchko
 * Package  : Scrapers.TripAdvisor
 * Project  : DistributedScraper
 */
public class Utilities {

    private static final Logger LOGGER = Logger.getLogger(Utilities.class.getName());

    private static final String searchURL    = "https://www.tripadvisor.com/Search";
    private static final String baseURL      = "https://www.tripadvisor.com/";
    private static final int    resultAmount = 10;

    public static void locationSearch(String Query) throws IOException {
        Utilities.locationSearch(Query, Proxy.NO_PROXY);
    }

    public static LocationSearch locationSearch(String Query, Proxy proxy) throws IOException {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("interleaved", "true");
        parameters.put("types", "geo");
        parameters.put("neighborhood_geos", "true");
        parameters.put("link_type", "geo");
        parameters.put("details", "true");
        parameters.put("max", "" + Utilities.resultAmount);
        parameters.put("hgtl", "true");
        parameters.put("query", Query);
        parameters.put("action", "API");
        parameters.put("startTime", "" + System.currentTimeMillis());
        parameters.put("uiOrigin", "GEOSCOPE");
        parameters.put("source", "GEOSCOPE");
        HttpURLConnection searchConnection = (HttpURLConnection) makeParametrizedURL(Utilities.baseURL + "TypeAheadJson", parameters).openConnection(proxy);
        searchConnection.setRequestMethod("GET");
        searchConnection.setInstanceFollowRedirects(true);
        searchConnection.setRequestProperty("Host", "www.tripadvisor.com");
        searchConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:52.0) Gecko/20100101 Firefox/52.0");
        searchConnection.setRequestProperty("Accept", "text/javascript, text/html, application/xml, text/xml, */*");
        searchConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        searchConnection.setRequestProperty("Accept-Encoding", "gzip");
        searchConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
        searchConnection.setRequestProperty("Referer", "https://www.tripadvisor.com/");
        searchConnection.setRequestProperty("Connection", "keep-alive");
        Reader responseReader = new InputStreamReader("gzip".equals(searchConnection.getContentEncoding()) ? new GZIPInputStream(searchConnection.getInputStream()) : searchConnection.getInputStream());
        StringBuilder searchJsonResponse = new StringBuilder();
        int currentByte;
        while ((currentByte = responseReader.read()) != -1) {
            searchJsonResponse.append((char)currentByte);
        }
        return new Gson().fromJson(searchJsonResponse.toString(), LocationSearch.class);
    }

    public static URL makeParametrizedURL(String url, HashMap<String, String> parameters) throws MalformedURLException, UnsupportedEncodingException {
        if (!url.endsWith("?")) {
            url += '?';
        }
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            url += entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "UTF-8") + "&";
        }
        if (url.endsWith("&")) {
            url = url.substring(0, url.length() - 1);
        }
        return new URL(url);
    }

}