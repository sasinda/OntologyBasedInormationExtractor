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
class SuspectSplit implements Serializable, gate.jape.RhsAction {
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

        Annotation featureSet=tagAnnots.iterator().next();
       // System.out.println("---------------------------------------------------");


        try {
            Long start= featureSet.getStartNode().getOffset();
            Long end =featureSet.getEndNode().getOffset() ;
            AnnotationSet toks = inputAS.get("Token", start, end);
            List<Annotation> tokens = new ArrayList<Annotation>(toks);
            Collections.sort(tokens, new OffsetComparator());

            DocumentContent con = doc.getContent().getContent(start ,end);
            String content=con.toString();
           // System.out.println(content);

            //System.out.println("start"+start+ "  end:"+end);
            String suspect;
            boolean split;
            Long s,e;
            List<Annotation> susTokens= new ArrayList<Annotation>(3);
            Annotation token;
            for (int i = 0; i <tokens.size() ; i++) {
                token=tokens.get(i);
                String cat= (String) token.getFeatures().get("category");
                split = cat.equals("CC") || cat.equals(",");
                if(!split){
                    susTokens.add(token);
                }else{
                    if( susTokens.size()>0){
                        s=susTokens.get(0).getStartNode().getOffset();
                        e=susTokens.get(susTokens.size()-1).getEndNode().getOffset();
                   //     System.out.println("s"+s+ "  e:"+e);
                        outputAS.add(s,e,"Suspect",Factory.newFeatureMap());
                        susTokens.clear();
                    }
                }
            }

            if( susTokens.size()>0){
                s=susTokens.get(0).getStartNode().getOffset();
                e=susTokens.get(susTokens.size()-1).getEndNode().getOffset();
               // System.out.println("s"+s+ "  e:"+e);
                outputAS.add(s,e,"Suspect",Factory.newFeatureMap());
            }

           // System.out.println("---------------------------------------------------");

        } catch (InvalidOffsetException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

    @Override
    public void setActionContext(ActionContext actionContext) {
    }
    public ActionContext getActionContext() {return null;}

    public String ruleName() {return null;}

    public String phaseName() {return null;}
}