//package rhyme;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.Map;
//
//import utils.Pair;
//import utils.Utils;
//
//public class HirjeeMatrix {
//
//    private static double[][] matrix = load();
//    private static final String hirjeeFilePath = Utils.rootPath + "local-data/phonemes/rhyme/hirjee/hirjeeMatrix.txt";
//
//    public static double[][] load() {
//        if (matrix == null) {
//
//            BufferedReader bf;
//            try {
//                bf = new BufferedReader(new FileReader(hirjeeFilePath));
//
//                String line = bf.readLine();
//                String[] lineSplit = line.split("\t");
//                int matrixWidth = lineSplit.length - 1;
//                double value;
//                matrix = new double[matrixWidth][matrixWidth];
//                // System.out.println(Arrays.toString(lineSplit));
//                // System.out.println(matrix.length);
//                // System.out.println(matrix[1].length);
//
//                int lineNum = 0;
//                while ((line = bf.readLine()) != null) {
//                    lineSplit = line.split("\t");
//                    for (int i = lineNum; i < matrixWidth; i++) {
//                        if (i + 1 >= lineSplit.length || lineSplit[i + 1].length() == 0) {
//                            value = -50.0;
//                        } else {
//                            value = Double.parseDouble(lineSplit[i + 1]) * 10.;
//                        }
//
//                        matrix[lineNum][i] = value;
//                        matrix[i][lineNum] = value;
//                    }
//                    lineNum++;
//                }
//
//                bf.close();
//            } catch (NumberFormatException | IOException e) {
//                e.printStackTrace();
//                throw new RuntimeException();
//            }
//        }
//
//        return matrix;
//    }
//
//    public static void main(String[] args) throws IOException {
//        double[][] matrix = HirjeeMatrix.load();
//        Map<String, Pair<Integer, MannerOfArticulation>> phonesDict = Phoneticizer.loadPhonesDict();
//
//        for (double[] array : matrix) {
//            for (double num : array)
//                System.out.print(num + "\t");
//            System.out.println();
//        }
//
//        for (String phone : phonesDict.keySet()) {
//            System.out.println("AA with " + phone + ": "
//                    + matrix[phonesDict.get(phone).getFirst()][phonesDict.get("AA").getFirst()]);
//        }
//    }
//
//    public static double score(int phone, int phone2) {
//        // TODO Auto-generated method stub
//        return matrix[phone][phone2];
//    }
//
//}
