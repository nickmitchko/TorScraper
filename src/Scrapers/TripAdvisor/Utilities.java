package Scrapers.TripAdvisor;

import Common.Structures.Tuple;
import Network.NetworkHandler;
import Scrapers.TripAdvisor.Responses.TypeAheadQuery;
import Scrapers.TripAdvisor.Responses.TypeAheadSearch;
import Scrapers.TripAdvisor.Review.ReviewSegment;
import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

/**
 * Created  : nicholai on 3/27/17.
 * Author   : Nicholai Mitchko
 * Package  : Scrapers.TripAdvisor
 * Project  : DistributedScraper
 */
public final class Utilities {

    private static final Logger LOGGER = Logger.getLogger(Utilities.class.getName());

    public static final  String searchURL    = "https://www.tripadvisor.com/Search";
    public static final  String baseURL      = "https://www.tripadvisor.com";
    private static final String gzip         = "gzip";
    private static final int    resultAmount = 10;
    private static NetworkHandler networkHandler;

    public static void setNetworkHandler(NetworkHandler handler) {
        Utilities.networkHandler = handler;
    }

    public static TypeAheadSearch locationSearch(String Query) throws IOException {
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
        HttpURLConnection searchConnection   = (HttpURLConnection) networkHandler.getRequest(parameters, headers, Utilities.baseURL + "/TypeAheadJson");
        Scanner           scanner            = new Scanner(Utilities.gzip.equals(searchConnection.getContentEncoding()) ? new GZIPInputStream(searchConnection.getInputStream()) : searchConnection.getInputStream(), "UTF-8").useDelimiter("\\A");
        String            searchJsonResponse = scanner.hasNext() ? scanner.next() : "";
        return new Gson().fromJson(searchJsonResponse, TypeAheadSearch.class);
    }

    public static TypeAheadQuery querySearch(String query, String geo) throws IOException {
        @SuppressWarnings("unchecked")
        Tuple<String, String>[] parameters = new Tuple[]{
                new Tuple<>("interleaved", "true"),
                new Tuple<>("geoPages", "true"),
                new Tuple<>("matchTags", "true"),
                new Tuple<>("matchGlobalTags", "true"),
                new Tuple<>("matchKeywords", "true"),
                new Tuple<>("strictAnd", "false"),
                new Tuple<>("scoreThreshold", "0.8"),
                new Tuple<>("disableMaxGroupSize", "true"),
                new Tuple<>("scopeFilter", "global"),
                new Tuple<>("injectNewLocation", "true"),
                new Tuple<>("injectLists", "true"),
                new Tuple<>("nearby", "false"),
                new Tuple<>("local", "true"),
                new Tuple<>("parentids", geo),
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
        HttpURLConnection searchConnection   = (HttpURLConnection) networkHandler.getRequest(parameters, headers, Utilities.baseURL + "/TypeAheadJson");
        Scanner           scanner            = new Scanner(Utilities.gzip.equals(searchConnection.getContentEncoding()) ? new GZIPInputStream(searchConnection.getInputStream()) : searchConnection.getInputStream(), "UTF-8").useDelimiter("\\A");
        String            searchJsonResponse = scanner.hasNext() ? scanner.next() : "";
        return new Gson().fromJson(searchJsonResponse, TypeAheadQuery.class);
    }

    public static ArrayList<String> extractListingUrls(String URL, int page) throws IOException {
        @SuppressWarnings("unchecked")
        Tuple<String, String>[] parameters = new Tuple[]{
                new Tuple<>("from_tpa", "true")
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
        if (page > 0) {
            URL += "?o=a" + (page * 30);
        }
        HttpURLConnection resultConnection   = (HttpURLConnection) networkHandler.getRequest(parameters, headers, Utilities.baseURL + URL);
        Scanner           scanner            = new Scanner(Utilities.gzip.equals(resultConnection.getContentEncoding()) ? new GZIPInputStream(resultConnection.getInputStream()) : resultConnection.getInputStream(), "UTF-8").useDelimiter("\\A");
        String            searchJsonResponse = scanner.hasNext() ? scanner.next() : "";
        Document          doc                = Jsoup.parse(searchJsonResponse);
        ArrayList<String>    extractedListings  = new ArrayList<>(30);
        for (Element e : doc.getElementsByClass("listing")) {
            for (Element a : e.select("a")) {
                String url = a.attr("href");
                if (!url.isEmpty()) {
                    extractedListings.add(Utilities.baseURL + url);
                    break;
                }
            }
        }
        return extractedListings;
    }

    public static Document defaultRequest(String URL) throws IOException {
        @SuppressWarnings("unchecked")
        Tuple<String, String>[] parameters = new Tuple[]{
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
        HttpURLConnection resultConnection = (HttpURLConnection) networkHandler.getRequest(parameters, headers, Utilities.baseURL + URL);
        Scanner           scanner          = new Scanner(Utilities.gzip.equals(resultConnection.getContentEncoding()) ? new GZIPInputStream(resultConnection.getInputStream()) : resultConnection.getInputStream(), "UTF-8").useDelimiter("\\A");
        String            listingResponse  = scanner.hasNext() ? scanner.next() : "";
        return Jsoup.parse(listingResponse);
    }

    public static Document requestFilteredReviews(String attractionUrl, ReviewSegment segment) throws IOException {
        int parameterLength = 7;
        int parameterIdx    = 0;
        @SuppressWarnings("unchecked")
        Tuple<String, String>[] parameters = new Tuple[parameterLength];
        parameters[parameterIdx++] = new Tuple<>("mode", "filterReviews");
        parameters[parameterIdx++] = new Tuple<>("filterRating", "");
        parameters[parameterIdx++] = new Tuple<>("filterSeasons", "");
        parameters[parameterIdx++] = new Tuple<>("filterLang", "en");
        parameters[parameterIdx++] = new Tuple<>("returnTo", "");
        parameters[parameterIdx++] = new Tuple<>("filterSegment", "");
        parameters[parameterIdx++] = new Tuple<>("filterSegment", "" + segment.segmentCode());
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
        HttpURLConnection filteredReviewConnection = (HttpURLConnection) networkHandler.postRequest(parameters, headers, attractionUrl);
        Scanner           scanner                  = new Scanner(Utilities.gzip.equals(filteredReviewConnection.getContentEncoding()) ? new GZIPInputStream(filteredReviewConnection.getInputStream()) : filteredReviewConnection.getInputStream(), "UTF-8").useDelimiter("\\A");
        String            reviewResponse           = scanner.hasNext() ? scanner.next() : "";
        return Jsoup.parse(reviewResponse);
    }
}