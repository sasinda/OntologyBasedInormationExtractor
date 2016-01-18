package com.sigmacr;

import gate.*;
import gate.creole.SerialAnalyserController;
import gate.creole.ontology.Ontology;
import gate.util.GateException;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Sasinda
 * Date: 6/28/13
 * Time: 2:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class OBIEPipelineBuilder {

    FeatureMap fm_onto;
    FeatureMap fm_orGaz;
    FeatureMap fm_flexGaz;
    FeatureMap fm_p0Jape;
    FeatureMap fm_p1Jape;
    FeatureMap fm_p2Jape;
    FeatureMap fm_p3Jape;
    private FeatureMap fm_p4Jape;


    public OBIEPipelineBuilder() {
        initialize();
    }

    /**
     * init the feature maps. config param here.
     */
    private void initialize() {

        fm_onto = Factory.newFeatureMap();
        fm_orGaz = Factory.newFeatureMap();
        fm_flexGaz = Factory.newFeatureMap();
        fm_p0Jape = Factory.newFeatureMap();
        fm_p1Jape = Factory.newFeatureMap();
        fm_p2Jape = Factory.newFeatureMap();
        fm_p3Jape = Factory.newFeatureMap();
        fm_p4Jape = Factory.newFeatureMap();

        Properties props = PropertyHolder.getProperties();
        URL ont_file_url = null;
        String ont_base_uri = null;
        String gaz_propsToInclude = props.getProperty("owl_propertiesToInclude");
        String gaz_propsToExclude = props.getProperty("owl_propertiesToExclude");
        URL p0_Jape_url = null;
        URL p1_Jape_url = null;
        URL p2_Jape_url = null;
        URL p3_Jape_url = null;
        URL p4_Jape_url = null;

        try {
            ont_file_url = new URL(props.getProperty("owlFile"));
            ont_base_uri = props.getProperty("OntoBaseURI");
            p0_Jape_url = new URL(props.getProperty("grammarP0"));
            p1_Jape_url = new URL(props.getProperty("grammarP1"));
            p2_Jape_url = new URL(props.getProperty("grammarP2"));
            p3_Jape_url = new URL(props.getProperty("grammarP3"));
            p4_Jape_url = new URL(props.getProperty("grammarP4"));


        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        //ontology LR
        fm_onto.put("rdfXmlURL", ont_file_url);
        fm_onto.put("baseURI", ont_base_uri);
        // fm_onto. put(" mappingsURL ", urlOfTheMappingsFile );
        //onto root gaz
        fm_orGaz.put("propertiesToExclude", gaz_propsToExclude);
        fm_orGaz.put("propertiesToInclude", gaz_propsToInclude);
        fm_orGaz.put("considerHeuristicRules", true);
        //flex gaz
        fm_p0Jape.put("grammarURL", p0_Jape_url);
        fm_p1Jape.put("grammarURL", p1_Jape_url);
        fm_p2Jape.put("grammarURL", p2_Jape_url);
        fm_p3Jape.put("grammarURL", p3_Jape_url);
        fm_p4Jape.put("grammarURL", p4_Jape_url);

    }


    public CorpusController createPipeline() {

        try {
            //register creole plugin directories.
            Gate.getCreoleRegister().registerDirectories(new File(Gate.getPluginsHome(), "ANNIE").toURI().toURL());
            Gate.getCreoleRegister().registerDirectories(new File(Gate.getPluginsHome(), "Tools").toURI().toURL());
            Gate.getCreoleRegister().registerDirectories(new File(Gate.getPluginsHome(), "Tagger_NP_Chunking").toURI().toURL());
            Gate.getCreoleRegister().registerDirectories(new File(Gate.getPluginsHome(), "Gazetteer_Ontology_Based").toURI().toURL());
            Gate.getCreoleRegister().registerDirectories(new File(Gate.getPluginsHome(), "Ontology_Tools").toURI().toURL());
            Gate.getCreoleRegister().registerDirectories(new File(Gate.getPluginsHome(), "Ontology").toURI().toURL());



            //reset pr
            ProcessingResource documentResetPR = (ProcessingResource) Factory.createResource("gate.creole.annotdelete.AnnotationDeletePR");
            //lexical analysis
            ProcessingResource sentenceSplitterPR = (ProcessingResource) Factory.createResource("gate.creole.splitter.SentenceSplitter");
            ProcessingResource engTokenizerPR = (ProcessingResource) Factory.createResource("gate.creole.tokeniser.DefaultTokeniser");
            ProcessingResource posTaggerPR = (ProcessingResource) Factory.createResource("gate.creole.POSTagger");
            ProcessingResource morpherPR = (ProcessingResource) Factory.createResource("gate.creole.morph.Morph");
            ProcessingResource npChunkerPR = (ProcessingResource) Factory.createResource("mark.chunking.GATEWrapper");
            ProcessingResource vpChunkerPR = (ProcessingResource) Factory.createResource("gate.creole.VPChunker");

            //Ontology
            Ontology ontologyLR = (Ontology) Factory.createResource("gate.creole.ontology.impl.sesame.OWLIMOntology", fm_onto);
            //ontology gaz
            fm_orGaz.put("ontology", ontologyLR);
            fm_orGaz.put("tokeniser", engTokenizerPR);
            fm_orGaz.put("posTagger", posTaggerPR);
            fm_orGaz.put("morpher", morpherPR);
            ProcessingResource ontoRootGazPR = (ProcessingResource) Factory.createResource("gate.clone.ql.OntoRootGaz", fm_orGaz);
            //flexi gaz
            fm_flexGaz.put("gazetteerInst", ontoRootGazPR);
            List<String> inputFnamesList = new ArrayList<String>(1);
            inputFnamesList.add("Token.root");
            fm_flexGaz.put("inputFeatureNames", inputFnamesList);
            ProcessingResource flexGaz_ontoRoot_PR = (ProcessingResource) Factory.createResource("gate.creole.gazetteer.FlexibleGazetteer");


            //Jape Transducers PR
            ProcessingResource p0_JapePR = (ProcessingResource) Factory.createResource("gate.creole.Transducer", fm_p0Jape);
            ProcessingResource p1_JapePR = (ProcessingResource) Factory.createResource("gate.creole.Transducer", fm_p1Jape);
            ProcessingResource p2_JapePR = (ProcessingResource) Factory.createResource("gate.creole.Transducer", fm_p2Jape);
            ProcessingResource p3_JapePR = (ProcessingResource) Factory.createResource("gate.creole.Transducer", fm_p3Jape);
            ProcessingResource p4_JapePR = (ProcessingResource) Factory.createResource("gate.creole.Transducer", fm_p4Jape);
            //set runtime param ontology.
            p1_JapePR.setParameterValue("ontology", ontologyLR);
            p2_JapePR.setParameterValue("ontology", ontologyLR);

            //pronominal coref. P4_JapePR executed after this
            ProcessingResource corefPr = (ProcessingResource) Factory.createResource("gate.creole.coref.PronominalCoref");
            //annotation set transfer pr
            ProcessingResource annotSetTranPR = (ProcessingResource) Factory.createResource(" gate.creole.annotransfer. AnnotationSetTransfer");
            ArrayList<String> tranfAnns = new ArrayList<String>();
            tranfAnns.add("FeatureSuspectSet");
            tranfAnns.add("Lookup");
            tranfAnns.add("Mention");
            tranfAnns.add("FSuspect");
            annotSetTranPR.setParameterValue("annotationTypes", tranfAnns);
            annotSetTranPR.setParameterValue("copyAnnotations", false);

            //ProcessingResource orthoMatcherPR = (ProcessingResource) Factory.createResource("gate.creole.");


            SerialAnalyserController pipeline = (SerialAnalyserController) Factory.createResource("gate.creole.SerialAnalyserController", Factory.newFeatureMap(), Factory.newFeatureMap(), "ANNIE");
            pipeline.add(documentResetPR);
            pipeline.add(sentenceSplitterPR);
            pipeline.add(engTokenizerPR);
            pipeline.add(posTaggerPR);
            pipeline.add(morpherPR);
            pipeline.add(npChunkerPR);
            pipeline.add(vpChunkerPR);
            pipeline.add(flexGaz_ontoRoot_PR);
            pipeline.add(p0_JapePR);
            pipeline.add(p1_JapePR);
            pipeline.add(p2_JapePR);
            pipeline.add(p3_JapePR);
            // pipeline.add(orthoMatcherPR);
            pipeline.add(corefPr);
            pipeline.add(p4_JapePR);
            pipeline.add(annotSetTranPR);

            return pipeline;

        } catch (GateException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }


}