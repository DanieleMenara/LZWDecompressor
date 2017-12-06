package Decompressor;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Class used to process input stream and read 12-bit words
 * at a time.
 */
public class FixedCodeInputStream implements Closeable, AutoCloseable {

    private final InputStream inputStream;
    private final ArrayList<Byte> buffer = new ArrayList<>();
    private static final byte UPPER_MASK = 15;

    public FixedCodeInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * Checks if there is any input left to read.
     * @return  True if there is more input to process.
     * @throws IOException if an IO error occurs.
     */
    public boolean hasNextCode() throws IOException {
        return !buffer.isEmpty() || (inputStream.available() != 0);
    }

    /**
     * Close underlying input source.
     * @throws IOException
     */
    public void close() throws IOException {
        inputStream.close();
    }

    /**
     * Function that reads input from the underlying input source and
     * returns the first 12-bit words available.
     * @return The next available 12-bit word
     * @throws IOException if an IO error occurs.
     */
    public int nextCode() throws IOException {
        if (buffer.isEmpty()) {
            byte[] temp = new byte[3];
            int n = inputStream.read(temp, 0, 3);
            if (n == 3) {
                // read 3 bytes and return the first 12 bits word and cache the remaining 12 bits
                buffer.add((byte) (temp[1] & UPPER_MASK));
                buffer.add(temp[2]);
                return ((temp[0] & 255) << 4) | ((temp[1] & 255) >>> 4);
            } else if (n == 2) {
                // only 2 bytes left, this is the last word padded to 16 bits
                return ((temp[0] & 255) << 8) | (temp[1] & 255);
            }
        } else if (buffer.size() == 2) {
            // returned cached word from previous reading
            return ((buffer.remove(0) & 255) << 8) | (buffer.remove(0) & 255);
        }
        throw new IOException("Invalid number of bytes in the input.");
    }
}
