package solvv;

public class MODO_1 {
    
    // общее количество переменных
    final static int n = 3;
    
    // количество базисных переменных   
    final static int m = 3;
    
    //большое число
    final static double M = Double.MAX_VALUE;
 
    public static void main(String[] args) {
        // коэффициенты из целевой функции
        double[] c = new double[n];        
        // коэффициенты из ограничений
        double[][] a = new double[m][n];
        // коэффициенты при базисных переменных
        double[] cB = new double[m];
        // базисные переменные
        int[] bV = new int[m];
        // базисное решение
        double[] bD = new double[m];
 
        c[0] = 8;
        c[1] = 6;
        c[2] = 4;
 
        a[0][0] = 16; a[0][1] =18; a[0][2] = 9;
        a[1][0] = 7; a[1][1] =7; a[1][2] = 2;
        a[2][0] = 9; a[2][1] = 2; a[1][2] = 1;
 
        cB[0] = 0;
        cB[1] = 0;
        cB[2] = 0;
 
        bV[0] = 0;
        bV[1] = 1;
        bV[2] = 2;
 
        bD[0] = 520;
        bD[1] = 140;
        bD[2] = 810;
 
//        double[] min = simplexMin(c, a, cB, bV, bD);
//        printArray(min);
        
        double[] max = simplexMax(c, a, cB, bV, bD);
        printArray(max);
    }
 
    /**
     * Симплекс-метод. Подсчет минимума.
     */
    static double[] simplexMin(double[] c, double[][] a, double[] cB, int[] bV,
            double[] bD) {
        double[] delta = new double[n];
        double[] min = new double[m];
 
        System.out.println("\n\nПоиск минимума\n\n");
        int i, j, r = -1, s = -1;
        double[][] tmpA = new double[m][];
        double[] tmpBD = new double[m];
 
        int k = 0;
        while (true) {
            ++k;
 
            // Считаем дельты, выделяем разрешающий столбец
            // (столбец с минимальной оценкой)
            double deltaMin = Double.POSITIVE_INFINITY;
            r = -1;
            for (j = 0; j < n; ++j) {
                double z = 0;
                for (i = 0; i < m; ++i) {
                    z += cB[i] * a[i][j];
                }
                delta[j] = c[j] - z;
                if (deltaMin > delta[j]) {
                    deltaMin = delta[j];
                    r = j;
                }
            }
 
            // Условие выхода (все оценки неотрицательны)
            if (deltaMin >= 0 || k > 100) {
                break;
            }
 
            // Определяем разрешающую строку (с минимальным отношением
            // bD[i] / a[i][r])
            double minRow = Double.POSITIVE_INFINITY;
            s = -1;
            for (i = 0; i < m; ++i) {
                min[i] = bD[i] / a[i][r];
                if (min[i] < 0) {
                    min[i] = Double.NaN;
                }
                if (minRow > min[i]) {
                    minRow = min[i];
                    s = i;
                }
            }
 
            System.out.println("Разрешающий столбец: r = " + r);
            System.out.println("Разрешающая строка:  s = " + s);
            printTable(c, a, cB, bV, bD, min);
 
            // Разрешающий элемент (на пересечении разрешающих строки и столбца)
            double element = a[s][r];
 
            // Сохраняем содержимое массивов (т.к. их значения в ходе
            // вычисления изменяются, но остаются нужными для этих вычислений
            // ну как-то так :)
            for (i = 0; i < m; ++i) {
                tmpA[i] = new double[n];
                for (j = 0; j < n; ++j) {
                    tmpA[i][j] = a[i][j];
                }
                tmpBD[i] = bD[i];
            }
 
            // Вносим переменную в базис,
            // Пересчитываем базисное решение и коэффицетов переменных -
 
            // - в разрешающей строке
            bV[s] = r;
            cB[s] = c[r];
            for (j = 0; j < n; ++j) {
                a[s][j] /= element;
            }
            bD[s] /= element;
 
            // - в остальных строках
            for (i = 0; i < m; ++i) {
                // разрешающая строка уже пересчитана
                if (i == s) {
                    continue;
                }
 
                // элемент разрешающего столбца
                double air = tmpA[i][r];
 
                // пересчет коэфициентов
                for (j = 0; j < n; ++j) {
                    a[i][j] -= (air * tmpA[s][j]) / element;
                }
 
                // пересчет базисного решения
                bD[i] -= (air * tmpBD[s]) / element;
            }
 
            printDelta(delta);
            System.out.println("----------------------------------------------------");
        }
 
        System.out.println("Разрешающий столбец: r = " + (r + 1));
        System.out.println("Разрешающая строка:  s = " + (s + 1));
        printTable(c, a, cB, bV, bD, min);
        printDelta(delta);
        return printDecision(bV, bD);
    }
 
    /**
     * Симплекс-метод. Подсчет максимума.
     */
    static double[] simplexMax(double[] c, double[][] a, double[] cB, int[] bV,
            double[] bD) {
        double[] delta = new double[n];
        double[] min = new double[m];
 
        System.out.println("\n\nПоиск максимума\n\n");
        int i, j, r = -1, s = -1;
        double[][] tmpA = new double[m][];
        double[] tmpBD = new double[m];
 
        int k = 0;
        while (true) {
            ++k;
 
            // Считаем дельты, выделяем разрешающий столбец
            // (столбец с максимальной оценкой)
            double deltaMax = Double.NEGATIVE_INFINITY;
            r = -1;
            for (j = 0; j < n; ++j) {
                double z = 0;
                for (i = 0; i < m; ++i) {
                    z += cB[i] * a[i][j];
                }
                delta[j] = c[j] - z;
                if (deltaMax < delta[j]) {
                    deltaMax = delta[j];
                    r = j;
                }
            }
 
            // Условие выхода (все оценки неположительны)
            if (deltaMax <= 0 || k > 100) {
                break;
            }
 
            // Определяем разрешающую строку (с минимальным отношением
            // bD[i] / a[i][r])
            double minRow = Double.POSITIVE_INFINITY;
            s = -1;
            for (i = 0; i < m; ++i) {
                min[i] = bD[i] / a[i][r];
                if (min[i] < 0) {
                    min[i] = Double.NaN;
                }
                if (minRow > min[i]) {
                    minRow = min[i];
                    s = i;
                }
            }
 
            System.out.println("Разрешающий столбец: r = " + r);
            System.out.println("Разрешающая строка:  s = " + s);
            printTable(c, a, cB, bV, bD, min);
 
            // Разрешающий элемент (на пересечении разрешающих строки и столбца)
            double element = a[s][r];
 
            // Сохраняем содержимое массивов (т.к. их значения в ходе
            // вычисления изменяются, но остаются нужными для этих вычислений
            // ну как-то так :)
            for (i = 0; i < m; ++i) {
                tmpA[i] = new double[n];
                for (j = 0; j < n; ++j) {
                    tmpA[i][j] = a[i][j];
                }
                tmpBD[i] = bD[i];
            }
 
            // Вносим переменную в базис,
            // Пересчитываем базисное решение и коэффицетов переменных -
 
            // - в разрешающей строке
            bV[s] = r;
            cB[s] = c[r];
            for (j = 0; j < n; ++j) {
                a[s][j] /= element;
            }
            bD[s] /= element;
 
            // - в остальных строках
            for (i = 0; i < m; ++i) {
                // разрешающая строка уже пересчитана
                if (i == s) {
                    continue;
                }
 
                // элемент разрешающего столбца
                double air = tmpA[i][r];
 
                // пересчет коэфициентов
                for (j = 0; j < n; ++j) {
                    a[i][j] -= (air * tmpA[s][j]) / element;
                }
 
                // пересчет базисного решения
                bD[i] -= (air * tmpBD[s]) / element;
            }
 
            printDelta(delta);
            System.out.println("----------------------------------------------------");
        }
 
        System.out.println("Разрешающий столбец: r = " + (r + 1));
        System.out.println("Разрешающая строка:  s = " + (s + 1));
        printTable(c, a, cB, bV, bD, min);
        printDelta(delta);
        return printDecision(bV, bD);
    }
 
    /**
     * Печатает решение
     */
    static double[] printDecision(int[] bV, double[] bD) {
        int i, j;
        System.out.println("Все оценки неположительны, подсчет завершен.");
        System.out.print("Решение x = (");
        double[] res = new double[m];
        boolean f;
        for (j = 0; j < n; ++j) {
            f = false;
            for (i = 0; i < m; ++i) {
                if (bV[i] == j) {
                    if (j < m) {
                        res[j] = bD[i];
                    }
                    System.out.print(round(bD[i], 2));
                    f = true;
                    break;
                }
            }
            if (!f) {
                System.out.print("0");
            }
            if (j < n - 1) {
                System.out.print(", ");
            }
        }
        System.out.print(")");
        return res;
    }
 
    /**
     * Печатает строку оценок
     */
    static void printDelta(double[] delta) {
        System.out.print("\t\t\t\t");
        for (int j = 0; j < n; ++j) {
            System.out.print(round(delta[j], 2) + "\t");
        }
        System.out.println("delta[j]");
    }
 
    /**
     * Печатает таблицу
     */
    static void printTable(double[] c, double[][] a, double[] cB, int[] bV,
            double[] bD, double[] min) {
        int i, j;
        // вывод: строка коэффициентов
        System.out.print("\t\t\t\t");
        for (j = 0; j < n; ++j) {
            System.out.print(round(c[j], 2) + "\t");
        }
        System.out.println("C[j]");
 
        // вывод: ряд x
        System.out.print("\tcB\tbV\tbD\t");
        for (j = 0; j < n; ++j) {
            System.out.print("x[" + j + "]\t");
        }
        System.out.println("bD[i] / a[i][r]");
 
        for (i = 0; i < m; ++i) {
            System.out.print("\t" + round(cB[i], 2) + "\tx[" + (bV[i] + 1) + "]\t"
                    + round(bD[i], 2) + "\t");
            for (j = 0; j < n; ++j) {
                System.out.print(round(a[i][j], 2) + "\t");
            }
            System.out.println(round(min[i], 2));
        }
    }
    
    static void printArray(double[] ar)
    {
        System.out.println();
        for (int i = 0; i < ar.length; ++i) {
            System.out.println("\t[" + i + "] = " + ar[i]);
        }
        System.out.println();
    }
 
    static String round(double n, int p) {
        if (Double.isNaN(n)) {
            return "NaN";
        }
        if (Double.isInfinite(n)) {
            return "\u221E";
        }
        double d = Math.pow(10, p);
        return (Math.round(n * d) / d) + "";
    }
}

