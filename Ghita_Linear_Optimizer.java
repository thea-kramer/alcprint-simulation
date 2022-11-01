import gurobi.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Ghita_Linear_Optimizer {

    public static GRBModel setVars(GRBModel model, double[] m, double[] sd, GRBVar[] x, int j) throws GRBException {
        GRBLinExpr expr = new GRBLinExpr();
        //probabilities' average is constrained to be the median
        for (int i = 1; i < 5; i++) {
            expr.addTerm(i, x[i]);
        }
        model.addConstr(expr, GRB.EQUAL, m[j], "c0");

        //probabilities have the standard deviation as a constraint
        expr = new GRBLinExpr();
        for (int i = 0; i < 5; i++) {
            expr.addTerm(Math.pow((i - m[j]), 2), x[i]);
        }
        model.addConstr(expr, GRB.EQUAL, Math.pow(sd[j], 2), "c2");
        return model;
    }

    public static void writeToFile(StringBuilder sb) {

        try (PrintWriter writer = new PrintWriter("ghita_craving_data.csv")) {
            writer.write(sb.toString());

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        /*
        0 Drinking alone                      1.06 (1.22) 1.87 (1.21) 2.64∗
        1 Drinking with two or more friends   1.2 (1.07) 1.48 (1.12) 1.02
        2 Drinking with a friend              1.1 (0.96) 1.3 (1.18) 0.79
        3 Drinking at a party                 1.76 (1.35) 1.87 (1.42) 0.3
        4 Drinking in a restaurant            1.43 (1.1) 2.04 (1.33) 2.07∗
        5 Drinking in a bar                   1.41 (1.34) 1.78 (1.47) 1.06
        6 Drinking in a pub                   1.04 (1.45) 1.48 (1.75) 1.12
        7 Drinking at home                    1 (0.98) 1.43 (1.27) 1.6
        8 Drinking at night                   1.49 (1.43) 1.96 (1.52) 1.27
        9 Drinking during the afternoon       1.29 (1.27) 1.39 (1.46) 0.29
        10 Drinking on a Friday                1.51 (1.33) 1.83 (1.49) 0.9
        11 Drinking on a Saturday              1.67 (1.4) 1.78 (1.47) 0.32
        12 Drinking while anxious/tense        1.12 (1.35) 2.13 (1.48) 2.89∗
        13 Drinking while sad                  1.12 (1.3) 2.17 (1.26) 3.24∗
        14 Drinking while stressed             1.1 (1.47) 1.96 (1.63) 2.24∗
        15 Drinking while frustrated           1.06 (1.46) 2.09 (1.59) 2.72∗
        16 Drinking beer                       1.96 (1.31) 1.65 (1.64) 0.8
        17 Drinking red wine                   1.06 (1.27) 1.3 (1.46) 0.73
        18 Drinking whisky                     0.98 (1.31) 0.65 (1.3) 0.99
        */

        double[] M_m = {1.06, 1.2, 1.1, 1.76, 1.43, 1.41, 1.04, 1, 1.49, 1.29, 1.51, 1.67, 1.12, 1.12, 1.1, 1.06, 1.96, 1.06, 0.98};
        double[] SD_m = {1.22, 1.07, 0.96, 1.35, 1.1, 1.34, 1.45, 0.98, 1.43, 1.27, 1.33, 1.4, 1.35, 1.3, 1.47, 1.46, 1.31, 1.27, 1.31};
        double[] M_f = {1.87, 1.48, 1.3, 1.87, 2.04, 1.78, 1.48, 1.43, 1.96, 1.39, 1.83, 1.78, 2.13, 2.17, 1.96, 2.09, 1.65, 1.3, 0.65};
        double[] SD_f = {1.21, 1.12, 1.18, 1.42, 1.33, 1.47, 1.75, 1.27, 1.52, 1.46, 1.49, 1.47, 1.48, 1.26, 1.63, 1.59, 1.64, 1.46, 1.3};
        String[] variables = {
                "Drinking alone",
                "Drinking with two or more friends",
                "Drinking with a friend",
                "Drinking at a party",
                "Drinking in a restaurant",
                "Drinking in a bar",
                "Drinking in a pub",
                "Drinking at home",
                "Drinking at night",
                "Drinking during the afternoon",
                "Drinking on a Friday",
                "Drinking on a Saturday",
                "Drinking while anxious/tense",
                "Drinking while sad",
                "Drinking while stressed",
                "Drinking while frustrated",
                "Drinking beer",
                "Drinking red wine",
                "Drinking whisky"
        };
        StringBuilder sb = new StringBuilder();
        sb.append(",MALES,,,,FEMALES\n");
        sb.append(",max,min,min,max,max,min,min,max\n");
        sb.append(",0,>0,0,>0,0,>0,0,>0\n");

        try {

            for (int j = 0; j < M_m.length; j++) {
                sb.append(variables[j] + ",");
                for (int k = 0; k < 2; k++) {
                    GRBEnv env = new GRBEnv("alcprint.log");
                    GRBModel model = new GRBModel(env);

                    GRBVar[] x = new GRBVar[5];
                    x[0] = model.addVar(0.0, 1.0, 0.0, GRB.CONTINUOUS, "Var 0");
                    for (int i = 1; i < 5; i++) {
                        x[i] = model.addVar(0.0, 1.0, 1.0, GRB.CONTINUOUS, "Var " + i);
                    }

                    //probabilities add up to 1
                    GRBLinExpr expr = new GRBLinExpr();
                    for (int i = 0; i < 5; i++) {
                        expr.addTerm(1.0, x[i]);
                    }
                    model.addConstr(expr, GRB.EQUAL, 1.0, "c1");

                    if(k==0)
                        model = setVars(model, M_m, SD_m, x, j);
                    else
                        model = setVars(model, M_f, SD_f,x, j);

                    // Optimize Model
                    //model.setObjective(expr, GRB.MAXIMIZE);
                    model.set(GRB.IntAttr.ModelSense, GRB.MINIMIZE);
                    model.optimize();


                    sb.append(x[0].get(GRB.DoubleAttr.X) + ",");
                    sb.append(model.get(GRB.DoubleAttr.ObjVal)+ ",");

                    model.set(GRB.IntAttr.ModelSense, GRB.MAXIMIZE);
                    model.optimize();
                    sb.append(x[0].get(GRB.DoubleAttr.X)+",");
                    sb.append(model.get(GRB.DoubleAttr.ObjVal)+",");

                    // Dispose of model and environment
                    model.dispose();
                    env.dispose();
                }
                sb.append("\n");
            }
            writeToFile(sb);
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " +
                    e.getMessage());
        }
    }
}
