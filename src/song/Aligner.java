package song;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import song.SequencePair.AlignmentBuilder;
//import song.LyricSheet;
//import song.RawDataLoader;
//import tabcomplete.utils.StopWatch;
//import tabcomplete.utils.Utils;

/*
 * This is a generic, banded, linear-time, linear-space, pairwise aligner
 */
public class Aligner{

    private static final char DIAGONAL = 'D';
    private static final char UP = 'U';
    private static final char LEFT = 'L';

    private static double minPercentOverlap = 0.50;
    private static int rowsOverlap, colsOverlap;
    private static int cols, matrixCols, matrixCols_cols, cols_colsOverlap;

    public static Alignment alignNW(SequencePair sequences) {
        int rows = sequences.seq1length() + 1;
        cols = sequences.seq2length() + 1;

        rowsOverlap = (int) (rows * minPercentOverlap);
        if (rowsOverlap >= rows) {
            rowsOverlap = rows - 1;
        } else if (rows - rowsOverlap > cols) {
            rowsOverlap = rows - cols;
        }

        colsOverlap = (int) (cols * minPercentOverlap);
        int ban_inverse_len = cols - colsOverlap;
        if (colsOverlap >= cols) {
            colsOverlap = cols - 1;
        } else if (ban_inverse_len > rows) {
            colsOverlap = cols - rows;
        }

        int colsOverlapPlus1 = colsOverlap + 1;
        matrixCols = Math.min(cols, rowsOverlap + colsOverlapPlus1);
        matrixCols_cols = matrixCols - cols;
        cols_colsOverlap = cols - colsOverlap;

        double[][] matrix = new double[rows][matrixCols];
        char[][] backtrack = new char[rows][matrixCols];

        matrix[0][0] = 0;
        backtrack[0][0] = 'S';
        if (rowsOverlap > 0) {
            matrix[1][0] = matrix[0][0] + SequencePair.GAP_EXTEND_SCORE;
            backtrack[1][0] = UP;
        }

        char[] currBackTrackRow = backtrack[0], prevBacktrackRow;
        double[] currMatrixRow = matrix[0], prevMatrixRow;
        if (colsOverlap > 0) {
            currMatrixRow[1] = currMatrixRow[0] + SequencePair.GAP_EXTEND_SCORE;
            currBackTrackRow[1] = LEFT;
        }

        for (int row = 1; row <= rowsOverlap; row++) {
            matrix[row][0] = matrix[row - 1][0] + SequencePair.GAP_EXTEND_SCORE;
            backtrack[row][0] = UP;
        }

        for (int col = 1; col <= colsOverlap; col++) {
            currMatrixRow[col] = currMatrixRow[col - 1] + SequencePair.GAP_EXTEND_SCORE;
            currBackTrackRow[col] = LEFT;
        }

        int i = rows - 1;
        int j = cols - 1;
        double left, diag, up;
        int matrixCol, matrixColAbove, colStart, colEnd, row_1;
        boolean headerRow, headerCol;
        for (int row = 1; row < rows; row++) {
            colStart = Math.max(1, row - rowsOverlap);
            colEnd = Math.min(cols, row + colsOverlapPlus1);
            row_1 = row - 1;
            headerRow = row < ban_inverse_len;
            headerCol = row > rowsOverlap;
            currBackTrackRow = backtrack[row];
            prevBacktrackRow = backtrack[row_1];
            prevMatrixRow = matrix[row_1];
            currMatrixRow = matrix[row];
            for (int col = colStart; col < colEnd; col++) {
                matrixCol = getMatrixColumn(row, col);
                matrixColAbove = getMatrixColumn(row_1, col);

                diag = prevMatrixRow[matrixColAbove - 1] + sequences.matchScore(row_1,col-1);

                if (headerCol && col == colStart)
                    left = Integer.MIN_VALUE;
                else // insert gap into seq1
                    left = currMatrixRow[matrixCol - 1] + sequences.leftGapCost(currBackTrackRow[matrixCol - 1] == LEFT || row == i, row);

                if (headerRow && col == colEnd - 1)
                    up = Integer.MIN_VALUE;
                else {
                    up = prevMatrixRow[matrixColAbove]
                            + sequences.nonGapCharCount(row_1) * (prevBacktrackRow[matrixColAbove] == UP || col == j? SequencePair.GAP_EXTEND_SCORE : SequencePair.GAP_OPEN_SCORE);
                }

                if (diag >= up) {
                    if (diag >= left) {
                        currMatrixRow[matrixCol] = diag;
                        currBackTrackRow[matrixCol] = DIAGONAL;
                    } else {
                        currMatrixRow[matrixCol] = left;
                        currBackTrackRow[matrixCol] = LEFT;
                    }
                } else {
                    if (up >= left) {
                        currMatrixRow[matrixCol] = up;
                        currBackTrackRow[matrixCol] = UP;
                    } else {
                        currMatrixRow[matrixCol] = left;
                        currBackTrackRow[matrixCol] = LEFT;
                    }
                }
            }
        }
//		if(rows < 10) {
//			printMatrixX(matrix);
//			printMatrixPointer(backtrack);
//		}

        AlignmentBuilder alnBldr = sequences.newAlignmentBuilder();

        matrixCol = getMatrixColumn(i, j);
        while (i > 0 || j > 0) {
            alnBldr.appendScore(matrix[i][matrixCol]);
            switch (backtrack[i][matrixCol]) {
                case DIAGONAL:
                    i--;
                    alnBldr.appendCharSequence1(i);
                    j--;
                    alnBldr.appendCharSequence2(j);
                    matrixCol = getMatrixColumn(i, j);
                    break;
                case UP:
                    i--;
                    alnBldr.appendCharSequence1(i);
                    alnBldr.appendIndelSequence2();
                    matrixCol = getMatrixColumn(i, j);
                    break;
                case LEFT:
                    alnBldr.appendIndelSequence1();
                    j--;
                    alnBldr.appendCharSequence2(j);
                    matrixCol--;
                    break;
            }
        }

        alnBldr.reverse();
        return alnBldr.renderAlignment();
    }

    @SuppressWarnings("unused")
    private static void printMatrixPointer(char[][] backtrack) {
        System.out.println("___________");
        for (char[] row : backtrack) {
            System.out.print("|");
            for (char col : row) {
                System.out.print(col == '\0'? ' ':col);
            }
            System.out.println("|");
        }
        System.out.println("___________");
    }

    public static Alignment alignSW(SequencePair sequences) {
        int rows = sequences.seq1length() + 1;
        cols = sequences.seq2length() + 1;

        rowsOverlap = (int) (rows * minPercentOverlap);
        if (rowsOverlap >= rows) {
            rowsOverlap = rows - 1;
        } else if (rows - rowsOverlap > cols) {
            rowsOverlap = rows - cols;
        }

        colsOverlap = (int) (cols * minPercentOverlap);
        int ban_inverse_len = cols - colsOverlap;
        if (colsOverlap >= cols) {
            colsOverlap = cols - 1;
        } else if (ban_inverse_len > rows) {
            colsOverlap = cols - rows;
        }

        int colsOverlapPlus1 = colsOverlap + 1;
        matrixCols = Math.min(cols, rowsOverlap + colsOverlapPlus1);
        matrixCols_cols = matrixCols - cols;
        cols_colsOverlap = cols - colsOverlap;

        double[][] matrix = new double[rows][matrixCols];
        char[][] backtrack = new char[rows][matrixCols];

        matrix[0][0] = 0;
        backtrack[0][0] = 'S';
        if (rowsOverlap > 0) {
            matrix[1][0] = matrix[0][0] + SequencePair.GAP_EXTEND_SCORE;
            backtrack[1][0] = UP;
        }

        char[] currBackTrackRow = backtrack[0], prevBacktrackRow;
        double[] currMatrixRow = matrix[0], prevMatrixRow;
        if (colsOverlap > 0) {
            currMatrixRow[1] = currMatrixRow[0] + SequencePair.GAP_EXTEND_SCORE;
            currBackTrackRow[1] = LEFT;
        }

        for (int row = 1; row <= rowsOverlap; row++) {
            matrix[row][0] = matrix[row - 1][0] + SequencePair.GAP_EXTEND_SCORE;
            backtrack[row][0] = UP;
        }

        for (int col = 1; col <= colsOverlap; col++) {
            currMatrixRow[col] = currMatrixRow[col - 1] + SequencePair.GAP_EXTEND_SCORE;
            currBackTrackRow[col] = LEFT;
        }

        int i = rows - 1;
        int j = cols - 1;
        double left, diag, up;
        int matrixCol, matrixColAbove, colStart, colEnd, row_1;
        boolean headerRow, headerCol;
        int maxI = 0, maxJ = 0;
        double maxScore = 0;
        for (int row = 1; row < rows; row++) {
            colStart = Math.max(1, row - rowsOverlap);
            colEnd = Math.min(cols, row + colsOverlapPlus1);
            row_1 = row - 1;
            headerRow = row < ban_inverse_len;
            headerCol = row > rowsOverlap;
            currBackTrackRow = backtrack[row];
            prevBacktrackRow = backtrack[row_1];
            prevMatrixRow = matrix[row_1];
            currMatrixRow = matrix[row];
            for (int col = colStart; col < colEnd; col++) {
                matrixCol = getMatrixColumn(row, col);
                matrixColAbove = getMatrixColumn(row_1, col);

                diag = prevMatrixRow[matrixColAbove - 1] + sequences.matchScore(row_1,col-1);

                if (headerCol && col == colStart)
                    left = Integer.MIN_VALUE;
                else // insert gap into seq1
                    left = currMatrixRow[matrixCol - 1] + sequences.leftGapCost(currBackTrackRow[matrixCol - 1] == LEFT || row == i, row);

                if (headerRow && col == colEnd - 1)
                    up = Integer.MIN_VALUE;
                else {
                    up = prevMatrixRow[matrixColAbove]
                            + sequences.nonGapCharCount(row_1) * (prevBacktrackRow[matrixColAbove] == UP || col == j? SequencePair.GAP_EXTEND_SCORE : SequencePair.GAP_OPEN_SCORE);
                }

                if (diag >= up) {
                    if (diag >= left) {
                        currMatrixRow[matrixCol] = diag;
                        currBackTrackRow[matrixCol] = DIAGONAL;
                    } else {
                        currMatrixRow[matrixCol] = left;
                        currBackTrackRow[matrixCol] = LEFT;
                    }
                } else {
                    if (up >= left) {
                        currMatrixRow[matrixCol] = up;
                        currBackTrackRow[matrixCol] = UP;
                    } else {
                        currMatrixRow[matrixCol] = left;
                        currBackTrackRow[matrixCol] = LEFT;
                    }
                }

                if (currMatrixRow[matrixCol] > maxScore) {
                    maxScore = currMatrixRow[matrixCol];
                    maxI = row;
                    maxJ = col;
                } else if (currMatrixRow[matrixCol] < 0) {
                    currMatrixRow[matrixCol] = 0;
                }
            }
        }
//		if(rows < 10) {
//			printMatrixX(matrix);
//			printMatrixPointer(backtrack);
//		}

        AlignmentBuilder alnBldr = sequences.newAlignmentBuilder();
        i = maxI;
        j = maxJ;
        matrixCol = getMatrixColumn(i, j);
        while ((i > 0 || j > 0) && matrix[i][matrixCol] >= 0) {
            alnBldr.appendScore(matrix[i][matrixCol]);
            switch (backtrack[i][matrixCol]) {
                case DIAGONAL:
                    i--;
                    alnBldr.appendCharSequence1(i);
                    j--;
                    alnBldr.appendCharSequence2(j);
                    matrixCol = getMatrixColumn(i, j);
                    break;
                case UP:
                    i--;
                    alnBldr.appendCharSequence1(i);
                    alnBldr.appendIndelSequence2();
                    matrixCol = getMatrixColumn(i, j);
                    break;
                case LEFT:
                    alnBldr.appendIndelSequence1();
                    j--;
                    alnBldr.appendCharSequence2(j);
                    matrixCol--;
                    break;
            }
        }

        alnBldr.reverse();
        return alnBldr.renderAlignment();
    }

    /**
     * @param //rowsOverlap
     * @param row
     * @param col
     * @return
     */
    public static int getMatrixColumn(int row, int col) {
        if (row <= rowsOverlap) {
            return col;
        } else if (row >= cols_colsOverlap) {
            return matrixCols_cols + col;
        } else {
            return col - row + rowsOverlap;
        }
    }

    public static void print2DMatrixDouble(double[][] matrix) {
        System.out.println("___________");
        for (double[] row : matrix) {
            System.out.print("|");
            boolean first = true;
            for (double col : row) {
                if (first)
                    first = false;
                else
                    System.out.print("\t");
                System.out.print(col);
            }
            System.out.println("|");
        }
        System.out.println("___________");
    }

	/*public static void main(String[] args) throws FileNotFoundException, IOException {

		Map<String, Map<String, List<LyricSheet>>> lyricSheets = RawDataLoader.loadLyricSheets(null);
		StopWatch refactoredWatch = new StopWatch();
		StopWatch oldOptimizedWatch = new StopWatch();
		StopWatch oldMSAWatch = new StopWatch();
		System.out.println(
				"Consistency\tXOpt time\tRefactored time\ttotalAlns");
		// for (int i = 0; i < tests.length; i += 2) {
		double consistent = 0.;
		double total = 0.;
		Aligner.setMinPercOverlap(.7);
		XOptimizedBandedPairwiseAlignment.setMinPercOverlap(.7);
		XSeqToAlnAlignment.setMinPercOverlap(.7);
		refactoredWatch.reset();
		oldOptimizedWatch.reset();
		oldMSAWatch.reset();
		int iterations = 10;
		for (int iters = 0; iters < iterations; iters++) {
			for (String key : lyricSheets.keySet()) {
				for (String key2 : lyricSheets.get(key).keySet()) {
					List<LyricSheet> tests = lyricSheets.get(key).get(key2);
					for (int i = 0; i < tests.size() - 1; i++) {
						String lyrics1 = tests.get(i).getLyrics().toLowerCase();
						String lyrics2 = tests.get(i + 1).getLyrics().toLowerCase();
						refactoredWatch.start();
						Alignment aln = Aligner.alignNW(new StringPair(lyrics1,lyrics2));
						refactoredWatch.stop();
						oldOptimizedWatch.start();
						XOptimizedBandedPairwiseAlignment aln2 = new XOptimizedBandedPairwiseAlignment(lyrics1,lyrics2);
						oldOptimizedWatch.stop();
						oldMSAWatch.start();
//						XOptimizedBandedPairwiseAlignment aln3 = new XOptimizedBandedPairwiseAlignment(lyrics1,lyrics2);
						oldMSAWatch.stop();
						boolean print = false;
						if (compareAlignments(aln, aln2))
							consistent++;
						else {
							System.out.println("WARNING: Difference between alns - ");
							print = true;
						}
//						if (compareAlignments(aln, aln3))
//							correct++;
//						else {
//							Utils.promptEnterKey(
//									"WARNING: Difference between alignments - ");
//							print = true;
//						}
						if (print) {
							System.out.println("\t" + ((String) aln.getFirst()).replaceAll("\n", "\\\\n"));
							System.out.println("\t" + ((String) aln.getSecond()).replaceAll("\n", "\\\\n"));
							System.out.println("\t" + Arrays.toString(aln.getScores()));
							System.out.println("\t" + aln2.getFirst().replaceAll("\n", "\\\\n"));
							System.out.println("\t" + aln2.getSecond().replaceAll("\n", "\\\\n"));
							System.out.println("\t" + Arrays.toString(aln2.getScores()));
							Utils.promptEnterKey("");
						}
						total++;
					}
				}
			}
		}
		System.out.print("" + (consistent / total));
		System.out.print("\t" + oldOptimizedWatch.elapsedTime() / iterations);
		System.out.print("\t" + refactoredWatch.elapsedTime() / iterations);
//		System.out.print("\t" + oldMSAWatch.elapsedTime() / iterations);
		System.out.print("\t" + total / iterations);
		System.out.println();
	}*/

    public static void setMinPercOverlap(double minPercOverlap) {
        minPercentOverlap = minPercOverlap;
    }

    /**
     * @param //aln
     * @param //aln2
     * @return
     */
	/*public static boolean compareAlignments(Alignment aln, XGenericPairwiseAlignment aln2) {
		String alnFirst = (String) aln.getFirst();
		String alnSecond = (String) aln.getSecond();
		double[] alnScores = aln.getScores();
		String aln2First = aln2.getFirst();
		String aln2Second = aln2.getSecond();
		int[] aln2Scores = aln2.getScores();
		boolean same = true;
		same &= (alnFirst.length() == aln2First.length() && alnSecond.length() == aln2Second.length());
		for (int i = 0; same && i < alnFirst.length(); i++) {
			same &= (alnFirst.charAt(i) == aln2First.charAt(i));
			same &= (alnSecond.charAt(i) == aln2Second.charAt(i));
			same &= (alnScores[i] == aln2Scores[i]);
		}
//		if (same) {
//			System.out.println("Same");
//		} else {
//			System.out.println(alnFirst);
//			System.out.println(alnSecond);
//			System.out.println(Arrays.toString(alnScores));
//			System.out.println(aln2First);
//			System.out.println(aln2Second);
//			System.out.println(Arrays.toString(aln2Scores));
//		}
		return same;
	}*/

    public static void printMatrixS(int[][] matrix) {
        System.out.println("___________");
        for (int[] row : matrix) {
            System.out.print("|");
            for (int col : row) {
                System.out.print(col + "\t");
            }
            System.out.println("|");
        }
        System.out.println("___________");
    }
}
