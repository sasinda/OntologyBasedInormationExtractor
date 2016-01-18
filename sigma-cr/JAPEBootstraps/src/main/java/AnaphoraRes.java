import gate.Annotation;
import gate.AnnotationSet;
import gate.Factory;
import gate.FeatureMap;
import gate.jape.ActionContext;
import gate.jape.JapeException;
import gate.util.InvalidOffsetException;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Sasinda
 * Date: 10/1/13
 * Time: 3:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class AnaphoraRes implements Serializable, gate.jape.RhsAction {
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

        gate.AnnotationSet tagSet = (gate.AnnotationSet) bindings.get("tag");
        gate.Annotation it = (gate.Annotation) tagSet.iterator().next();
        List<Integer> matchList= (List<Integer>) it.getFeatures().get("matches");
        String antecedentClassName = (String)inputAS.get(matchList.get(0)).getFeatures().get("class");

        it.getFeatures().put("class", antecedentClassName);
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