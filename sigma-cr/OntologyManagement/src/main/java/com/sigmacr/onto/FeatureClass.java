package com.sigmacr.onto;

import com.hp.hpl.jena.ontology.AnnotationProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.sigmacr.dict.LingUtil;
import com.sigmacr.dict.SynSuggestion;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sasinda
 * Date: 7/18/13
 * Time: 11:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class FeatureClass {
    OntClass f;
    FeatureClass parent;
    String name;
    Set<FeatureClass> cfs = null;
    private static OntModel model;

    public FeatureClass(OntClass feature) {
        f = feature;
        name = f.getLocalName();
        if (model == null) {
            model = OntoManager.getInstance().getModel();
        }
    }

    public FeatureClass(OntClass feature, FeatureClass parent) {
        this(feature);
        this.parent = parent;
    }

    public OntClass getOntClass() {
        return f;
    }

    /**
     * return labels of the class. (in row form. ex: PictureQuality)
     *
     * @return
     */
    public List<String> getLabels() {
        String name = f.getLocalName();
        ExtendedIterator<RDFNode> iterator = f.listLabels(null);
        List<String> labels = new ArrayList<String>(3);
        while (iterator.hasNext()) {
            labels.add(iterator.next().asLiteral().getLexicalForm());
        }
        return labels;
    }

    public List<String> getAssociations() {
        List<String> associations = associations = new ArrayList<String>(4);
        AnnotationProperty associationAnn =PO.AP_ASSOCIATON;

        NodeIterator itrAssociation = f.listPropertyValues(associationAnn);
        while (itrAssociation.hasNext()) {
            String asso = itrAssociation.next().asLiteral().getLexicalForm();
            associations.add(asso);
        }
        return associations;
    }

    /**
     * *
     *
     * @return all direct children of this feature
     */
    public Set<FeatureClass> getChildFeatures() {
        if (cfs == null) {

            ExtendedIterator<OntClass> children = f.listSubClasses(true);
            if (children.hasNext()) {
                cfs = new HashSet<FeatureClass>(15);
            }
            while (children.hasNext()) {
                cfs.add(new FeatureClass(children.next(), this));
            }
        }
        return cfs;
    }

    public Set<FeatureClass> getChildFeatures(boolean direct) {

        if (direct == false) {
            Set<FeatureClass> decendatns = null;

            ExtendedIterator<OntClass> children = f.listSubClasses(false);
            if (children.hasNext()) {
                decendatns = new HashSet<FeatureClass>(15);
            }
            while (children.hasNext()) {
                decendatns.add(new FeatureClass(children.next()));
            }
            return decendatns;
        }
        return cfs;
    }


    /**
     * @return parent feature of this feature regarding to the product which instantiated this FeatureClass.
     *         null if this was instantiated out of context of a product or if the feature does not have a parent feature.(lower than Qualitative/QuantitativeFeature class)
     */

    public FeatureClass getParentFeature() {
        return parent;
    }

    public boolean hasParentFeature(FeatureClass fc) {
        FeatureClass parent = this.getParentFeature();
        while (parent != null) {
            if (parent.equals(fc)) {
                return true;
            }
            parent = parent.parent;
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FeatureClass)) return false;
        return ((FeatureClass) obj).getOntClass().getURI().equals(f.getURI());    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public int hashCode() {
        return name.hashCode();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public String toString() {
        return "FeatureClass: " + name;    //To change body of overridden methods use File | Settings | File Templates.
    }

    public String toDotString() {
        FeatureClass curr = this;
        Stack<String> stack = new Stack<String>();
        stack.push(this.name);
        while (curr.parent != null) {
            curr = curr.parent;
            stack.push(curr.name);
        }
        StringBuilder sb = new StringBuilder();
        while (!stack.empty()) {
            sb.append(stack.pop() + ".");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public String getName() {
        return name;
    }


}
