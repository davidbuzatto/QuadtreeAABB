package quadtreeaabb;

import java.util.*;

public class Quadtree {
    
    private Quadnode root;
    private int maxTreeDepth;
    private AABB[] allAbbs;

    public static final int QUADTREE_SHAPE_SQUARE = 0;
    public static final int QUADTREE_SHAPE_RECTANGULAR = 1;

    public Quadtree( AABB[] aabbArray, int width, int height, int maxTreeDepth ) {
        this.allAbbs = aabbArray;
        this.maxTreeDepth = maxTreeDepth;
        reshape( width, height );
    }

    public Quadnode buildQuadtree( int x1, int y1, int x2, int y2, int treeDepth ) {
        
        Quadnode node = null;
        
        if ( treeDepth <= maxTreeDepth ) {
            node = new Quadnode( x1, y1, x2, y2, treeDepth );
            node.nw = buildQuadtree( x1, y1, node.xCenter, node.yCenter, treeDepth + 1 );
            node.ne = buildQuadtree( node.xCenter, y1, x2, node.yCenter, treeDepth + 1 );
            node.sw = buildQuadtree( x1, node.yCenter, node.xCenter, y2, treeDepth + 1 );
            node.se = buildQuadtree( node.xCenter, node.yCenter, x2, y2, treeDepth + 1 );
        }
        
        return node;
        
    }

    public AABB[] getAllAABBS() {
        return allAbbs;
    }

    public Quadnode getRoot() {
        return root;
    }

    public void insert( Quadnode node ) {

        LinkedList<AABB> aabbs = node.aabbs;

        if ( node.treeDepth < maxTreeDepth ) {
            if ( node.aabbs.size() > 1 ) {
                for ( AABB aabb : aabbs ) {
                    if ( aabb.y1 < node.yCenter ) {
                        if ( aabb.x1 < node.xCenter ) {
                            if ( node.nw != null ) {
                                node.nw.aabbs.add( aabb );
                            }
                        }
                        if ( aabb.x2 > node.xCenter ) {
                            if ( node.ne != null ) {
                                node.ne.aabbs.add( aabb );
                            }
                        }
                    }
                    if ( aabb.y2 > node.yCenter ) {
                        if ( aabb.x1 < node.xCenter ) {
                            if ( node.sw != null ) {
                                node.sw.aabbs.add( aabb );
                            }
                        }
                        if ( aabb.x2 > node.xCenter ) {
                            if ( node.se != null ) {
                                node.se.aabbs.add( aabb );
                            }
                        }
                    }
                }
                
                insert( node.nw );
                insert( node.ne );
                insert( node.sw );
                insert( node.se );
                
            }
            
        } else {
            for ( AABB aabb : aabbs ) {
                setNearby( aabbs, aabb );
            }
        }
    }

    private void resetQuadnodes( Quadnode node ) {
        if ( node != null ) {
            if ( !node.aabbs.isEmpty() ) {
                node.aabbs.clear();
            }
            resetQuadnodes( node.nw );
            resetQuadnodes( node.ne );
            resetQuadnodes( node.sw );
            resetQuadnodes( node.se );
        }
    }

    public void reshape( int width, int height ) {
        root = buildQuadtree( 0, 0, width, height, 0 );
    }

    public void setAllAABBs( AABB[] allAbbs ) {
        this.allAbbs = allAbbs;
    }

    public void setMaxTreeDepth( int mtd ) {
        if ( maxTreeDepth != mtd ) {
            maxTreeDepth = ( mtd < 1 || mtd > 10 ) ? maxTreeDepth : mtd;
            root = buildQuadtree( root.x1, root.y1, root.x2, root.y2, 0 );
            root.aabbs.addAll( Arrays.asList( allAbbs ) );
        }
    }

    private void setNearby( LinkedList<AABB> aabbList, AABB target ) {
        for ( AABB aabb : aabbList ) {
            aabb.nearby = target;
        }
    }

    public void update() {
        resetQuadnodes( root );
        root.aabbs.addAll( Arrays.asList( allAbbs ) );
        for ( AABB aabb : allAbbs ) {
            aabb.nearby = null;
        }
        insert( root );
    }

}
