package quadtreeaabb;

public class AABB {

    public double x1;
    public double y1;
    public double x2;
    public double y2;
    public double width;
    public double height;
    public double vx;
    public double vy;
    public AABB nearby;

    public AABB() {
        x1 = 0;
        y1 = 0;
        x2 = 50;
        y2 = 50;
        vx = 0;
        vy = 0;
        width = x2;
        height = y2;
        nearby = null;
    }

    public AABB( int x1, int y1, int x2, int y2 ) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.vx = 0;
        this.vy = 0;
        this.width = x2 - x1;
        this.height = y2 - y1;
        this.nearby = null;
    }

    public boolean collidesWith( AABB aabb ) {

        if ( x1 > aabb.x2 || aabb.x1 > x2 ) {
            return false;
        }

        if ( y2 < aabb.y1 || aabb.y2 < y1 ) {
            return false;
        }

        return true;

    }

    public void relocate( double deltaX, double deltaY ) {
        x1 += deltaX;
        y1 += deltaY;
        x2 += deltaX;
        y2 += deltaY;
    }

    public void setSize( double w, double h ) {
        x2 = x1 + w;
        y2 = y1 + h;
        width = x2 - x1;
        height = y2 - y1;
    }

    public void setVelocity( double vx, double vy ) {
        this.vx = vx;
        this.vy = vy;
    }

}
