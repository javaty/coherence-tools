package com.seovic.io;


import com.tangosol.util.Binary;
import com.tangosol.util.BinaryWriteBuffer;
import com.tangosol.io.WrapperInputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileInputStream;


/**
 * @author Aleksandar Seovic  2010.11.16
 */
public class FileUtils {
    public static void writeBinary(Binary binary, String fileName) {
        File file = new File(fileName);
        try {
            if (!file.exists()) {
                File directory = file.getParentFile();
                if (directory != null && !directory.exists()) {
                    if (!directory.mkdirs()) {
                        throw new IOException("Unable to create directory [" + directory.getPath() + "]");
                    }
                }
            }

            if (file.exists() || file.createNewFile()) {
                binary.writeTo(new FileOutputStream(file));
            }
            else {
                throw new IOException("Unable to write file [" + file.getPath() + "]");
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Binary readBinary(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) return null;

        try {
            FileInputStream in = new FileInputStream(file);
            BinaryWriteBuffer buf = new BinaryWriteBuffer((int) file.length());
            buf.getBufferOutput().writeStream(new WrapperInputStream(in));
            return buf.toBinary();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
