package com.asamioffice.goldenport.io;

import java.io.*;
import java.net.URL;

import com.asamioffice.goldenport.text.UString;

/**
 * Utilites for I/O operation
 *
 * @since   Feb.  8, 1998
 * @version Jan. 18, 2009
 * @author  ASAMI, Tomoharu (asami@AsamiOffice.com)
 */
public class UIO {
    public static String uri2String(String uri) throws IOException {
        try {
            URL url = new URL(uri);
            return (URL2String(url));
        } catch (IOException e) {
        }
        return (file2String(uri));
    }

    public static String uri2String(String uri, String encoding)
        throws IOException {

        try {
            URL url = new URL(uri);
            return (URL2String(url, encoding));
        } catch (IOException e) {
        }
        return (file2String(uri, encoding));
    }

    public static String file2String(String file) throws IOException {
        InputStream in = null;
        String result = null;
        try {
            in = new FileInputStream(file);
            result = stream2String(in);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
            }
        }
        return (result);
    }

    public static String file2String(File file) {
        InputStream in = null;
        String result = null;
        try {
            in = new FileInputStream(file);
            result = stream2String(in);
        } catch (IOException e) {
            throw (new IllegalArgumentException(e.getMessage()));
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
            }
        }
        return (result);
    }

    public static String URL2String(URL url) throws IOException {
        InputStream in = null;
        String result = null;
        try {
            in = url.openStream();
            result = stream2String(in);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
            }
        }
        return (result);
    }

    public static String resource2String(String resource, Object base)
        throws IOException {

        return (resource2String(resource, base.getClass()));
    }

    public static String resource2String(String resource, Class clazz)
        throws IOException {

        return (resource2String(resource, clazz.getClassLoader()));
    }

    public static String resource2String(String resource, ClassLoader loader)
        throws IOException {

        InputStream in = null;
        String result = null;
        try {
            URL url = UURL.getURLFromResourceName(resource, loader);
	    if (url == null) {
		throw (new IOException(resource));
	    }
            in = url.openStream();
            result = stream2String(in);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
            }
        }
        return (result);
    }

    public static String stream2String(InputStream in) throws IOException {
        Reader reader = null;
        StringWriter writer = null;
        String result = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            writer = new StringWriter();
            int c;
            while ((c = reader.read()) != -1) {
                writer.write(c);
            }
            writer.flush();
            result = new String(writer.getBuffer());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
        return (result);
    }

    public static String file2String(String file, String encoding)
        throws IOException {
        InputStream in = null;
        String result = null;
        try {
            in = new FileInputStream(file);
            result = stream2String(in, encoding);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
            }
        }
        return (result);
    }

    public static String file2String(File file, String encoding)
        throws IOException {
        InputStream in = null;
        String result = null;
        try {
            in = new FileInputStream(file);
            result = stream2String(in, encoding);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
            }
        }
        return (result);
    }

    public static String URL2String(URL url, String encoding)
        throws IOException {
        InputStream in = null;
        String result = null;
        try {
            in = url.openStream();
            result = stream2String(in, encoding);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
            }
        }
        return (result);
    }

    public static String resource2String(
        String resource,
        Object base,
        String encoding)
        throws IOException {
        return (resource2String(resource, base.getClass(), encoding));
    }

    public static String resource2String(
        String resource,
        Class clazz,
        String encoding)
        throws IOException {
        return (resource2String(resource, clazz.getClassLoader(), encoding));
    }

    public static String resource2String(
        String resource,
        ClassLoader loader,
        String encoding)
        throws IOException {
        InputStream in = null;
        String result = null;
        try {
            URL url = UURL.getURLFromResourceName(resource, loader);
            in = url.openStream();
            result = stream2String(in, encoding);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
            }
        }
        return (result);
    }

    public static String stream2String(InputStream in, String encoding)
        throws IOException {
        Reader reader = null;
        StringWriter writer = null;
        String result = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in, encoding));
            writer = new StringWriter();
            int c;
            while ((c = reader.read()) != -1) {
                writer.write(c);
            }
            writer.flush();
            result = new String(writer.getBuffer());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
        return (result);
    }

    public static byte[] file2Bytes(String file) throws IOException {
        InputStream in = null;
        byte[] result = null;
        try {
            in = new FileInputStream(file);
            result = stream2Bytes(in);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
            }
        }
        return (result);
    }

    public static byte[] file2Bytes(File file) throws IOException {
        InputStream in = null;
        byte[] result = null;
        try {
            in = new FileInputStream(file);
            result = stream2Bytes(in);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
            }
        }
        return (result);
    }

    public static byte[] URL2Bytes(URL url) throws IOException {
        InputStream in = null;
        byte[] result = null;
        try {
            in = url.openStream();
            result = stream2Bytes(in);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
            }
        }
        return (result);
    }

    public static byte[] resource2Bytes(String resource, Object base)
        throws IOException {

        return (resource2Bytes(resource, base.getClass()));
    }

    public static byte[] resource2Bytes(String resource, Class clazz)
        throws IOException {

        return (resource2Bytes(resource, clazz.getClassLoader()));
    }

    public static byte[] resource2Bytes(String resource, ClassLoader loader)
        throws IOException {

        InputStream in = null;
        byte[] result = null;
        try {
            URL url = UURL.getURLFromResourceName(resource, loader);
	    in = url.openStream();
            result = stream2Bytes(in);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
            }
        }
        return (result);
    }

    public static void stream2stream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[8192];
        int size;
        while ((size = in.read(buffer)) != -1) {
            out.write(buffer, 0, size);
        }
        out.flush();
    }

    public static byte[] stream2Bytes(InputStream in) throws IOException {
        ByteArrayOutputStream out = null;
        try {
            byte[] buffer = new byte[8192];
            out = new ByteArrayOutputStream();
            int size;
            while ((size = in.read(buffer)) != -1) {
                out.write(buffer, 0, size);
            }
            out.flush();
            return out.toByteArray();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
            }
        }
    }

    public static String reader2String(Reader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        char[] buffer = new char[8192];
        int size;
        while ((size = reader.read(buffer)) != -1) {
            sb.append(buffer, 0, size);
        }
        return sb.toString();
    }

    public static char[] reader2Chars(Reader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        char[] buffer = new char[8192];
        int size;
        while ((size = reader.read(buffer)) != -1) {
            sb.append(buffer, 0, size);
        }
        return sb.toString().toCharArray();
    }

    public static int[] reader2CodePoints(Reader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        char[] buffer = new char[8192];
        int size;
        while ((size = reader.read(buffer)) != -1) {
            sb.append(buffer, 0, size);
        }
        return UString.string2CodePoints(sb.toString());
    }


    public static BufferedWriter getBufferedWriter(Writer out) {
        if (out instanceof BufferedWriter) {
            return (BufferedWriter)out;
        } else {
            return new BufferedWriter(out);
        }
    }
}
