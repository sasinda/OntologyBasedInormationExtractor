package com.sigmacr.mongodb;

import com.mongodb.DBCursor;
import com.sigmacr.domain.Document;

/**
 * Created with IntelliJ IDEA.
 * User: Sasinda
 * Date: 8/23/13
 * Time: 3:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class Test {
    public static void main(String[] args) {
        MongoAdapter mongo=new MongoAdapter();
        Document doc=new Document("TV",null,null, null);
        DBCursor dbCur = mongo.getDocsWith_greaterThan(doc,"features.PictureQuality.score",2d);
        //mongo.get
        while (dbCur.hasNext()){
            System.out.println(dbCur.next());
        }

    }
}
