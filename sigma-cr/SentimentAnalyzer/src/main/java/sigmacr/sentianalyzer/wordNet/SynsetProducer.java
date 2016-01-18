package sigmacr.sentianalyzer.wordNet;


import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.Word;
import net.didion.jwnl.dictionary.Dictionary;
import sigmacr.sentianalyzer.exceptions.SentiAnalyzerException;
import sigmacr.sentianalyzer.gate.AffectScoresFinder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Logger;


public class SynsetProducer {

    protected Logger log = Logger.getLogger(AffectScoresFinder.class.getName());


    public String[] getSynset(String str) throws SentiAnalyzerException {

        String fileName= "G:/libraries/jwnl14-rc2/config/file_properties.xml";
        try {

            JWNL.initialize(new FileInputStream("G:/libraries/jwnl14-rc2/config/file_properties.xml"));
            Dictionary dic = Dictionary.getInstance();
            ArrayList<String> synsetArraylist = new ArrayList<String>();
            IndexWord indexWord = dic.getIndexWord(POS.ADJECTIVE,str);
            if(indexWord!=null)  {
            Synset[] synsets= indexWord.getSenses();
            if (synsets.length > 0) {

                for (int i = 0; i < synsets.length; i++) {
                    Word[]  words = synsets[i].getWords();
                    //String[] wordForms = synsets[i].getWords().;
                    for (int j = 0; j < words.length; j++) {

                        synsetArraylist.add(words[j].getLemma());
                    }
                }

                String[] synsetArray = new String[synsetArraylist.size()];
                synsetArray = synsetArraylist.toArray(synsetArray);
                return synsetArray;
            } else {

                return null;
            }
            }else{
                return null;
            }
        } catch (JWNLException e) {
            log.warning("Couldn't fine the URL specified, " +fileName+ e.getMessage());
            throw new SentiAnalyzerException("Couldn't fine the URL specified", e);

        } catch (FileNotFoundException e) {
            log.warning("Couldn't fine the file specified, "+ fileName+ e.getMessage());
            throw new SentiAnalyzerException("Couldn't find the file specified", e);

        }

    }


}
