package Decompressor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * This class can be used to decompress files encoded using
 * the LZW compression algorithm with 12-bit codewords. The
 * initial dictionary is the 8-bit ISO_8859_1 characters.
 */
public class LZWDecompressor {

    private static final int ALPHABET = 256; // num of chars in starting dictionary
    private static final int NUM_CODEWORDS = 4096; // max number of codewords available (2^12)
    private static final Charset CHARSET = StandardCharsets.ISO_8859_1;

    private final HashMap<Integer, String> dictionary = new HashMap<>();

    private void initializeDictionary() {
        dictionary.clear();
        for(int i = 0; i<ALPHABET; i++) {
            String s = new String(new byte[]{(byte) i}, CHARSET);
            dictionary.put(i, s);
        }
    }

    /**
     * Read 12-bit codewords from the input stream provided and decompress
     * according to LZW compression algorithm, using assumptions as specified.
     * @param in    Input stream from which the codewords are read
     * @param out   Output stream where decoded output is written
     * @throws IOException  Propagate exception if InputStream or OutputStream throw an excpetion.
     */
    public void decompress(final InputStream in, final OutputStream out) throws IOException {
        FixedCodeInputStream encoded = new FixedCodeInputStream(in);
        initializeDictionary();
        if (!encoded.hasNextCode()) {
            return; // Empty input
        }
        Integer currentCode = encoded.nextCode();
        if (!dictionary.containsKey(currentCode)) {
            throw new IOException("First character is not in the dictionary.");
        }
        int newEntryCode = ALPHABET;
        String currentString = dictionary.get(currentCode), decoded;
        out.write(currentString.getBytes(CHARSET));
        while(encoded.hasNextCode()) {
            currentCode = encoded.nextCode();
            if(!dictionary.containsKey(currentCode)) {
                decoded = currentString + currentString.charAt(0);
            } else {
                decoded = dictionary.get(currentCode);
            }
            out.write(decoded.getBytes(CHARSET));
            dictionary.put(newEntryCode, currentString + decoded.charAt(0));
            newEntryCode++;
            if (newEntryCode >= NUM_CODEWORDS) {
                newEntryCode = ALPHABET;
                initializeDictionary();
            }
            currentString = decoded;
        }
    }
}
