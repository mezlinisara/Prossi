import java.util.Arrays;

public class hillADN {

    private static final char[][] DNA_MAPPING = {
            {'A', '0', '0'},
            {'C', '0', '1'},
            {'G', '1', '0'},
            {'T', '1', '1'}
    };

    private static final char[][] AMINO_ACID_TABLE = {
            {'G', 'C', 'U'}, {'G', 'C', 'C'}, {'G', 'C', 'A'}, {'G', 'C', 'G'},
            {'A', 'A', 'U'}, {'A', 'A', 'C'},
            {'G', 'A', 'U'}, {'G', 'A', 'C'},
            {'U', 'G', 'U'}, {'U', 'G', 'C'},
            {'C', 'A', 'A'}, {'C', 'A', 'G'},
            {'G', 'A', 'A'}, {'G', 'A', 'G'},
            {'G', 'U', 'U'}, {'G', 'U', 'C'}, {'G', 'U', 'A'}, {'G', 'U', 'G'},
            {'C', 'A', 'U'}, {'C', 'A', 'C'},
            {'A', 'U', 'U'}, {'A', 'U', 'C'}, {'A', 'U', 'A'},
            {'U', 'A', 'A'}, {'U', 'G', 'A'}, {'U', 'A', 'G'},
            {'A', 'G', 'A'}, {'A', 'G', 'G'},
            {'U', 'A', 'C'},
            {'C', 'U', 'U'}, {'C', 'U', 'C'}, {'C', 'U', 'A'}, {'C', 'U', 'G'},
            {'A', 'A', 'A'}, {'A', 'A', 'G'},
            {'A', 'U', 'G'},
            {'U', 'U', 'U'}, {'U', 'U', 'C'},
            {'C', 'C', 'U'}, {'C', 'C', 'C'}, {'C', 'C', 'A'}, {'C', 'C', 'G'},
            {'A', 'C', 'U'}, {'A', 'C', 'C'}, {'A', 'C', 'A'}, {'A', 'C', 'G'},
            {'U', 'G', 'G'},
            {'U', 'A', 'U'},
            {'G', 'U', 'U'}, {'G', 'U', 'C'}, {'G', 'U', 'A'}, {'G', 'U', 'G'},
            {'U', 'U', 'A'}, {'U', 'U', 'G'},
            {'A', 'G', 'U'}, {'A', 'G', 'C'},
            {'U', 'C', 'U'}, {'U', 'C', 'C'}, {'U', 'C', 'A'}, {'U', 'C', 'G'},
            {'C', 'G', 'U'}, {'C', 'G', 'C'}, {'C', 'G', 'A'}, {'C', 'G', 'G'},
    };

    private static String hillCipherDecrypt(String encryptedText, int[][] keyMatrix) {
        int blockSize = keyMatrix.length;
        StringBuilder decryptedText = new StringBuilder();

        for (int i = 0; i < encryptedText.length(); i += blockSize) {
            String block = encryptedText.substring(i, i + blockSize);
            int[] blockVector = new int[blockSize];

            for (int j = 0; j < blockSize; j++) {
                blockVector[j] = block.charAt(j) - 'A';
            }

            int[][] inverseKeyMatrix = getInverseMatrix(keyMatrix, 26);

            int[] resultVector = new int[blockSize];
            for (int j = 0; j < blockSize; j++) {
                for (int k = 0; k < blockSize; k++) {
                    resultVector[j] += keyMatrix[j][k] * blockVector[k];
                }
                resultVector[j] %= 26;
            }

            for (int j = 0; j < blockSize; j++) {
                decryptedText.append((char) (resultVector[j] + 'A'));
            }
        }

        return decryptedText.toString();
    }

    private static int[][] getInverseMatrix(int[][] keyMatrix, int mod) {
        int det = determinant(keyMatrix);
        int[][] adjugate = adjugateMatrix(keyMatrix);

        int detInverse = modInverse(det, mod);

        int[][] inverseMatrix = scalarMultiply(adjugate, detInverse, mod);

        for (int i = 0; i < inverseMatrix.length; i++) {
            for (int j = 0; j < inverseMatrix[0].length; j++) {
                inverseMatrix[i][j] = (inverseMatrix[i][j] + mod) % mod;
            }
        }

        return inverseMatrix;
    }

    private static int[][] scalarMultiply(int[][] matrix, int scalar, int mod) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[][] result = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = (matrix[i][j] * scalar) % mod;
            }
        }

        return result;
    }

    private static int determinant(int[][] matrix) {
        if (matrix.length != matrix[0].length) {
            throw new IllegalArgumentException("Matrix must be square");
        }

        int n = matrix.length;

        if (n == 1) {
            return matrix[0][0];
        }

        if (n == 2) {
            return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        }

        int det = 0;

        for (int i = 0; i < n; i++) {
            det += matrix[0][i] * cofactor(matrix, 0, i);
        }

        return det;
    }

    private static int cofactor(int[][] matrix, int row, int col) {
        return (int) Math.pow(-1, row + col) * determinant(minor(matrix, row, col));
    }

    private static int[][] minor(int[][] matrix, int row, int col) {
        int n = matrix.length;
        int[][] minorMatrix = new int[n - 1][n - 1];

        for (int i = 0, m = 0; i < n; i++) {
            if (i == row) continue;
            for (int j = 0, k = 0; j < n; j++) {
                if (j == col) continue;
                minorMatrix[m][k++] = matrix[i][j];
            }
            m++;
        }

        return minorMatrix;
    }

    private static int[][] adjugateMatrix(int[][] matrix) {
        int n = matrix.length;
        int[][] adjugate = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                adjugate[i][j] = cofactor(matrix, i, j);
            }
        }

        // Transpose the cofactor matrix to get the adjugate
        return transpose(adjugate);
    }

    private static int[][] transpose(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[][] result = new int[cols][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[j][i] = matrix[i][j];
            }
        }

        return result;
    }

    private static int modInverse(int a, int m) {
        a = (a % m + m) % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        throw new IllegalArgumentException("Modular inverse does not exist");
    }
    public static void main(String[] args) {
        String encryptedText = "AAAMAOXWH";  // Replace with the actual encrypted text
        int[][] keyMatrix = {
                {6, 24, 1},
                {13, 16, 10},
                {20, 17, 15}
        };

        String decryptedText = hillCipherDecrypt(encryptedText, keyMatrix);

        System.out.println("Encrypted Text: " + encryptedText);
        System.out.println("Decrypted Text: " + decryptedText);
    }
}