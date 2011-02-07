package com.seovic.io;


import com.tangosol.util.Binary;
import com.tangosol.util.BinaryWriteBuffer;
import com.tangosol.io.WrapperInputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.Set;
import java.util.HashSet;


/**
 * @author Aleksandar Seovic  2010.11.16
 */
public class FileUtils {
    public static void writeBinary(Binary binary, String fileName) {
        writeBinary(binary, new File(fileName));
    }

    public static void writeBinary(Binary binary, File file) {
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
        return readBinary(new File(fileName));    
    }

    public static Binary readBinary(File file) {
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

    public static Set<String> listAllFiles(String directory) {
        return listAllFiles(new File(directory));
    }

    public static Set<String> listAllFiles(File directory) {
        Set<String> results = new HashSet<String>(256);
        if (!directory.exists()) {
            return results;
        }
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Specified file is not a directory");
        }

        for (File file : directory.listFiles()) {
            if (!file.isDirectory()) {
                results.add(file.getName());
            }
        }

        return results;
    }
}
