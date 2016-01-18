/**
 * Created with IntelliJ IDEA.
 * User: Sasinda
 * Date: 5/7/13
 * Time: 5:55 PM
 * To change this template use File | Settings | File Templates.
 */

import java.io.*;
import java.util.*;

import gate.*;
import gate.jape.*;
import gate.creole.ontology.*;
import gate.annotation.*;
import gate.util.*;

// Import: block code will be embedded here
class JJNegations implements Serializable, gate.jape.RhsAction {
    private ActionContext ctx;

    AnnotationSet tagAnnots;
    public void doit(
            gate.Document doc,
            Map<String, gate.AnnotationSet> bindings,
            gate.AnnotationSet annotations,
            gate.AnnotationSet inputAS,
            gate.AnnotationSet outputAS,
            gate.creole.ontology.Ontology ontology) throws JapeException {
        // your RHS Java code will be embedded here ...
        Annotation lookup=tagAnnots.iterator().next();
        lookup.getFeatures().put("negation",true );
    }

    @Override
    public void setActionContext(ActionContext actionContext) {
    }
    public ActionContext getActionContext() {return null;}

    public String ruleName() {return null;}

    public String phaseName() {return null;}
}