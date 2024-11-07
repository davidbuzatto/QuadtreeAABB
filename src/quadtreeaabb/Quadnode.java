package quadtreeaabb;

import java.util.LinkedList;

/**
 * Um nó da quadtree.
 * 
 * @author Prof. Dr. David Buzatto
 */
public class Quadnode {

    public int x1;
    public int y1;
    public int x2;
    public int y2;
    public int xCenter;
    public int yCenter;
    public int treeDepth;

    public Quadnode nw;
    public Quadnode ne;
    public Quadnode sw;
    public Quadnode se;

    public LinkedList<AABB> aabbs;

    public Quadnode( int x1, int y1, int x2, int y2, int treeDepth ) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.xCenter = ( x1 + x2 ) / 2;
        this.yCenter = ( y1 + y2 ) / 2;
        this.treeDepth = treeDepth;
        this.aabbs = new LinkedList<>();
    }

}
