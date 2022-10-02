package saarr_5.framework.cognative;

import saarr_5.framework.phraser.Phrase;
import saarr_5.morphalize.Sentence;
import saarr_5.morphalize.Word;

public class CognateClause {

    Sentence sentence;
    Word verb;
    Word gerund;
    Word descriptive;
    Word subject;
    Phrase complment1;
    Phrase complment2;

    public CognateClause(Sentence sent, Word vrb, Word grnd) {
        sentence = sent;
        verb = vrb;
        gerund = grnd;
    }

    public void build() {
        subject = getSubject();
    }

    private Word getSubject() {
        
        Word subj = null;
        subj=new SubjectIdentify(sentence).subject();
        int verbIndex = verb.index();
        Phrase verbPhrase = sentence.getPhrase(verbIndex);
        String per = verb.per();
        return subj;
    }
}
