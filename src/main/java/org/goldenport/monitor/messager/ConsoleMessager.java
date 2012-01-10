package org.goldenport.monitor.messager;

import java.io.PrintWriter;

/**
 * ConsoleMessager
 *
 * @since   Aug. 27, 2005
 * @version Oct. 31, 2008
 * @version Oct. 30, 2011
 * @author  ASAMI, Tomoharu (asami@relaxer.org)
 */
public class ConsoleMessager extends GMessager {
    PrintWriter stdout_ = new PrintWriter(System.out);
    PrintWriter stderr_ = new PrintWriter(System.err);

    public void message(String message) {
        stdout_.write(message);
        stdout_.flush();
    }

    public void messageln(String message) {
        stdout_.println(message);
        stdout_.flush();
    }

    public void messageln() {
        stdout_.println();
        stdout_.flush();
    }

    public void message(char[] cbuf, int off, int len) {
        stdout_.write(cbuf, off, len);
        stdout_.flush();
    }

    public void error(String errorMessage) {
        stderr_.write(errorMessage);
        stderr_.flush();
    }

    public void errorln(String errorMessage) {
        stderr_.println(errorMessage);
        stderr_.flush();
    }

    public void errorln() {
        stderr_.println();
        stderr_.flush();
    }

    public void error(char[] cbuf, int off, int len) {
      stderr_.write(cbuf, off, len);
        stderr_.flush();
    }

    public void warning(String warningMessage) {
      stderr_.write(warningMessage);
      stderr_.flush();
    }

    public void warningln(String warningMessage) {
      stderr_.println(warningMessage);
      stderr_.flush();
    }

    public void warningln() {
      stderr_.println();
      stderr_.flush();
    }

    public void warning(char[] cbuf, int off, int len) {
      stderr_.write(cbuf, off, len);
      stderr_.flush();
    }
}
