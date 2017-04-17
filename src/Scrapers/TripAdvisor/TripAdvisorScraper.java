package Scrapers.TripAdvisor;

import Scrapers.ScraperDefault;
import Scrapers.TripAdvisor.Attraction.AttractionThread;
import Scrapers.TripAdvisor.Review.ReviewSegment;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created  : nicholai on 3/27/17.
 * Author   : Nicholai Mitchko
 * Package  : Scrapers
 * Project  : DistributedScraper
 */
public class TripAdvisorScraper extends Thread implements ScraperDefault {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private       File          directory;
    private       File          output;
    private       OUTPUT_FORMAT outputFormat;
    private final String        url;
    private       boolean             running         = false;
    private       Object              fileHandle      = null;
    private       ArrayList<String[]> nextRows        = new ArrayList<>(10);
    private final int                 paginationValue = 30; // This is the offset size of the search pagination

    public TripAdvisorScraper(String url) {
        super();
        this.url = url;
        this.outputFormat = OUTPUT_FORMAT.CSV;
    }

    public TripAdvisorScraper(String url, OUTPUT_FORMAT saveFormat) {
        super();
        this.url = url;
        this.outputFormat = saveFormat;
    }

    @Override
    public void run() {
        if (!this.running) {
            this.running = true;
            this.openFile();
            int offset = 0;
            while (this.running) {
                try {
                    ArrayList<String> listingURLs = Utilities.extractListingUrls(url, offset);
                    ArrayList<ReviewSegment> segments = new ArrayList<>();
                    segments.add(ReviewSegment.COUPLE);
                    AttractionThread thread = new AttractionThread(listingURLs.get(0), segments, 1, 1 );
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ++offset;
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "There was an error scraping listing urls {0}", e.getStackTrace());
                    logger.log(Level.WARNING, "Scraping will end now");
                    this.endScraping(true);
                }
            }
            this.saveFile();
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

    public void startScraping() {
        this.start();
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
        this.output = outputFile;
        if (this.directory == null) {
            this.directory = this.output.getParentFile();
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
        }
    }

    @Override
    public void openFile() {
        try {
            switch (this.outputFormat) {
                case CSV: {
                    fileHandle = new CSVWriter(new FileWriter(this.output));
                    break;
                }
                case XLS: // TODO: Finish these implementations later
                case JSON:
                case XLSX:
                case EXCEL:
                default:
                    break;
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "There was an error opening the output file {0}", e.getStackTrace());
            logger.log(Level.WARNING, "Scraping will end now");
            this.endScraping(false);
        }
    }

    @Override
    public void flushData() {
        try {
            switch (this.outputFormat) {
                case CSV: {
                    for (String[] row : this.nextRows) {
                        ((CSVWriter) this.fileHandle).writeNext(row);
                    }
                    break;
                }
                case XLS:
                case JSON:
                case XLSX:
                case EXCEL:
                default:
                    break;
            }
            this.nextRows.clear();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Could not flush data to file {0}", e.getStackTrace());
            logger.log(Level.WARNING, "Scraping will end now");
            this.endScraping(false);
        }
    }

    @Override
    public void saveFile() {
        try {
            this.flushData();
            switch (this.outputFormat) {
                case CSV: {
                    ((CSVWriter) this.fileHandle).close();
                    break;
                }
                case XLS:
                case JSON:
                case XLSX:
                case EXCEL:
                default:
                    break;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "There was an error saving your file {0}", e.getStackTrace());
            logger.log(Level.WARNING, "Scraping will end now");
            this.endScraping(true);
        }
    }

    @Override
    public File getDataFile() {
        return this.output;
    }

    public int getPaginationValue() {
        return paginationValue;
    }
}