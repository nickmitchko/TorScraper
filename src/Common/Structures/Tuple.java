package Common.Structures;

/**
 * Created  : nicholai on 3/31/17.
 * Author   : Nicholai Mitchko
 * Package  : Common.Structures
 * Project  : DistributedScraper
 */
public class Tuple<X, Y> {
    public final X x;
    public final Y y;
    public Tuple(X a, Y b){
        this.x = a;
        this.y = b;
    }
}
