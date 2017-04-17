package Scrapers.TripAdvisor.Review;

import org.jsoup.nodes.Document;

/**
 * Created  : nicholai on 4/16/17.
 * Author   : Nicholai Mitchko
 * Package  : Scrapers.TripAdvisor.Review
 * Project  : DistributedScraper
 */
public class Hotel extends Review {

    public Hotel(Document document){
        super(document);
    }

    @Override
    public void getReviewRating() {
        try{
            this.addToReview("Rating", this.reviewPage.select(".sprite-rating_s_fill").attr("alt").substring(0,1));
        } catch (IndexOutOfBoundsException e){
            this.addToReview("Rating", "");
        }
    }

    @Override
    public void process() {
        this.processGenericContent();
        this.getReviewRating();
    }

    @Override
    public Object[] asArray() {
        return new Object[0];
    }

    @Override
    public String[] asStringArray() {
        return new String[0];
    }
}
