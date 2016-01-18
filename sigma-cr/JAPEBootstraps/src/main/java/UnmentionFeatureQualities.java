import java.io.*;
import java.util.*;

import gate.*;
import gate.jape.*;
import gate.creole.ontology.*;
import gate.annotation.*;
import gate.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sasinda
 * Date: 5/8/13
 * Time: 2:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class UnmentionFeatureQualities implements Serializable, gate.jape.RhsAction {
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

        Iterator<Annotation> itr=tagAnnots.get("Mention").iterator();
        Annotation ann1=itr.next();//1st mention
        Annotation ann2=itr.next();
        Node start=tagAnnots.firstNode();
        Node end=tagAnnots.lastNode();
        FeatureMap features;  //TODO:check if needs clone
        if(ann1.getStartNode().getOffset()<ann2.getStartNode().getOffset()){    //gate.utils.OffsetComparator can be used also.
           features= ann1.getFeatures();
        } else{
           features= ann2.getFeatures();
        }


        inputAS.remove(ann2);
        inputAS.remove(ann1);
        outputAS.add(start, end, "Mention", features);

        String content = null;
        try {
            content = doc.getContent().getContent(ann2.getStartNode().getOffset(),
                    ann2.getEndNode().getOffset()).toString();
        } catch (InvalidOffsetException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        System.out.println(ann1.getStartNode().getOffset());
        System.out.println(ann2.getStartNode().getOffset());

        System.out.println(content);



    }

    @Override
    public void setActionContext(ActionContext actionContext) {
    }
    public ActionContext getActionContext() {return null;}

    public String ruleName() {return null;}

    public String phaseName() {return null;}
}