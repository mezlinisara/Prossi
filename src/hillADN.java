//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
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

    private static String textToBinary(String text) {
        StringBuilder binaryText = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {  // Exclude numbers and non-letter characters
                String binaryChar = Integer.toBinaryString(c);
                while (binaryChar.length() < 8) {
                    binaryChar = "0" + binaryChar;
                }
                binaryText.append(binaryChar);
            }
        }
        return binaryText.toString();
    }

    private static String binaryToDNA(String binaryText) {
        StringBuilder dnaText = new StringBuilder();
        for (int i = 0; i < binaryText.length(); i += 2) {
            String twoBits = binaryText.substring(i, i + 2);
            for (char[] mapping : DNA_MAPPING) {
                if (Arrays.equals(Arrays.copyOfRange(mapping, 1, 3), twoBits.toCharArray())) {
                    dnaText.append(mapping[0]);
                    break;
                }
            }
        }
        return dnaText.toString();
    }

    private static String dnaToAminoAcids(String dnaText) {
        StringBuilder aminoAcidText = new StringBuilder();
        for (int i = 0; i < dnaText.length(); i++) {
            char dnaChar = dnaText.charAt(i);
            for (char[] row : AMINO_ACID_TABLE) {
                if (row[0] == dnaChar) {
                    aminoAcidText.append(row[1]);
                    break;
                }
            }
        }
        return aminoAcidText.toString();
    }

    private static String hillCipherEncrypt(String aminoAcidText, int[][] keyMatrix) {
        // Assuming keyMatrix is a square matrix
        int blockSize = keyMatrix.length;

        // Padding if needed
        int padding = aminoAcidText.length() % blockSize;
        if (padding != 0) {
            StringBuilder aminoAcidTextBuilder = new StringBuilder(aminoAcidText);
            for (int i = 0; i < blockSize - padding; i++) {
                aminoAcidTextBuilder.append('X'); // Padding with 'X' character
            }
            aminoAcidText = aminoAcidTextBuilder.toString();
        }

        StringBuilder encryptedText = new StringBuilder();

        for (int i = 0; i < aminoAcidText.length(); i += blockSize) {
            String block = aminoAcidText.substring(i, i + blockSize);
            int[] blockVector = new int[blockSize];

            // Convert characters to numbers
            for (int j = 0; j < blockSize; j++) {
                blockVector[j] = block.charAt(j) - 'A'; // Assuming 'A' is 0, 'B' is 1, and so on
            }

            // Multiply keyMatrix with blockVector
            int[] resultVector = new int[blockSize];
            for (int j = 0; j < blockSize; j++) {
                for (int k = 0; k < blockSize; k++) {
                    resultVector[j] += keyMatrix[j][k] * blockVector[k];
                }
                resultVector[j] %= 26; // Modulo 26 to wrap around the alphabet
            }

            // Convert numbers back to characters
            for (int j = 0; j < blockSize; j++) {
                encryptedText.append((char) (resultVector[j] + 'A'));
            }
        }

        return encryptedText.toString();
    }

    public static void main(String[] args) {
        // Example usage
        String plaintext = "bonne annee 2024";

        String processedText = plaintext.replaceAll("[^a-zAA-Z]", "");
        int[][] keyMatrix = {
                {6, 24, 1},
                {13, 16, 10},
                {20, 17, 15}
        };

        // Encrypt
        String binaryText = textToBinary(plaintext);
        String dnaText = binaryToDNA(binaryText);
        String aminoAcidText = dnaToAminoAcids(dnaText);
        String encryptedText = hillCipherEncrypt(aminoAcidText, keyMatrix);

        // Output
        System.out.println("Original Text: " + plaintext);
        System.out.println("Processed Text: " + processedText);
        System.out.println("Binary Text: " + binaryText);
        System.out.println("DNA Text: " + dnaText);
        System.out.println("Amino Acid Text: " + aminoAcidText);
        System.out.println("Encrypted Text: " + encryptedText);
    }
}
