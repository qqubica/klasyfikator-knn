import java.util.Arrays;

public class SetRow {
    double [] parameters;
    String result;

    public SetRow(double[] parameters, String result) {
        this.parameters = parameters;
        this.result = result;
    }

    public double[] getParameters() {
        return parameters;
    }

    public double distanceApart(SetRow check){
        double distance = 0;
        for (int i = 0; i < parameters.length; i++) {
            distance += Math.pow(parameters[i]-check.parameters[i],2);
        }
        return Math.sqrt(distance);
    }

    @Override
    public String toString() {
        String tmp = "";
        for (int i = 0; i < parameters.length; i++) {
            tmp += parameters[i] + " ";
        }
        tmp += result;
        return tmp;
    }

    public String getResult() {
        return result;
    }
}
