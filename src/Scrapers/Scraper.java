package Scrapers;

/**
 * Created  : nicholai on 3/27/17.
 * Author   : Nicholai Mitchko
 * Package  : Scrapers
 * Project  : DistributedScraper
 */
public class Scraper extends Thread {

    private int ScraperId;

    public Scraper(int id){
        super();
        this.ScraperId = id;
    }

    @Override
    public void run() {

    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}