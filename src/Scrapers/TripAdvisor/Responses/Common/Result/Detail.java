package Scrapers.TripAdvisor.Responses.Common.Result;

/**
 * Created  : nicholai on 3/28/17.
 * Author   : Nicholai Mitchko
 * Package  : Scrapers.TripAdvisor.Responses.Common
 * Project Name : DistributedScraper
 */
public class Detail {
    private String parent_name;
    private String address;
    private String grandparent_name;
    private String highlighted_name;
    private String name;
    private int[] parent_ids;
    private String geo_name;

    public String getParent_name() {
        return parent_name;
    }

    public String getAddress() {
        return address;
    }

    public String getGrandparent_name() {
        return grandparent_name;
    }

    public String getName() {
        return name;
    }

    public int[] getParent_ids() {
        return parent_ids;
    }

    public String getGeo_name() {
        return geo_name;
    }

    @Override
    public String toString(){
        return this.getName() + " @ " + this.getAddress();
    }
}
