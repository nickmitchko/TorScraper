package Scrapers.TripAdvisor;

import Common.Structures.Tuple;
import Network.NetworkHandler;
import Scrapers.TripAdvisor.Responses.TypeAheadQuery;
import Scrapers.TripAdvisor.Responses.TypeAheadSearch;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
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

    public static TypeAheadSearch locationSearch(String Query, NetworkHandler networkHandler) throws IOException {
        @SuppressWarnings("unchecked")
        Tuple<String, String>[] parameters = new Tuple[]{
                new Tuple<>("interleaved", "true"),
                new Tuple<>("types", "geo"),
                new Tuple<>("neighborhood_geos", "true"),
                new Tuple<>("link_type", "geo"),
                new Tuple<>("details", "true"),
                new Tuple<>("max", "" + Utilities.resultAmount),
                new Tuple<>("hgtl", "true"),
                new Tuple<>("query", Query),
                new Tuple<>("action", "API"),
                new Tuple<>("startTime", "" + System.currentTimeMillis()),
                new Tuple<>("uiOrigin", "GEOSCOPE"),
                new Tuple<>("source", "GEOSCOPE")};
        @SuppressWarnings("unchecked")
        Tuple<String, String>[] headers = new Tuple[]{
                new Tuple<>("Host", "www.tripadvisor.com"),
                new Tuple<>("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:52.0) Gecko/20100101 Firefox/52.0"),
                new Tuple<>("Accept", "text/javascript, text/html, application/xml, text/xml, */*"),
                new Tuple<>("Accept-Language", "en-US,en;q=0.5"),
                new Tuple<>("Accept-Encoding", "gzip"),
                new Tuple<>("X-Requested-With", "XMLHttpRequest"),
                new Tuple<>("Referer", "https://www.tripadvisor.com/"),
                new Tuple<>("Connection", "keep-alive")
        };
        HttpURLConnection searchConnection   = (HttpURLConnection) networkHandler.getRequest(parameters, headers, Utilities.baseURL + "TypeAheadJson");
        Reader            responseReader     = new InputStreamReader("gzip".equals(searchConnection.getContentEncoding()) ? new GZIPInputStream(searchConnection.getInputStream()) : searchConnection.getInputStream());
        StringBuilder     searchJsonResponse = new StringBuilder();
        int               currentByte;
        while ((currentByte = responseReader.read()) != -1) {
            searchJsonResponse.append((char) currentByte);
        }
        return new Gson().fromJson(searchJsonResponse.toString(), TypeAheadSearch.class);
    }

    public static TypeAheadQuery querySearch(String query, String geo, NetworkHandler networkHandler) throws IOException {
        @SuppressWarnings("unchecked")
        Tuple<String, String>[] parameters = new Tuple[]{
                new Tuple<>("interleaved", "true"),
                new Tuple<>("geoPages","true"),
                new Tuple<>("matchTags","true"),
                new Tuple<>("matchGlobalTags","true"),
                new Tuple<>("matchKeywords","true"),
                new Tuple<>("strictAnd","false"),
                new Tuple<>("scoreThreshold","0.8"),
                new Tuple<>("disableMaxGroupSize","true"),
                new Tuple<>("scopeFilter","global"),
                new Tuple<>("injectNewLocation","true"),
                new Tuple<>("injectLists","true"),
                new Tuple<>("nearby","false"),
                new Tuple<>("local","true"),
                new Tuple<>("parentids",geo),
                new Tuple<>("types", "geo,hotel,eat,attr,vr,air,theme_park,al,act"),
                new Tuple<>("neighborhood_geos", "true"),
                new Tuple<>("link_type", "geo"),
                new Tuple<>("details", "true"),
                new Tuple<>("max", "" + Utilities.resultAmount),
                new Tuple<>("hgtl", "true"),
                new Tuple<>("query", query),
                new Tuple<>("action", "API"),
                new Tuple<>("startTime", "" + System.currentTimeMillis()),
                new Tuple<>("uiOrigin", "MASTHEAD"),
                new Tuple<>("source", "MASTHEAD")
        };
        @SuppressWarnings("unchecked")
        Tuple<String, String>[] headers = new Tuple[]{
                new Tuple<>("Host", "www.tripadvisor.com"),
                new Tuple<>("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:52.0) Gecko/20100101 Firefox/52.0"),
                new Tuple<>("Accept", "*/*"),
                new Tuple<>("Accept-Language", "en-US,en;q=0.5"),
                new Tuple<>("Accept-Encoding", "gzip"),
                new Tuple<>("X-Requested-With", "XMLHttpRequest"),
                new Tuple<>("Referer", "https://www.tripadvisor.com/"),
                new Tuple<>("Connection", "keep-alive")
        };
        HttpURLConnection searchConnection = (HttpURLConnection) networkHandler.getRequest(parameters, headers, Utilities.baseURL + "TypeAheadJson");
        Reader            responseReader     = new InputStreamReader("gzip".equals(searchConnection.getContentEncoding()) ? new GZIPInputStream(searchConnection.getInputStream()) : searchConnection.getInputStream());
        StringBuilder     searchJsonResponse = new StringBuilder();
        int               currentByte;
        while ((currentByte = responseReader.read()) != -1) {
            searchJsonResponse.append((char) currentByte);
        }
        return new Gson().fromJson(searchJsonResponse.toString(), TypeAheadQuery.class);
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