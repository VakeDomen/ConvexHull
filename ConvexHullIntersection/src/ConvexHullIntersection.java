import java.io.*;
import java.util.ArrayList;

public class ConvexHullIntersection {


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

        for (Point p : hullPoints[0]) {
            s1.addPoint(p);
        }
        for (Point p : hullPoints[1]) {
            s2.addPoint(p);
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
            return null;
        } else {
            int next = index + 1 % this.points.size();
            return this.points.get(next);
        }
    }
}

class Point {
    public double x;
    public double y;
}

class Vector {
    Point start;
    Point end;

    public Vector(Point start, Point end) {
        this.start = start;
        this.end = end;
    }
}