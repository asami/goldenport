package com.asamioffice.goldenport.text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * CsvTableMaker
 *
 * @since   Jan. 20, 2006
 * @version Jan. 20, 2006
 * @author  ASAMI, Tomoharu (asami@AsamiOffice.com)
 */
public class ReaderCsvTableMaker {
    private final BufferedReader reader_;

    public ReaderCsvTableMaker(BufferedReader reader) {
        reader_ = reader;
    }

    public ReaderCsvTableMaker(Reader reader) {
        reader_ = new BufferedReader(reader);
    }
    
    public String[] readLine() throws IOException {
        String line = reader_.readLine();
        if (line == null) {
            return null;
        }
        CsvLineMaker maker = new CsvLineMaker(line);
        return maker.make();
    }
}
