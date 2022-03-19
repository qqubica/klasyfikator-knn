import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int k = 3;
        String trainSetPath = "iris.data.txt";
        String testSetPath = "iris.test.data.txt";

        List<SetRow> traningSet = loadSet(trainSetPath);
        List<SetRow> testSet = loadSet(testSetPath);

        SetRow[][] closest = kClosest(k,traningSet,testSet);

        traningResult(closest, testSet);

    }

    public static void traningResult(SetRow[][] closest, List<SetRow> testSet){
        for (int i = 0; i < testSet.size(); i++) {
            for (int j = 0; j < closest[i].length; j++) {
                System.out.print("Closest results:\t" + closest[i][j].getResult() );
            }
            System.out.println("result should be\t" + testSet.get(i).result);
        }
    }

    public static SetRow[] isCloser(SetRow[] closest, SetRow traningSetRow, SetRow testSetRow){
        if (closest.equals(null)){
            closest = new SetRow[3];
        }
        for (int i = 0; i < closest.length; i++) {
            if (closest[i].distanceApart(testSetRow) > traningSetRow.distanceApart(testSetRow)){
                closest[i] = testSetRow;
            }
        }
        return closest;
    }

    public static SetRow[][] kClosest(int k, List<SetRow> traningSet, List<SetRow> testSet){
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