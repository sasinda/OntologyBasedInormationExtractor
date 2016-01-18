package com.sigmacr.onto;


import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sasinda
 * Date: 5/27/13
 * Time: 3:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class OntoManager {

    final Logger logger = LoggerFactory.getLogger(OntoManager.class);

    OntModel model;
    File localFile;
    URL remoteUrl;
    private boolean isLoaded = false;
    private String NS;
    private static OntoManager me;

    public static synchronized OntoManager getInstance() {
        if (me == null) {
            me = new OntoManager();
        }
        return me;
    }

    private OntoManager() {
        model = ModelFactory.createOntologyModel();  //OntModelSpec.OWL_MEM_RULE_INF
        Properties p=PropertyHolder.getProperties();
        String path=p.getProperty("owlFile") ;
        try {
            remoteUrl = new URL(p.getProperty("remoteUrl"));               //"http://www.co-ode.org/ontologies/pizza/pizza.owl"
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        localFile = new File(path);         //  /product-ontology-1.1/

    }

    public void load() {
        if(isLoaded){
            logger.info("OntoManager: ontology already Loaded. To reload call reload()");return;
        }
        OntDocumentManager dm = model.getDocumentManager();
        String localPath = null;
        try {
            localPath = localFile.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        dm.addAltEntry(remoteUrl.toString(), "file:" + localPath);
        model.read(remoteUrl.toString());

        NS = getOntologyIRI() + "#";
        isLoaded = true;
        logger.info("ontology model loaded");

    }

    public void save() {
        try {
            model.write(new FileOutputStream(localFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public OntModel getModel() {
        if (!isLoaded) {
            throw new IllegalStateException("ontology not yet loaded");
        }
        return model;
    }

    public String getOntologyIRI() {

        OntModel mBase = ModelFactory.createOntologyModel(
                OntModelSpec.OWL_MEM, model.getBaseModel());

        String ontIri = null;
        for (Iterator i = mBase.listOntologies(); i.hasNext(); ) {
            Ontology ont = (Ontology) i.next();
            ontIri = ont.getURI();
        }
        return ontIri;
    }

    /**
     *
     * @param name  short name or full name of the product ontology class. ex: TV  or http://cse.....#TV
     * @return  wrapper for OntClass.
     */
    public ProductClass getProductClass(String name){
        OntClass ontClass;
        if(name.startsWith("http://")){
              ontClass=model.getOntClass(name);
         }else{
              ontClass= model.getOntClass(NS + name);
         }

        if(ontClass==null) {
            throw new IllegalArgumentException("class not found in the ontology iri/name="+name);
        }
        ProductClass pcls=new ProductClass(ontClass);
        return pcls;
    }

    public FeatureClass getFeatureClass(String name){
        OntClass ontClass;
        if(name.startsWith("http://")){
            ontClass=model.getOntClass(name);
        }else{
            ontClass= model.getOntClass(NS + name);
        }

        if(ontClass==null) {
            throw new IllegalArgumentException("class not found in the ontology iri/name="+name);
        }
        FeatureClass fcls=new FeatureClass(ontClass);
        return fcls;
    }


    /**
     * returns super class chain until cls Product or Feature.  If multiple inheritance then any super class chain.
     *
     * @param ocls Product or Feature class
     * @return
     */
    public List<OntClass> getSuperClasses(OntClass ocls) {
        OntClass pCls = model.getOntClass(NS + "Product");
        OntClass fCls = model.getOntClass(NS + "Feature");

        Vector<OntClass> v = new Vector<OntClass>();
        OntClass stopCls = null;
        if (ocls.hasSuperClass(pCls)) { //if product
            stopCls = pCls;
        } else if (ocls.hasSuperClass(fCls)) {//if feature
            stopCls = fCls;
        }
        boolean con = true;
        OntClass sup = ocls;
        while (con) {
            sup = sup.getSuperClass();
            if (sup.equals(stopCls)) {
                con = false;
            } else {
                v.add(sup);
            }
        }
        return v;
    }

    /**
     * Returns map with superclasses and there distance.
     * direct superclasses have distance 1. The next superclasses have dist 2 and etc.
     *
     * @param ocls
     * @return
     */
    public Map<Integer, List<OntClass>> getSuperClassesWithDistance(OntClass ocls) {
        OntClass pCls = model.getOntClass(NS + "Product");
        OntClass fCls = model.getOntClass(NS + "Feature");

        OntClass stopCls = null;
        if (ocls.hasSuperClass(pCls)) { //if product
            stopCls = pCls;
        } else if (ocls.hasSuperClass(fCls)) {//if feature
            stopCls = fCls;
        }
        boolean esc = false;
        List<OntClass> supList = null;
        List<OntClass> currList = new ArrayList<OntClass>();
        currList.add(ocls);
        Map<Integer, List<OntClass>> distMap = new TreeMap<Integer, List<OntClass>>();
        int dist = 0;

        while (!esc) {
            dist++;
            esc = true;
            supList = new ArrayList<OntClass>();
            for (OntClass curr : currList) {
                ExtendedIterator<OntClass> supClasses = curr.listSuperClasses(true);
                while (supClasses.hasNext()) {
                    OntClass sup = supClasses.next();
                    if (sup.equals(stopCls)) {
                        esc = esc & true;
                    } else {
                        esc = esc & false;
                        supList.add(sup);
                    }
                }
            }
            if(supList.size()>0){
               distMap.put(dist, supList);
            }
            currList = supList;
        }
        return distMap;
    }

}
