package com.asamioffice.goldenport.text;

import java.util.ArrayList;
import java.util.List;

/**
 * CsvMaker
 *
 * @since   Jan. 20, 2006
 * @version Jan. 20, 2006
 * @author  ASAMI, Tomoharu (asami@AsamiOffice.com)
 */
public class CsvLineMaker {
    private final String text_;
    private StringBuilder buffer_ = new StringBuilder();
    private List<String> line_ = new ArrayList<String>();

    private abstract class State {
        public abstract State execute(char c);
    }
    private final State INIT = new State() {
        public State execute(char c) {
            switch (c) {
            case ',' :
                line_.add("");
                return INIT;
            case '"' :
                return DOUBLEQUOTE;
            default :
                buffer_.append(c);
                return TEXT;
            }
        }
    };
    private final State TEXT = new State() {
        public State execute(char c) {
            switch (c) {
            case ',' :
                line_.add(new String(buffer_));
                buffer_ = new StringBuilder();
                return INIT;
            case '"' :
                return DOUBLEQUOTE;
            default :
                buffer_.append(c);
                return TEXT;
            }
        }
    };
    private final State DOUBLEQUOTE = new State() {
        public State execute(char c) {
            switch (c) {
            case ',' :
                buffer_.append(c);
                return DOUBLEQUOTETEXT;
            case '"' :
                buffer_.append("\"");
                return TEXT;
            default :
                buffer_.append(c);
                return DOUBLEQUOTETEXT;
            }
        }
    };
    private final State DOUBLEQUOTETEXT = new State() {
        public State execute(char c) {
            switch (c) {
            case ',' :
                buffer_.append(c);
                return DOUBLEQUOTETEXT;
            case '"' :
                return TEXT;
            default :
                buffer_.append(c);
                return DOUBLEQUOTETEXT;
            }
        }
    };

    public CsvLineMaker(String text) {
        text_ = text;
    }

    public String[] make() {
        State state = INIT;
        int size = text_.length();
        for (int i = 0; i < size; i++) {
            char c = text_.charAt(i);
            state = state.execute(c);
        }
        if (state != INIT) {
            line_.add(new String(buffer_));
        }
        String[] array = new String[line_.size()];
        return line_.toArray(array);
    }
}
