package Scrapers;

import java.io.File;

/**
 * Created  : nicholai on 3/29/17.
 * Author   : Nicholai Mitchko
 * Package  : Scrapers.TripAdvisor
 * Project Name : DistributedScraper
 */
public interface ScraperDefault {
    enum OUTPUT_FORMAT {
        JSON,
        CSV,
        EXCEL,
        XLSX,
        XLS
    }

    void startScraping();
    void setOutputFormat(OUTPUT_FORMAT format);
    void setOutputDirectory(File outputDirectory);
    void setOutputFile(File outputFile);
    void openFile();
    File getOutputDirectory();
    OUTPUT_FORMAT getOutputFormat();
    void endScraping(boolean immediately);
    void flushData();
    void saveFile();
    File getDataFile();
}
