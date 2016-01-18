import gate.Annotation;
import gate.AnnotationSet;
import gate.jape.ActionContext;
import gate.jape.JapeException;
import gate.util.OffsetComparator;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Sasinda
 * Date: 8/23/13
 * Time: 2:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class Preprocess_Meta {
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
        StringBuilder sb = new StringBuilder();
        List<Annotation> tkns=new ArrayList<Annotation>();
        for(Annotation t:tagAnnots){
            tkns.add(t);
        }
        Collections.sort(tkns, new OffsetComparator());
        for(Annotation tkn:tkns){
            sb.append(tkn.getFeatures().get("string"));
        }
        String link=sb.toString();
        String[] split = link.split("/");
        String modelName=split[3];
        doc.getFeatures().put("modelName",modelName);
        doc.getFeatures().put("srcLink", link);
    }

    public void setActionContext(ActionContext actionContext) {
    }
    public ActionContext getActionContext() {return null;}

    public String ruleName() {return null;}

    public String phaseName() {return null;}
}
