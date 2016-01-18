package sigmacr.sentianalyzer.gate;

import gate.Factory;
import gate.Factory;
import gate.creole.ResourceInstantiationException;
import gate.util.GateException;
import sigmacr.sentianalyzer.exceptions.SentiAnalyzerException;

import java.io.File;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: hasinthaindrajee
 * Date: 6/3/13
 * Time: 10:30 AM
 * To change this template use File | Settings | File Templates.
 */
public class DocumentHandler {

    private gate.Corpus corpus;
    protected Logger log = Logger.getLogger(AffectScoresFinder.class.getName());

    public void initiateCorpus() throws SentiAnalyzerException {
        try {
            corpus = Factory.newCorpus("Transient Gate Corpus");
        } catch (ResourceInstantiationException e) {
            log.warning("Failed to initiate corpus" + e.getMessage());
            throw new SentiAnalyzerException("Failed to initiate corpus", e);
        }
    }
    public void createCorpus(String[] files) throws GateException, SentiAnalyzerException {


        for(int file = 0; file < files.length; file++) {
            System.out.print("\t " + (file + 1) + ") " + files[file]);
            try {
                corpus.add(Factory.newDocument(new File(files[file]).toURL()));
                System.out.println(" -- success");
            } catch (MalformedURLException e) {
                log.warning("Couldn't fine the URL specified" + e.getMessage());
                throw new SentiAnalyzerException("Couldn't fine the URL specified", e);
            }


        }
    }
    public gate.Corpus getCorpus(){
        if(corpus!=null){
            return corpus;
        }
        else throw new NullPointerException("Corpus is not initialzed");
    }
}
