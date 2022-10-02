package saarr_5.framework.cognative;

import saarr_5.morphalize.Word;

public class CognaterEntity {

    private final Word verb;
    private final Word gerund;
    private String root;
    private Type type;

    public CognaterEntity(Word v, Word g) {
        verb = v;
        gerund = g;
    }

    public CognaterEntity(Word v, Word g, Type typ) {
        verb = v;
        gerund = g;
        type = typ;
    }

    public void setRoot(String rt) {
        root = rt;
    }

    public Type getType() {
        return type;
    }

    public enum Type {

        EMPHASIS, DESCRIPTIVE, NUMERICAL;
    }

    public Word gerund() {
        return gerund;
    }

    public Word verb() {
        return verb;
    }

    @Override
    public String toString() {
        return verb.modifiedStem() + "\t" + gerund.modifiedStem();
    }
}
