import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        for (int i = 1; i <= 105; i++) {
            int k = i;
            String trainSetPath = "iris.data.txt";
            String testSetPath = "iris.test.data.txt";

            List<SetRow> traningSet = loadSet(trainSetPath);
            List<SetRow> testSet = loadSet(testSetPath);

            SetRow[][] closest = kClosest(k, traningSet, testSet);

            String[] results = traningResult(closest);

            outPutForExcel(i,compareResults(results, testSet, k));

        }

    }

    public static void outPutForExcel(int k, double results){
        List<String> excelOutPut = new LinkedList<>();
        excelOutPut.add("k,identified correctly");
        if (k%5==0){
            excelOutPut.add(k + "," + results + "%");
        }
        try {
            Files.write(Path.of("excelOutPut.txt"), excelOutPut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double compareResults(String[] results, List<SetRow> testSet, int k) {
        double good = 0;
        for (int i = 0; i < results.length; i++) {
            if (results[i].equals(testSet.get(i).result)) {
                good++;
            }
        }
        System.out.println("For k = " + k + "\t" + good / results.length * 100 + "% of results correct");
        return good / results.length;
    }

    public static String[] traningResult(SetRow[][] closest) {
        String[] results = new String[closest.length];
        for (int i = 0; i < results.length; i++) {
            Map<String, Integer> resultsAnalized = new HashMap<>();
            for (int j = 0; j < closest[0].length; j++) {
                if (resultsAnalized.containsKey(closest[i][j].result)) {
                    resultsAnalized.replace(closest[i][j].result, resultsAnalized.get(closest[i][j].result) + 1);
                } else {
                    resultsAnalized.put(closest[i][j].result, 1);
                }
            }
            int tmp = 0;
            for (Map.Entry<String, Integer> entry : resultsAnalized.entrySet()) {
                if (tmp < entry.getValue()) {
                    tmp = entry.getValue();
                    results[i] = entry.getKey();
                }
            }
        }
        return results;
    }

    public static SetRow[] isCloser(SetRow[] closest, SetRow traningSetRow, SetRow testSetRow) {
        for (int i = 0; i < closest.length; i++) {
            if (closest[i] == null) {
                closest[i] = traningSetRow;
                return closest;
            } else {
                if (closest[i].distanceApart(testSetRow) > traningSetRow.distanceApart(testSetRow)) {
                    SetRow tmp = closest[i];
                    closest[i] = traningSetRow;
                    return isCloser(closest, tmp, testSetRow);
                }
            }
        }
        return closest;
    }

    public static SetRow[][] kClosest(int k, List<SetRow> traningSet, List<SetRow> testSet) {
        SetRow[][] closest = new SetRow[testSet.size()][k];
        for (int i = 0; i < testSet.size(); i++) {
            for (int j = 0; j < traningSet.size(); j++) {
                closest[i] = isCloser(closest[i], traningSet.get(j), testSet.get(i));
            }
        }
        return closest;
    }

    public static List<SetRow> loadSet(String path) {
        try {
            Scanner sc = new Scanner(Path.of(path));
            List<SetRow> setList = new LinkedList<>();
            while (sc.hasNextLine()) {
                String[] tmp = sc.nextLine().split(",");
                double tmpDouble[] = new double[tmp.length - 1];
                for (int i = 0; i < tmp.length - 1; i++) {
                    tmpDouble[i] = Double.parseDouble(tmp[i]);
                }
                setList.add(new SetRow(tmpDouble, tmp[tmp.length - 1]));
            }
            return setList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void promptUser() {
        Scanner sc = new Scanner(System.in);
        System.out.print("podaj liczbe k:\t");
        int k = sc.nextInt();
        System.out.print("\nPodaj sciezke do traing set:\t");
        String trainSetPath = sc.next();
        System.out.print("\nPodaj sciezke do test set:\t");
        String testSetPath = sc.next();
    }
}