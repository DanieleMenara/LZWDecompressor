import Decompressor.LZWDecompressor;

import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("Please enter the path of the file to decompress: ");
            String filePath = br.readLine();
            File inputFile = new File(filePath);
            if( !inputFile.exists() || !inputFile.isFile() || !inputFile.canRead()) {
                System.out.print("Invalid file name.");
                return;
            }
            System.out.print("Please enter name for the output file: ");
            String outputFileName = br.readLine();
            File decompressed = new File(inputFile.toPath().getParent().toString(), outputFileName);
            try (InputStream in = new BufferedInputStream(new FileInputStream(inputFile));
                 OutputStream out = new BufferedOutputStream(new FileOutputStream(decompressed))) {
                LZWDecompressor decompressor = new LZWDecompressor();
                decompressor.decompress(in, out);
            }
        }
    }
}
