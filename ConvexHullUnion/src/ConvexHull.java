import java.awt.geom.Line2D;
import java.io.*;
import java.util.ArrayList;

public class ConvexHull {

    private static final String INPUT_PATH = "./src/input.txt";

    static Hull s1;
    static Hull s2;

    static Hull union;


    public static void main(String[] args) {
        ArrayList<Point>[] hullPoints;

        hullPoints = readInputHulls();

        s1 = new Hull();
        s2 = new Hull();
        union = new Hull();


        // -------------------------------------union---------------------------------------------------
        for (Point p : hullPoints[0]) {
            s1.addPoint(p);
        }
        for (Point p : hullPoints[1]) {
            s2.addPoint(p);
        }


        Point anchorPoint, referencePoint;
        anchorPoint = s1.points.get(0);
        referencePoint = s1.nextCounterClockwisePoint(anchorPoint);

        Hull anchorHull, referenceHull;
        anchorHull = s1;
        referenceHull = s1;

        boolean calculatingHullUnion = true;
        while (calculatingHullUnion) {

            Vector borderLine = new Vector(anchorPoint, referencePoint);
            // since anchor point is in a hull, we know that all points in that hull are on the left
            // we now start checking other hull
            if (anchorHull == s1) {
                referenceHull = s2;
            }
            if (anchorHull == s2){
                referenceHull = s1;
            }

            boolean allToLeft = true;
            Point pointToRight = null;
            for (Point p : referenceHull.points) {
                if  (borderLine.isToRight(p)) {
                    allToLeft = false;
                    pointToRight = p;
                    System.out.println("Point to right: " + p.toString());
                    break;
                }
            }

            if (allToLeft) {
                // add reference point to union hull
                union.addPoint(referencePoint);
                // advance vector points
                anchorPoint = referencePoint;
                // next point in hull cycle
                if (anchorHull.contains(referencePoint)) {
                    referencePoint = anchorHull.nextCounterClockwisePoint(referencePoint);
                } else {
                    referencePoint = referenceHull.nextCounterClockwisePoint(referencePoint);
                }
                // save the hull of current anchor point
                anchorHull = referenceHull;
            } else {
                // take point that is to the right of reference point and replace reference point
                referencePoint = pointToRight;
            }

            // if we made a cycle in union hull
            if (union.contains(referencePoint)) {
                calculatingHullUnion = false;
            }
        }



        // output
        for (Point p : union.points) {
            System.out.print(" " + p.x);
        }
        System.out.println();
        for (Point p : union.points) {
            System.out.print(" " + p.y);
        }
        System.out.println();


        // -------------------------------------intersection---------------------------------------------------

        //inital vectors
        Vector v1 = new Vector(s1.points.get(0), s1.nextCounterClockwisePoint(s1.points.get(0)));
        Vector v2 = new Vector(s2.points.get(0), s2.nextCounterClockwisePoint(s2.points.get(0)));

        boolean intersection = false;
        // boolean values that store wether the vector has made a full loop around hull
        boolean s1cycle = false, s2cycle = false;
        while (true) {
            //check if intersection found
            if (v1.intersects(v2)) {
                intersection = true;
                break;
            }
            // if cycles have completed, stop
            if (v1.end == s1.points.get(0)) {
                s1cycle = true;
            }
            if (v2.end == s2.points.get(0)) {
                s2cycle = true;
            }
            if (s1cycle && s2cycle) {
                break;
            }

            // store if vectors point to one another
            boolean v1pointsTov2 = v1.pointsInto(v2);
            boolean v2pointsTov1 = v2.pointsInto(v1);

            if ((v1pointsTov2 && v2pointsTov1) || (!v1pointsTov2 && !v2pointsTov1)) {
                // move vector on the outside (to the right, since we are going counterclockwise)
                if (v1.isToRight(v2.start)) {
                    v2 = new Vector(v2.end, s2.nextCounterClockwisePoint(v2.end));
                } else {
                    v1 = new Vector(v1.end, s1.nextCounterClockwisePoint(v1.end));
                }
            } else if (v1pointsTov2) {
                v1 = new Vector(v1.end, s1.nextCounterClockwisePoint(v1.end));
            } else if (v2pointsTov1) {
                v2 = new Vector(v2.end, s2.nextCounterClockwisePoint(v2.end));
            }



        }

        if (intersection) {
            System.out.println("TRUE");
        } else {
            System.out.println("FALSE");
        }


    }

    private static ArrayList[] readInputHulls() {
        BufferedReader reader = null;
        ArrayList<Point>[] hullPoints = new ArrayList[2];
        try {
            reader = new BufferedReader(new FileReader(new File(INPUT_PATH).getAbsolutePath()));
            String line;
            int lineCounter = -1;


            while ((line = reader.readLine()) != null) {
                lineCounter++;
                if (line == "\n") {
                    continue;
                }
                // s1 x
                // crate points for first hull and fill in x values
                if (lineCounter == 0) {
                    String[] s1x = line.split(" ");
                    hullPoints[0] = new ArrayList<>();
                    for (String x : s1x) {
                        Point p = new Point();
                        p.x = Double.parseDouble(x);
                        hullPoints[0].add(p);
                    }
                    continue;
                }

                // s1 y
                // add y values to s1 points
                if (lineCounter == 1) {
                    String[] s1y = line.split(" ");
                    for (int i = 0 ; i < s1y.length ; i++) {
                        Point p = hullPoints[0].get(i);
                        p.y = Double.parseDouble(s1y[i]);
                        hullPoints[0].set(i, p);
                    }
                    continue;
                }

                // s2 x
                if (lineCounter == 2) {
                    String[] s2x = line.split(" ");
                    hullPoints[1] = new ArrayList<>();
                    for (String x : s2x) {
                        Point p = new Point();
                        p.x = Double.parseDouble(x);
                        hullPoints[1].add(p);
                    }
                    continue;
                }

                // s2 y
                if (lineCounter == 3) {
                    String[] s2y = line.split(" ");
                    for (int i = 0 ; i < s2y.length ; i++) {
                        Point p = hullPoints[1].get(i);
                        p.y = Double.parseDouble(s2y[i]);
                        hullPoints[1].set(i, p);
                    }
                    continue;
                }


            }



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hullPoints;
    }
}



class Hull {

    ArrayList<Point> points;

    public Hull() {
        this.points = new ArrayList<>();
    }

    public void addPoint(Point p) {
        this.points.add(p);
    }

    public Point nextCounterClockwisePoint(Point p) {
        int index = this.points.indexOf(p);
        // p not in hull
        if (index == -1) {
            System.out.println("not in");
            return null;
        } else {
            int next = (index + 1) % this.points.size();
            return this.points.get(next);
        }
    }

    public boolean contains(Point p) {
        return points.contains(p);
    }
}

class Point {
    public double x;
    public double y;

    public double distTo(Point p) {
        return Math.sqrt((p.y - y) * (p.y - y) + (p.x - x) * (p.x - x));
    }

    @Override
    public String toString() {
        return "(" + x + " | " + y + ")";
    }
}

class Vector {
    Point start;
    Point end;

    public Vector(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    public boolean isToLeft(Point p) {
        double ax, ay, px, py;
        ax = end.x - start.x;
        ay = end.y - start.y;
        px = p.x - start.x;
        py = p.y - start.y;
        return ((ax * py) - (ay * px)) >= 0;
        //return ((end.x - start.x)*(p.y - start.y) - (end.y - start.y)*(p.x - start.x)) >= 0;
    }

    public boolean isToRight(Point p) {
        double ax, ay, px, py;
        ax = end.x - start.x;
        ay = end.y - start.y;
        px = p.x - start.x;
        py = p.y - start.y;
        return ((ax * py) - (ay * px)) < 0;
        //return ((end.x - start.x)*(p.y - start.y) - (end.y - start.y)*(p.x - start.x)) < 0;
    }

    public boolean intersects(Vector v) {
        return Line2D.linesIntersect(
            start.x, start.y,
            end.x, end.y,
            v.start.x, v.start.y,
            v.end.x, v.end.y
        );
    }

    public boolean pointsInto(Vector v) {
        return (perpendicularProjection(v.start).distTo(v.start) > perpendicularProjection(v.end).distTo(v.end));
    }

    private Point perpendicularProjection(Point p) {
        Point projection = new Point();
        double x1=start.x, y1=start.y, x2=end.x, y2=end.y, x3=p.x, y3=p.y;
        double px = x2-x1, py = y2-y1, dAB = px*px + py*py;
        double u = ((x3 - x1) * px + (y3 - y1) * py) / dAB;
        projection.x = x1 + u * px;
        projection.y = y1 + u * py;
        return projection;
    }
}