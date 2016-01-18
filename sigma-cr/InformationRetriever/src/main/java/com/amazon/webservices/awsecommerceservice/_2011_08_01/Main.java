package com.amazon.webservices.awsecommerceservice._2011_08_01;

import sigmacr.informationretriver.structure.handlers.FileAssistent;
import sigmacr.informationretriver.webservice.adapter.AWSAdapter;

public class Main {



    public static void main(String[] args) {


        FileAssistent fileAssistent= new FileAssistent();
        AWSAdapter awsAdapter = new AWSAdapter();



        String searchProdcut="";
        String fileSaveLocation=Constants.defaultSaveLocation;
         if(args.length<1){
             System.out.println("please provide product to search");
         }  else if(args.length==1){
             System.out.println("using default location"+fileSaveLocation+" to save reviews");
             searchProdcut=args[0];
             String[] reviewLinks=awsAdapter.getProductUrls(searchProdcut);
              fileSaveLocation=fileAssistent.createSearchFolder(fileSaveLocation, searchProdcut);
             System.out.println("calling scrapper");
             for(int j=0;j<reviewLinks.length;j++){
                 if(Constants.no_of_pages==j){
                     break;
                 }
                 fileAssistent.callScraper(reviewLinks[j], fileAssistent.createFilePath(fileSaveLocation, reviewLinks[j]));
             }
        }  else if(args.length==2){
             searchProdcut=args[0];
             fileSaveLocation=fileAssistent.createSearchFolder(args[1], searchProdcut);
             System.out.println("using file save location"+fileSaveLocation+" to save reviews");
             String[] reviewLinks=awsAdapter.getProductUrls(searchProdcut);

             System.out.println("calling scrapper");
             for(int j=0;j<reviewLinks.length;j++){
                 if(Constants.no_of_pages==j){
                     break;
                 }

                 fileAssistent.callScraper(reviewLinks[j], fileAssistent.createFilePath(fileSaveLocation, reviewLinks[j]));
             }
         } else {
             System.out.println("wrong number of arguments");
         }


    }



}
