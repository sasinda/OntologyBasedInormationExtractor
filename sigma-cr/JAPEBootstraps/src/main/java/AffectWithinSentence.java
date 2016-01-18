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
class AffectWithinSentence  implements Serializable, gate.jape.RhsAction {
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
        Annotation[] tokens=new Annotation[1];
        tokens= tagAnnots.toArray(tokens);
        Arrays.sort(tokens,new Comparator<Annotation>() {
            @Override
            public int compare(Annotation o1, Annotation o2) {
                return (int) (o1.getStartNode().getOffset() - o2.getStartNode().getOffset());  //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        String number="";
        for(Annotation token:tokens){
            number+= (String) token.getFeatures().get("string");
        }


        System.out.println(number);
        Double rating=new Double(number);
        doc.getFeatures().put("starRaging", rating);
    }

    @Override
    public void setActionContext(ActionContext actionContext) {
    }
    public ActionContext getActionContext() {return null;}

    public String ruleName() {return null;}

    public String phaseName() {return null;}
}