import gate.Annotation;
import gate.AnnotationSet;
import gate.Factory;
import gate.FeatureMap;
import gate.jape.ActionContext;
import gate.jape.JapeException;
import gate.util.InvalidOffsetException;

import java.io.Serializable;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Sasinda
 * Date: 10/1/13
 * Time: 2:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class P2_PowerConsumption implements Serializable, gate.jape.RhsAction {
    private ActionContext ctx;

    AnnotationSet tagAnnots;

    public void doit(
            gate.Document doc,
            Map<String, AnnotationSet> bindings,
            gate.AnnotationSet annotations,
            gate.AnnotationSet inputAS,
            gate.AnnotationSet outputAS,
            gate.creole.ontology.Ontology ontology) throws JapeException {
        // your RHS Java code will be embedded here ...

        Annotation lookup = tagAnnots.iterator().next();
        Long end = tagAnnots.lastNode().getOffset();
        Long start = tagAnnots.firstNode().getOffset();

        AnnotationSet tokens = annotations.get("Token", start, end);
        boolean noPunc=true;  // check if a , or . is in the middle of the two lookup. power, consumption of water....
        for(Annotation t:tokens){
            if(t.getFeatures().get("kind").equals("punctuation")){
                noPunc=false;
                break;
            }
        }
        System.out.println(noPunc);
        if (noPunc) {
            FeatureMap features = Factory.newFeatureMap();
            features.put("class",  lookup.getFeatures().get("URI"));
            try {
                outputAS.add(start, end, "FeatureSection", features);
            } catch (InvalidOffsetException e) {
                e.printStackTrace();
            }
        } else {
            //ignore
        }
    }

    @Override
    public void setActionContext(ActionContext actionContext) {
    }

    public ActionContext getActionContext() {
        return null;
    }

    public String ruleName() {
        return null;
    }

    public String phaseName() {
        return null;
    }
}