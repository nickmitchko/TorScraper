package Scrapers.TripAdvisor;

import Network.NetworkConnection;
import Scrapers.ScraperDefault;

import java.io.File;
import java.net.Proxy;

/**
 * Created  : nicholai on 3/27/17.
 * Author   : Nicholai Mitchko
 * Package  : Scrapers
 * Project  : DistributedScraper
 */
public class TripAdvisorScraper extends Thread implements ScraperDefault {

    private final NetworkConnection scraperConnection;

    public TripAdvisorScraper(NetworkConnection proxy){
        super();
        this.scraperConnection = proxy;
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

    @Override
    public void startScraping() {
        this.run();
    }

    @Override
    public void setOutputFormat() {

    }

    @Override
    public void setOutputDirectory() {

    }

    @Override
    public File getOutputDirectory() {
        return null;
    }

    @Override
    public OUTPUT_FORMAT getOutputFormat() {
        return null;
    }

    @Override
    public void endScraping(boolean immediately) {
        if (immediately){
            this.interrupt();
        } else {
            // end loop
            // save file
            // stop loop
            //
            this.interrupt();
        }
    }

    @Override
    public void flushData() {

    }

    @Override
    public void saveFile() {

    }

    @Override
    public File getDataFile() {
        return null;
    }
}