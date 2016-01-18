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
class SampleAction implements Serializable, gate.jape.RhsAction {
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
        Integer hl=(Integer)lookup.getFeatures().get("heuristic_level");
        if(hl!=null){
            Long hlL=new Long(hl);
            lookup.getFeatures().put("heuristic_level",hlL);
        }
        String content = null;
        try {
            content = doc.getContent().getContent(lookup.getStartNode().getOffset(),
                    lookup.getEndNode().getOffset()).toString();
        } catch (InvalidOffsetException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        System.out.println(content);

        if(hl==null){
            System.out.println("heuristic level is null for this "+content);
        }

    }

    @Override
    public void setActionContext(ActionContext actionContext) {
    }
    public ActionContext getActionContext() {return null;}

    public String ruleName() {return null;}

    public String phaseName() {return null;}
}