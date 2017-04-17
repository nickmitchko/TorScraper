package Scrapers.TripAdvisor.Attraction;


import Scrapers.TripAdvisor.Review.Hotel;
import Scrapers.TripAdvisor.Review.Review;
import Scrapers.TripAdvisor.Review.ReviewSegment;
import Scrapers.TripAdvisor.Utilities;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created  : nicholai on 4/12/17.
 * Author   : Nicholai Mitchko
 * Package  : Scrapers.TripAdvisor.Attraction
 * Project  : DistributedScraper
 */
public class AttractionThread extends Thread {

    private static final Logger logger = Logger.getLogger(AttractionThread.class.getName());

    private final String                   attractionURL;
    private       ArrayList<String>        reviewUrls;
    private       ArrayList<ReviewSegment> reviewTypes;
    private       Document                 reviewPage;
    private       int                      pageOffset;
    private final int                      pageLimit;
    private final int                      reviewLimit;
    private boolean           running        = false;
    private ArrayList<Review> scrapedReviews = new ArrayList<>(31); // I have no clue why 31 is the page minimum

    public AttractionThread(String URL, ArrayList<ReviewSegment> reviewTypes, int pageMax, int reviewMax) throws MalformedURLException {
        this.attractionURL = URL;
        this.reviewTypes = reviewTypes;
        this.pageLimit = pageMax;
        this.reviewLimit = reviewMax;
    }

    private ArrayList<String> getReviewUrls() throws IOException {
        ArrayList<String> extractedURLs = new ArrayList<>(10);
        for (Element e : reviewPage.select("#REVIEWS .col2of2 .quote > a")) {
            String reviewURL = e.attr("href");
            if (!reviewURL.isEmpty()) {
                extractedURLs.add(reviewURL);
            }
        }
        return extractedURLs;
    }

    @Override
    public void run() {
        this.running = true;
        this.pageOffset = 0;
        while (this.isRunning()) {
            try {
                for (ReviewSegment segment : reviewTypes) {
                    this.reviewPage = Utilities.requestFilteredReviews(this.attractionURL, segment);
                    this.reviewUrls = this.getReviewUrls();
                    Hotel review = new Hotel(Utilities.defaultRequest(this.reviewUrls.get(0)));
                    review.process();
                    review.getReviewObjects();
                }
                ++this.pageOffset;
            } catch (IOException e) {
                e.printStackTrace();
                logger.log(Level.SEVERE, "Requesting reviews encountered an error {0}", e.getMessage());
                this.interrupt();
            }
            this.running = false;
        }
    }

    private void scrapeReviews() {
        for (String url : reviewUrls) {
            try{
            } catch (Exception e){
                logger.log(Level.SEVERE, "There was an error retrieving a review {0}", url);
                logger.log(Level.INFO, "Continuing despite error");
            }
        }
    }

    public boolean isRunning() {
        return running;
    }
}
