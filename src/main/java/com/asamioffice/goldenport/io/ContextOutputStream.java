package com.asamioffice.goldenport.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * ContextOutputStream
 *
 * @since   Aug. 26, 2005
 * @version Jul. 21, 2006
 * @author  ASAMI, Tomoharu (asami@relaxer.org)
 */
public class ContextOutputStream extends OutputStream {
    private final OutputStream out_;

    public ContextOutputStream(OutputStream out) {
        out_ = out;
    }

    public void write(int b) throws IOException {
        out_.write(b);
    }

    public void write(byte[] b) throws IOException {
        out_.write(b);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        out_.write(b, off, len);
    }

    public void flush() throws IOException {
        out_.flush();
    }

    public void close() throws IOException {
        // block outer close.
    }
}
