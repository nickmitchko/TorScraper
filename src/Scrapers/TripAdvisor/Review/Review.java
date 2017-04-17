package Scrapers.TripAdvisor.Review;

import Scrapers.TripAdvisor.Utilities;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created  : nicholai on 3/27/17.
 * Author   : Nicholai Mitchko
 * Package  : Scrapers.TripAdvisor.Review
 * Project  : DistributedScraper
 */
public abstract class Review {

    private static final Logger logger = Logger.getLogger(Review.class.getName());
    private      HashMap<String, Object> reviewObjects;
    public final Document                reviewPage;
    public       Document                memberPage;
    private static final String     visitDateRegex1 = ", traveled.*";
    private static final String     visitDateRegex2 = "(Visited|Stayed).";
    private static final String     memberURLExt = "/members/";
    private static final DateFormat visitDateFormat = new SimpleDateFormat("M y");
    private static final DateFormat outputFormat    = new SimpleDateFormat("M/d/y");

    public Review(Document document) {
        this.reviewPage = document;
        this.reviewObjects = new HashMap<>();
    }

    public void addToReview(String key, Object value) {
        this.reviewObjects.put(key, value);
    }

    public void processGenericContent() {
        this.getProfileUsername();
        this.getUserPage();
        this.getReviewTitle();
        this.getReviewLocation();
        this.getNumberPictures();
        this.getMobile();
        this.getReviewDate();
        this.getVisitDate();
        this.isReviewHelpful();
        this.getReviewText();
        this.getLevelContributor();
        this.getReviewThanked();
        this.getProfilePoints();
        this.getProfileNumberReviews();
        this.getProfileAge();
        this.getProfileGender();
        this.getProfileNumPictures();
        this.getUserAge();
    }

    private void getUserAge() {
        this.addToReview("ReviewerAge", "");
    }

    private void getProfileNumPictures() {
        this.addToReview("ReviewerPhotos", memberPage
                .select(".content-link [name=\"photos\"]")
                .text()
                .toLowerCase()
                .replaceAll(" photo", "")
                .replaceAll(" photos", ""));
    }

    private void getProfileGender() {
        char profileGender = ' ';
        try {
            String gender = memberPage.select(".ageSince").html().toLowerCase();
            if (gender.indexOf("male") > -1) {
                profileGender = 'M';
            } else if (gender.indexOf("female") > -1) {
                profileGender = 'F';
            }
        } catch (Exception e) {
            logger.log(Level.INFO, "There was not gender indicated on the profile");
        }
        this.addToReview("ReviewerGender", profileGender);
    }

    private void getProfileAge() {
        try {
            String memberAge = memberPage.select(".ageSince .since").text().toLowerCase().replaceAll("since ", "");
            this.addToReview("ReviewerSince", Review.outputFormat.format(Review.visitDateFormat.parse(memberAge)));
        } catch (Exception e) {
            logger.log(Level.WARNING, "Could not get the member age");
            this.addToReview("ReviewerSince", "");
        }
    }

    private void getProfileNumberReviews() {
        this.addToReview("ReviewerNumReviews", memberPage.select(".content-link [name=\"reviews\"]").text().toLowerCase().replaceAll(" reviews", ""));
    }

    private void getProfilePoints() {
        this.addToReview("ReviewerPoints", memberPage.select(".memberPointInfo .points_info .points").text());
    }

    private void getReviewThanked() {
    }

    private void getLevelContributor() {
        try {
            String badgeUrl = reviewPage.select("levelBadge img").attr("src");
            this.addToReview("ReviewerLevelContrib", badgeUrl.substring(badgeUrl.indexOf("lvl_"), badgeUrl.indexOf(".png")));
        } catch (Exception e) {
            logger.log(Level.WARNING, "Could not figure out the reviewer's contributor level.");
            this.addToReview("ReviewerLevelContrib", "");
        }
    }

    private void getNumberPictures() {
        this.addToReview("ReviewNumPic", reviewPage.select(".col2of2 img").size() + "");
    }

    private void getMobile() {
        this.addToReview("ReviewMobile", (reviewPage.select(".viaMobile").size() > 0) ? "1" : "0");
    }

    private void getVisitDate() {
        try {
            String visitDateString = reviewPage.select(".rating-list > .recommend > .recommend-titleInline").text();
            visitDateString = visitDateString.replaceAll(Review.visitDateRegex1, "").replaceAll(Review.visitDateRegex2, "");
            Date visitDate = Review.visitDateFormat.parse(visitDateString);
            this.addToReview("DateVisited", Review.outputFormat.format(visitDate));
        } catch (Exception e) {
            this.addToReview("DateVisited", "");
        }
    }

    private void getReviewDate() {
        try {
            this.addToReview("ReviewDate", reviewPage.select(".relativeDate").attr("Content"));
        } catch (Exception e) {
            this.addToReview("ReviewDate", "NA");
        }
    }

    private void isReviewHelpful() {
        this.addToReview("ReviewHelpful", reviewPage.select(".numHlpIn").text());
    }

    private void getReviewText() {
        this.addToReview("ReviewText", reviewPage.select(".entry [property=\"reviewBody\"]").text());
    }

    private void getReviewLocation() {
        try {
            String location  = reviewPage.select(".location").text();
            int    separator = location.indexOf(',');
            this.addToReview("ReviewerLocationState", location.substring(0, separator));
            this.addToReview("ReviewerLocationCity", location.substring(separator, location.length()));
        } catch (IndexOutOfBoundsException e) {
            logger.log(Level.WARNING, "The location format provided by TripAdvisor is bad.");
            this.addToReview("ReviewerLocationState", "");
            this.addToReview("ReviewerLocationCity", "");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Something terrible went wrong; The location format provided by TripAdvisor is bad (probably).");
            this.addToReview("ReviewerLocationState", "");
            this.addToReview("ReviewerLocationCity", "");
        }
    }

    private void getProfileUsername() {
        this.addToReview("Username", reviewPage.select(".username").first().text());
    }

    private void getUserPage() {
        try {
            this.memberPage = Utilities.defaultRequest(memberURLExt + (this.reviewObjects.get("Username")));
            if (this.memberPage.select(".error404").size() > 0) {
                throw new IOException("There was an error retrieving the user profile");
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not retrieve the member page. {0}", e.getStackTrace());
        }
    }

    public HashMap<String, Object> getReviewObjects(){
        return this.reviewObjects;
    }

    private void getReviewTitle() {
        this.addToReview("Title", reviewPage.select(".noQuotes").text());
    }

    public abstract void getReviewRating();

    public abstract Object[] asArray();

    public abstract String[] asStringArray();

    public abstract void process();

}
