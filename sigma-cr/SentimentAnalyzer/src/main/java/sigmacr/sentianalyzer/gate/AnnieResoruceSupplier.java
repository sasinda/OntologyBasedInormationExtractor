package sigmacr.sentianalyzer.gate;

import gate.Factory;
import gate.Factory;
import gate.FeatureMap;
import gate.FeatureMap;
import gate.Gate;
import gate.Gate;
import gate.ProcessingResource;
import gate.ProcessingResource;
import gate.creole.ANNIEConstants;
import gate.creole.ResourceInstantiationException;
import gate.creole.SerialAnalyserController;
import gate.util.GateException;
import gate.util.Out;
import sigmacr.sentianalyzer.exceptions.SentiAnalyzerException;

import java.io.File;
import java.net.MalformedURLException;
import java.util.logging.Logger;


public class AnnieResoruceSupplier {
    protected Logger log = Logger.getLogger(AffectScoresFinder.class.getName());
    private SerialAnalyserController annieController;
    File gateHome = Gate.getGateHome();
    File pluginsHome = new File(gateHome, "plugins");

    AnnieResoruceSupplier() throws SentiAnalyzerException {

        try {
            Gate.getCreoleRegister().registerDirectories(new File(pluginsHome, "ANNIE").toURI().toURL());
        } catch (MalformedURLException e) {
            log.warning("Cannot find the URL specified for ANNIE plugin" + e.getMessage());
            throw new SentiAnalyzerException("Cannot find the URL specified for ANNIE plugin", e);
        } catch (GateException e) {
            log.warning("ANNIE plugin loading failed" + e.getMessage());
            throw new SentiAnalyzerException("ANNIE plugin loading failed", e);
        }


        try {
            annieController =
                    (SerialAnalyserController) Factory.createResource("gate.creole.SerialAnalyserController", Factory.newFeatureMap(),
                            Factory.newFeatureMap(), "ANNIE_" + Gate.genSym()
                    );
        } catch (ResourceInstantiationException e) {
            log.warning("SerialAnalyserController instantiation failed" + e.getMessage());
            throw new SentiAnalyzerException("SerialAnalyserController instantiation failed", e);
        }
        Out.prln("...Annie initialised");
    }


    public ProcessingResource getProcessingResource(String resourceName,FeatureMap map) throws SentiAnalyzerException {
        ProcessingResource resource=null;
        try {
            resource =(ProcessingResource)Factory.createResource(resourceName,map);
        } catch (ResourceInstantiationException e) {
            log.warning("Processing resource "+resourceName+" instantiation failed" + e.getMessage());
            throw new SentiAnalyzerException("Processing resource "+resourceName+" instantiation failed", e);
        }
        return resource;
    }

    public void loadAllDefaultPrs() throws SentiAnalyzerException {
        for(int i = 0; i < ANNIEConstants.PR_NAMES.length; i++) {
            FeatureMap params = Factory.newFeatureMap(); // use default parameters
            ProcessingResource pr = null;
            try {
                pr = (ProcessingResource)
                        Factory.createResource(ANNIEConstants.PR_NAMES[i], params);
            } catch (ResourceInstantiationException e) {
                log.warning("Processing resource instantiation failed" + e.getMessage());
                throw new SentiAnalyzerException("Processing resource instantiation failed", e);
            }

            // add the PR to the pipeline controller
            annieController.add(pr);
        } // for each ANNIE PR
    }

    public ProcessingResource getDefaultPrs(String name) throws SentiAnalyzerException {


        ProcessingResource pr = null;
        try {
            pr = (ProcessingResource)
                    Factory.createResource(name);
        } catch (ResourceInstantiationException e) {
            log.warning("Processing resource " + name + " instantiation failed" + e.getMessage());
            throw new SentiAnalyzerException("Processing resource "+name+" instantiation failed", e);
        }

        return pr;
    }
}
