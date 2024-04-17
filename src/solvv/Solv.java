package solvv;
import it.ssc.log.SscLogger;
import it.ssc.pl.milp.LP;
import it.ssc.pl.milp.Solution;
import it.ssc.pl.milp.SolutionType;
import it.ssc.pl.milp.Variable;
import it.ssc.ref.InputString;

public class Solv {
    public static String lp_string;
    public static String format="";
    public static String answer = "";
    
    public static double[][] phase_one;
    public static double[][] phase_two;
    
    public static String Solve(String str, int col) throws Exception {
        format="";
        answer = "";
        lp_string="";
        InputString lp_input = new InputString(str);
        
        for(int i=1; i<=col;i++) {
            format+="X"+i+":double, ";
        }
        
        lp_input.setInputFormat(format + "TYPE:varstring(5), RHS:double");

        LP lp = new LP(lp_input);
        SolutionType solution_type = lp.resolve();
        
        if (solution_type == SolutionType.OPTIMUM) {
            Solution solution = lp.getSolution();
            for (Variable var : solution.getVariables()) {
                SscLogger.log(var.getName() + " value:" + var.getValue());
                answer+="" + var.getName() + " value:" + var.getValue()+"\n";
            }
            SscLogger.log("o.f. value:" + solution.getOptimumValue());
            answer+="o.f. value:" + solution.getOptimumValue();
        } else {
            SscLogger.log("no optimal solution:" + solution_type);
            answer+="no optimal solution:" + solution_type;
        }
        
        printTable(phase_one);
        printTable(phase_two);
        return answer;
    }
    static void printTable(double tb[][]) {
        for(int _i=0;_i<tb.length;_i++) {
            System.out.println("");
            for(int _j=0;_j<tb[0].length;_j++) {
                double val=tb[_i][_j];
                System.out.printf("\t : %7.2f",val);
            }
        }
        System.out.println("");
    }
    static String saveTable(double tb[][]) {
        String ans="";
        for(int _i=0;_i<tb.length;_i++) {
            for(int _j=0;_j<tb[0].length;_j++) {
                double val=tb[_i][_j];
                ans+=String.format("%.2f", val)+" \t";
            }
            ans+="\n";
        }
        return ans;
    }
}


