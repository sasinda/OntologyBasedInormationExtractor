package com.sigmacr.aggr;

import com.sigmacr.domain.Document;
import com.sigmacr.domain.Sentiment;
import com.sigmacr.mongodb.MongoAdapter;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Sasinda
 * Date: 8/20/13
 * Time: 7:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    public static void main(String[] args) throws MalformedURLException {
//        DocSummaryWriter docSWriter=new DocSummaryWriter();

//
//        Document doc=new Document("TV","Sony Bravia sb1000","sasinda", new Date());
//        doc.getFeatureScores().put("pictureQuality",new Sentiment("good", 5));
//        Document doc2=new Document("TV","Samsung ExL5500","rukshan", new Date());
//        doc.getFeatureScores().put("soundQuality", new Sentiment("very good", 8));
//        doc.getFeatureScores().put("colorQuality", new Sentiment("awesome", 10));
//
//        docSWriter.initializeFor(new URL(PropertyHolder.getProperties().getProperty("testDocRoot")),"TV","Sony Bravia sb1000","sasinda", new Date());
//        docSWriter.markFeature("pictureQuality",new Sentiment("good", 5));
//        docSWriter.initializeFor(new URL(PropertyHolder.getProperties().getProperty("testDocRoot")),"TV","Samsung EL5500","rukshan", new Date());
//        docSWriter.markFeature("soundQuality", new Sentiment("very good", 8));
//        docSWriter.markFeature("colorQuality", new Sentiment("awesome", 10));
//        docSWriter.close();
//        MongoAdapter mongo=new MongoAdapter();
//        mongo.init();
//        System.out.println(doc.getCategory());
//        System.out.println(mongo.addDocument(doc));
//        System.out.println(mongo.addDocument(doc2));

        AggregatorExecuter ae=new AggregatorExecuter();
        ae.executeOn(new File("C:\\Users\\Sasinda\\Desktop\\Test docs\\Samsung_UN40EH5300"));

    }
}
