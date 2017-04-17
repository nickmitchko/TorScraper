package Scrapers.TripAdvisor.Review;

/**
 * Created  : nicholai on 4/15/17.
 * Author   : Nicholai Mitchko
 * Package  : Scrapers.TripAdvisor.Attraction
 * Project  : DistributedScraper
 */
public enum ReviewSegment {
    BUSINESS(1),
    COUPLE(2),
    FAMILY(3),
    FRIEND(4),
    SOLO(5);

    private final int code;

    ReviewSegment(int code) {
        this.code = code;
    }

    public int segmentCode() {
        return this.code;
    }

}
