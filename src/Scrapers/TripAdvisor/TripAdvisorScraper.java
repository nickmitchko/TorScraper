package Scrapers.TripAdvisor;

import Scrapers.ScraperDefault;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created  : nicholai on 3/27/17.
 * Author   : Nicholai Mitchko
 * Package  : Scrapers
 * Project  : DistributedScraper
 */
public class TripAdvisorScraper extends Thread implements ScraperDefault {

    private Logger logger = Logger.getLogger("myLogger");

    private       File          directory;
    private       File          output;
    private       OUTPUT_FORMAT outputFormat;
    private final String        url;
    private boolean running = false;


    public TripAdvisorScraper(String url) {
        super();
        this.url = url;
    }

    @Override
    public void run() {
        if (!this.running) {
            this.running = true;
            while (this.running) {


            }
        }
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
    public void setOutputFormat(OUTPUT_FORMAT format) {
        this.outputFormat = format;
    }

    @Override
    public void setOutputDirectory(File outputDirectory) {
        if (outputDirectory.isDirectory()) {
            this.directory = outputDirectory;
        } else {
            logger.log(Level.INFO, "The file supplied as 'outputDirectory' is not a valid directory. Please supply a valid directory");
        }
    }

    @Override
    public void setOutputFile(File outputFile) {
        if (outputFile.isFile()) {
            this.output = outputFile;
            if (this.directory == null) {
                this.directory = this.output.getParentFile();
            }
        } else {
            logger.log(Level.INFO, "The file supplied as 'outputFile' is not a valid file.");
        }
    }

    @Override
    public File getOutputDirectory() {
        return directory;
    }

    @Override
    public OUTPUT_FORMAT getOutputFormat() {
        return this.outputFormat;
    }

    @Override
    public void endScraping(boolean immediately) {
        if (immediately) {
            this.interrupt();
        } else {
            this.running = false;

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