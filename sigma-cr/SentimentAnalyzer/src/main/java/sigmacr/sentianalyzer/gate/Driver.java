package sigmacr.sentianalyzer.gate;


import sigmacr.sentianalyzer.exceptions.SentiAnalyzerException;
import sigmacr.sentianalyzer.preprocess.AffectsListsCreator;
import sigmacr.sentianalyzer.scoreProducer.SWN;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

public class Driver {
    protected Logger log = Logger.getLogger(Driver.class.getName());
    private String inputHome = null;
    private String outputHome = null;
    private String gazetteerHome = null;
    private String preprocessHome = null;
    private String grammarHome = null;
    private String sentiPath = null;
    private DocumentHandler documentHandler;

    public Driver(String inputHome, String outputHome, String gazetteerHome, String preprocessHome, String grammarHome, String sentiPath) {
        this.inputHome = inputHome;
        this.outputHome = outputHome;
        this.gazetteerHome = gazetteerHome;
        this.preprocessHome = preprocessHome;
        this.grammarHome = grammarHome;
        this.sentiPath = sentiPath;
        documentHandler = new DocumentHandler();

    }


    public void runGateEmbeded() throws SentiAnalyzerException {

        try {
            URL gazHomeURL = new URL(gazetteerHome);
            AffectsListsCreator affectsListsCreator = new AffectsListsCreator(preprocessHome, gazHomeURL.getFile());
            affectsListsCreator.createGazList();    //create gazetteer list from seed set

            SWN sentiWordNet = new SWN(sentiPath); //Store positive and negative rates of senti words

            AffectScoresFinder affectScoresFinder = new AffectScoresFinder(gazHomeURL.getFile(), sentiWordNet);
            //insert score values in gazetteer affects files
            affectScoresFinder.insertAffectsScores();

            //Create the gate application
            GateAppCreator gateAppCreator = new GateAppCreator(inputHome, outputHome, gazetteerHome, grammarHome, documentHandler);
            gateAppCreator.runGate(); //run the gate app
            //Annalyze annotations
            AnnotAnalyzer annotAnalyzer = new AnnotAnalyzer(outputHome, documentHandler);
            annotAnalyzer.analyzeSentiAnnot();
        } catch (MalformedURLException e) {
            log.warning("Cannot find the URL specified for gazetteerHome" + e.getMessage());
            throw new SentiAnalyzerException("Cannot find the URL specified for gazetteerHome ", e);
        }

    }
}
