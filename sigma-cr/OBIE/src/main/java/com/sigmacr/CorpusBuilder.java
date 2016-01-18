package com.sigmacr; /**
 * Created with IntelliJ IDEA.
 * User: Sasinda
 * Date: 8/15/13
 * Time: 3:55 PM
 * To change this template use File | Settings | File Templates.
 */

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import gate.Corpus;
import gate.Document;
import gate.Factory;
import gate.creole.ResourceInstantiationException;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.MalformedURLException;


public class CorpusBuilder {
    Logger logger = Logger.getLogger(CorpusBuilder.class);
    private Corpus corpus;

    CorpusBuilder() {
        initiateCorpus();
    }

    public void initiateCorpus() {
        try {
            corpus = Factory.newCorpus("Review Corpus");
        } catch (ResourceInstantiationException ex) {
            logger.error("Gate Factory.newCorpus error");
        }
    }

    /**
     * contverts text files into Gate xml files. Builds a corpus. The directory should have a meta dir containing meta file wich denotes the
     * product type of the reviews in the folder.
     *
     * @param reviewDir
     * @param overwrite
     */
    public Corpus createCorpus(File reviewDir, boolean overwrite) {
        File[] listOfFiles = reviewDir.listFiles();
        //get meta data.
        File f = new File(reviewDir, PropertyHolder.metaDirName + "/" + PropertyHolder.catFileName);
        String cat = null;
        try {
            JsonReader reader = new JsonReader(new FileReader(f));
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("category")) {
                    cat = reader.nextString();
                }
            }
        } catch (FileNotFoundException e) {
            logger.error("no cat file found" + f.getAbsolutePath());
        } catch (IOException e) {
            logger.error("illegal cat file foramt");
        }
        if (cat == null) {
            throw new IllegalArgumentException("not a valid dir. Must have a meta-data dir with category.json file");
        }
        return createCorpus(listOfFiles, cat, overwrite);
    }

    /**
     * builds corpus from files in the directory. The files need to be in gate xml  format and the doc feature "category" needs to be available.
     *
     * @param reviewDir
     */
    public void createCorpus(File reviewDir) {
        createCorpus(reviewDir, false);
    }

    private Corpus createCorpus(File[] files, String category, boolean overwrite) {


        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (!f.isDirectory()) {
                logger.info("\t " + (i + 1) + ") " + f);
                try {
                    //transform original review file to gate xml
                    Document doc = null;
                    if (overwrite == true) {//overwrite if extension is txt and rename to xml.
                        if (f.getName().endsWith(".txt")) {

                            String movePath = f.getParent() + "/originals/" + f.getName();
                            String xmlFileName = f.getName().substring(0, f.getName().length() - 3);
                            xmlFileName += "xml";
                            File xmlFile = new File(f.getParent(), xmlFileName);

                            //move old to originals
                            File newFile = new File(movePath);
                            boolean ok = f.renameTo(newFile);

                            if (!ok) {
                                logger.error("couldn't move to originals dir:" + f);
                            }
                            //read old
                            doc = Factory.newDocument(newFile.toURI().toURL());
                            doc.getFeatures().put("category", category);

                            FileWriter writer = new FileWriter(xmlFile);
                            writer.write(doc.toXml());
                            writer.close();
                            doc.cleanup(); //close gate doc.


                            doc = Factory.newDocument(xmlFile.toURI().toURL());
                        } else {
                            doc = Factory.newDocument(f.toURI().toURL());
                        }
                    }else{
                        doc = Factory.newDocument(f.toURI().toURL());
                    }
                    corpus.add(doc);


                } catch (gate.creole.ResourceInstantiationException e) {
                    logger.error(" -- failed (" + e.getMessage() + ")");
                } catch (MalformedURLException e) {
                    logger.error(e);
                } catch (IOException e) {
                    logger.error(e);
                }
            }

        }


        return corpus;
    }

    public void changeFileExtension(String from, String to, File dir) {
        File[] listOfFiles = dir.listFiles();
        for (File f : listOfFiles) {
            if (f.getName().endsWith(from)) {
                String newName = f.getName().substring(0, f.getName().length() - from.length()) + to;
                File rn = new File(f.getParent(), newName);
                f.renameTo(rn);
                logger.debug("new file name" + f.getName());
            }
        }
    }

    public gate.Corpus getCorpus() {
        if (corpus != null) {
            return corpus;
        } else throw new NullPointerException("Corpus is not initialzed");
    }
}
