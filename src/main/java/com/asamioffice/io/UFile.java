package com.asamioffice.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Utilites for File manipulation
 *
 * @since   Jul.  7, 1998
 * @version Nov. 28, 2008
 * @author  ASAMI, Tomoharu (asami@AsamiOffice.com)
 */
public class UFile {
    public static void createFile(File file, InputStream is) throws IOException {
        OutputStream os = null;
        try {
            createParentDirectory(file);
            int chunk = 8192;
            byte[] buffer = new byte[chunk];
            os = new FileOutputStream(file);
            int rsize;
            while ((rsize = is.read(buffer)) != -1) {
                os.write(buffer, 0, rsize);
            }
            os.flush();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static void createFile(File file, String string)
        throws IOException {

        createParentDirectory(file);
        Writer writer = new FileWriter(file);
        writer.write(string, 0, string.length());
        writer.flush();
        writer.close();
    }

    public static void createFile(File file, String string, String encoding)
        throws IOException {

        createParentDirectory(file);
        Writer writer =
            new OutputStreamWriter(new FileOutputStream(file), encoding);
        writer.write(string, 0, string.length());
        writer.flush();
        writer.close();
    }

    public static void createFile(File file, byte[] data) throws IOException {

        createParentDirectory(file);
        OutputStream out = new FileOutputStream(file);
        out.write(data);
        out.close();
    }

    public static void copyFile(File src, File dest) throws IOException {
        FileInputStream in = null;
        try {
            if (isIdentical(src, dest)) {
                throw (new IOException("same file = " + src.toString()));
            }
            in = new FileInputStream(src);
            createFile(dest, in);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }

/*
    public static void copyAllFiles(File src, File dest) throws IOException {
        if (!(src.isDirectory() && dest.isDirectory())) {
            throw (new IllegalArgumentException("not directory"));
        }
        String[] files = src.list(new FileFilenameFilter());
        if (files == null) {
            return;
        }
        for (int i = 0; i < files.length; i++) {
            copyFile(new File(src, files[i]), new File(dest, files[i]));
        }
    }
*/

    public static void createDirectory(File dir) throws IOException {
        dir.mkdirs();
    }

    public static void createParentDirectory(File file) throws IOException {
        File dir = file.getParentFile();
        if (dir != null) {
            createDirectory(dir);
        }
    }

    public static File createTempDir(String prefix) throws IOException {
        File dir = File.createTempFile(prefix, ".d");
        dir.delete();
        dir.mkdir();
        dir.deleteOnExit();
        return (dir);
    }

    public static File createTempDir(String prefix, File parent) throws IOException {
        File dir = File.createTempFile(prefix, ".d", parent);
        dir.delete();
        dir.mkdir();
        dir.deleteOnExit();
        return (dir);
    }

    public static void deleteWholeFiles(String file) throws IOException {
        deleteWholeFiles(new File(file));
    }

    public static void deleteWholeFiles(File file) throws IOException {
        File[] children = file.listFiles();
        if (children != null) {
            for (int i = 0; i < children.length; i++) {
                deleteWholeFiles(children[i]);
            }
        }
        file.deleteOnExit();
        file.delete();
    }

    public static void deleteChildFiles(String file) throws IOException {
        deleteChildFiles(new File(file));
    }

    public static void deleteChildFiles(File file) throws IOException {
        File[] children = file.listFiles();
        if (children != null) {
            for (int i = 0; i < children.length; i++) {
                deleteWholeFiles(children[i]);
            }
        }
    }

    public static void deleteWholeFilesDebug(String file) throws IOException {
        deleteWholeFilesDebug(new File(file));
    }

    public static void deleteWholeFilesDebug(File file) throws IOException {
        File[] children = file.listFiles();
        if (children != null) {
            for (int i = 0; i < children.length; i++) {
                deleteWholeFilesDebug(children[i]);
            }
        }
        System.err.println("Delete(Debug): " + file);
    }

    public static void deleteChildFilesDebug(String file) throws IOException {
        deleteChildFilesDebug(new File(file));
    }

    public static void deleteChildFilesDebug(File file) throws IOException {
        File[] children = file.listFiles();
        if (children != null) {
            for (int i = 0; i < children.length; i++) {
                deleteWholeFilesDebug(children[i]);
            }
        }
    }

    public static boolean isAbsolutePath(String child) {
        File file = new File(child);
        return (file.isAbsolute());
    }

    public static boolean isExist(String src) {
        try {
            URL url = UURL.getURLFromFileOrURLName(src);
            return (isExist(url));
        } catch (MalformedURLException e) {
            return (false);
        }
    }

    public static boolean isExist(URL url) {
        try {
            InputStream in = url.openStream();
            in.close();
            return (true);
        } catch (IOException e) {
            return (false);
        }
    }

    public static boolean isIdentical(File lhs, File rhs) throws IOException {
        return (lhs.getCanonicalPath().equals(rhs.getCanonicalPath()));
    }

    public static File newNormalizedFile(String pathName) {
        if (pathName.charAt(0) == '/') {
            return new File(pathName).getAbsoluteFile();
        } else {
            return new File(pathName);
        }
    }
}
