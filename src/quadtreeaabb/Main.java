package quadtreeaabb;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.core.utils.ColorUtils;
import br.com.davidbuzatto.jsge.geom.Rectangle;
import br.com.davidbuzatto.jsge.math.CollisionUtils;
import br.com.davidbuzatto.jsge.math.MathUtils;
import java.awt.Color;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Teste Quadtree para AABBs.
 * 
 * @author Prof. Dr. David Buzatto
 */
public class Main extends EngineFrame {
    
    private Color qnOutlineColor;
    private Color commonQnColor;
    private Color aabbColor;
    private Color aabbNearbyColor;
    private Color aabbOverlapColor;
    
    private int width;
    private int height;
    private int numberOfAABBs;
    private int maxTreeDepth;
    
    private AABB[] aabbs;
    private Quadtree quadtree;
    private List<Rectangle> overlaps;
    
    public Main() {
        super ( 1000, 1000, "AABB Quad Tree Test", 60, true );
    }
    
    @Override
    public void create() {
        
        width = getScreenWidth();
        height = getScreenHeight();
        
        qnOutlineColor = new Color( 0, 0, 0 );
        commonQnColor = new Color( 150, 150, 150 );
        
        aabbColor = ColorUtils.fade( GOLD, 0.7 );
        aabbNearbyColor = ColorUtils.fade( LIME, 0.7 );
        aabbOverlapColor = ColorUtils.fade( BLUE, 0.7 );
        
        numberOfAABBs = 200;
        maxTreeDepth = 5;

        initAABBs();
        quadtree = new Quadtree( aabbs, width, height, maxTreeDepth );
        setQuadtreeShape( Quadtree.QUADTREE_SHAPE_SQUARE );
        
        overlaps = new CopyOnWriteArrayList<>();
        
    }
    
    @Override
    public void update( double delta ) {
        updateAABBLocations( delta );
        quadtree.update();
    }
    
    @Override
    public void draw() {
        
        clearBackground( WHITE );

        drawQuadTree( this );
        drawAABBs( this );
        
        for ( Rectangle r : overlaps ) {
            r.fill( this, aabbOverlapColor );
        }
        
        drawFPS( 20, 20 );
    
    }

    private void initAABBs() {
        
        aabbs = new AABB[numberOfAABBs];
        
        for ( int i = 0; i < aabbs.length; i++ ) {
            aabbs[i] = new AABB();
            aabbs[i].setSize( MathUtils.getRandomValue( 5, height / 15 ), MathUtils.getRandomValue( 5, height / 15 ) );
            aabbs[i].setVelocity( MathUtils.getRandomValue( -100, 100 ), MathUtils.getRandomValue( -100, 100 ) );
            aabbs[i].relocate( MathUtils.getRandomValue( 1, (int) ( width - aabbs[i].x2 - aabbs[i].x1 - 2 ) ), MathUtils.getRandomValue( 1, (int) ( height - aabbs[i].y2 - aabbs[i].y1 - 2 ) ) );
        }
        
    }
    
    private void drawAABBs( EngineFrame e ) {
        for ( AABB aabb : aabbs ) {
            e.fillRectangle( aabb.x1, aabb.y1, aabb.width, aabb.height, aabb.nearby == null ? aabbColor : aabbNearbyColor );
        }
    }
    
    private void drawQuadnode( EngineFrame e, Quadnode node ) {
        
        if ( node.treeDepth < maxTreeDepth ) {
            
            if ( node.aabbs.size() > 1 ) {
                if ( node.treeDepth == maxTreeDepth - 1 ) {
                    e.fillRectangle( node.x1, node.y1, node.x2 - node.x1, node.y2 - node.y1, commonQnColor );
                }
                e.drawRectangle( node.x1, node.y1, node.x2 - node.x1, node.y2 - node.y1, qnOutlineColor );
            }

            for ( AABB a : node.aabbs ) {
                for ( AABB b : node.aabbs ) {
                    if ( a != b ) {
                        Rectangle ra = new Rectangle( a.x1, a.y1, a.x2 - a.x1, a.y2 - a.y1 );
                        Rectangle rb = new Rectangle( b.x1, b.y1, b.x2 - b.x1, b.y2 - b.y1 );
                        if ( CollisionUtils.checkCollisionRectangles( ra, rb ) ) {
                            Rectangle ri = CollisionUtils.getCollisionRectangle( ra, rb );
                            overlaps.add( ri );
                        }
                    }
                }
            }
            
            drawQuadnode( e, node.nw );
            drawQuadnode( e, node.ne );
            drawQuadnode( e, node.sw );
            drawQuadnode( e, node.se );
            
        }
        
    }
    
    private void drawQuadTree( EngineFrame e ) {
        overlaps.clear();
        drawQuadnode( e, quadtree.getRoot() );
    }
    
    private void setQuadtreeShape( int shape ) throws IllegalArgumentException {
        
        if ( shape != Quadtree.QUADTREE_SHAPE_SQUARE && shape != Quadtree.QUADTREE_SHAPE_RECTANGULAR ) {
            throw new IllegalArgumentException(
                    shape + " is not recognized as a valid argument for Quadtree shape" );
        } else {
            
            int quadtreeWidth = width;
            int quadtreeHeight = height;

            if ( shape == Quadtree.QUADTREE_SHAPE_SQUARE ) {
                quadtreeWidth = quadtreeHeight = width > height ? width - 1 : height - 1;
            }
            
            quadtree.reshape( quadtreeWidth, quadtreeHeight );
            
        }
        
    }
    
    private void updateAABBLocations( double delta ) {
        
        for ( AABB aabb : aabbs ) {
            
            if ( aabb.x1 <= 0 || aabb.x2 >= width  ) {
                aabb.vx = -aabb.vx;
            }
            if ( aabb.y1 <= 0 || aabb.y2 >= height ) {
                aabb.vy = -aabb.vy;
            }
            
            aabb.relocate( aabb.vx * delta, aabb.vy * delta );
            
        }
        
    }
    
    public static void main( String[] args ) {
        new Main();
    }
    
}
