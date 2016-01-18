import com.sigmacr.onto.OntoManager;
import com.sigmacr.onto.ProductClass;
import gate.Annotation;
import gate.AnnotationSet;
import gate.jape.ActionContext;
import gate.jape.JapeException;
import gate.util.InvalidOffsetException;

import java.io.Serializable;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Sasinda
 * Date: 10/3/13
 * Time: 5:57 PM
 * To change this template use File | Settings | File Templates.
 */


public class ProductPrune implements Serializable, gate.jape.RhsAction {
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
        String category = (String) doc.getFeatures().get("category");
        Annotation mention = tagAnnots.iterator().next();
        String pName= (String) mention.getFeatures().get("class");

        OntoManager om = OntoManager.getInstance();
        ProductClass catClass = om.getProductClass(category);
        ProductClass pClass=om.getProductClass(pName);
        boolean sameCat  = pClass.getOntClass().hasSuperClass(catClass.getOntClass());//TODO: refactor to used direct method
        if(!sameCat){
           outputAS.remove(mention);
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