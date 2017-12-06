# LZWDecompressor

Decompressor code for LZW using 12-bit fixed width codes. Assumptions:
- If the file being compressed results in an odd number of codes then the last code is padded to 16-bits.
- Dictionary is initialized to first 256 byte characters using the ISO_8859_1 alphabet.
- When all the possible codes have been used, the dictionary is reset to the initial dictionary.


## Usage

The code is written using Java 8 and it provides a simple main function to test it with encoded files. Sample IO:
```
Please enter the path of the file to decompress: /path/to/file/compressedfile1.z
Please enter name for the output file: decompressedfile1.txt
```
This should read the compressed file as specified by the user and decode the file saving it in the same location with
 the provided name.
