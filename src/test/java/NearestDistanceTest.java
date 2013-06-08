import com.savarese.spatial.GenericPoint;
import com.savarese.spatial.KDTree;
import com.savarese.spatial.NearestNeighbors;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.zip.GZIPInputStream;

/**
 * User: ddumanskiy
 * Date: 6/7/13
 * Time: 8:54 PM
 */
public class NearestDistanceTest {


    private static Map<GenericPoint<Double>, Location> points = new HashMap<GenericPoint<Double>, Location>();
    private static KDTree<Double, GenericPoint<Double>, Location> tree = new KDTree<Double, GenericPoint<Double>, Location>();


    private static Random random = new Random();
    private final static int latRangeMin = -90;
    private final static int latRangeMax = 90;
    private final static int lonRangeMin = -180;
    private final static int lonRangeMax = 180;

    public static double getRandomLat() {
        return random.nextDouble() * (latRangeMax - latRangeMin) + latRangeMin;
    }
    public static double getRandomLon() {
        return random.nextDouble() * (lonRangeMax - lonRangeMin) + lonRangeMin;
    }


    @BeforeClass
    public static void init() throws Exception {
        BufferedReader in =
            new BufferedReader(
                new InputStreamReader(
                        new GZIPInputStream(
                                NearestDistanceTest.class.getResourceAsStream("/worldcitiespop.txt.gz"))));

        String line;
        while ((line = in.readLine()) != null) {
            String[] values = line.split(",");
            try {
                double lat = Double.valueOf(values[5]);
                double lon = Double.valueOf(values[6]);

                //country, city, region
                points.put(new GenericPoint<Double>(lat, lon), new Location(values[0], values[1], values[3]));
            } catch (Exception e) {
                System.err.println("Error parsing string : " + line);
            }
        }
        System.out.println("Read points from file : " + points.size());

        tree.putAll(points);
    }

    @Test
    public void performTests() {
        performance3TimesTest();
        performance3TimesBalancedTreeTest();
    }

    private void performance3TimesTest() {
        System.out.println("Tree size = " + tree.size());
        makeTest(10000, tree);
        makeTest(10000, tree);
        makeTest(10000, tree);
    }

    private void performance3TimesBalancedTreeTest() {
        long start = System.currentTimeMillis();

        System.out.println("Starting balancing tree. Size : " + tree.size());
        tree.optimize();
        System.out.println("Time for tree balancing took : " + (System.currentTimeMillis() - start) + " ms.");

        makeTest(10000, tree);
        makeTest(10000, tree);
        makeTest(10000, tree);

    }

    private static void makeTest(int iterations, KDTree<Double, GenericPoint<Double>, Location> tree) {
        NearestNeighbors<Double, GenericPoint<Double>, Location> nn = new NearestNeighbors<Double, GenericPoint<Double>, Location>();
        GenericPoint<Double> point;

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            point = new GenericPoint<Double>(getRandomLat(), getRandomLon());
            NearestNeighbors.Entry<Double, GenericPoint<Double>, Location>[] n = nn.get(tree, point, 1, false);

            Assert.assertNotNull(n);
            Assert.assertNotNull(n[0]);
        }

        System.out.println("Test result with " + iterations + " iterations : " + (double) (System.currentTimeMillis() - startTime) / iterations + " ms per 1 get.");
    }


}
