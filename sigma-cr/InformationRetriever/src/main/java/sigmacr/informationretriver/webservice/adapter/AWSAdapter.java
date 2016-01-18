package sigmacr.informationretriver.webservice.adapter;

import com.amazon.webservices.awsecommerceservice._2011_08_01.*;

import javax.xml.ws.Holder;
import java.util.ArrayList;
import java.util.List;

public class AWSAdapter
{


    public String[] getProductUrls(String searchProduct) {


// Set the Service:
        AWSECommerceService service = new AWSECommerceService();
        service.setHandlerResolver(new AwsHandlerResolver(Constants.getAmazonSecurityKey("sigmacr")));

//Set the Service port:
        AWSECommerceServicePortType port = service.getAWSECommerceServicePort();

//Get the operation object:
        ItemSearchRequest itemSearchRequest = new ItemSearchRequest();

//Fill in the request object:
        itemSearchRequest.setSearchIndex("All");
        itemSearchRequest.setKeywords(searchProduct);

//itemRequest.setVersion("2010-10-01");

//ItemElement.getRequest().add(itemRequest);

        List<ItemSearchRequest> request = new ArrayList<ItemSearchRequest>();
        request.add(itemSearchRequest);

//Call the Web Service operation and store the response
//in the response object:
        Holder<OperationRequest> operationRequest = new Holder<OperationRequest>();
        Holder<List<Items>> items = new Holder<List<Items>>();

        port.itemSearch("", Constants.getAmazonAcessId("sigmacr"), "galaxy", "", "", itemSearchRequest, request, operationRequest, items);
//port.itemSearch(ItemElement);

        System.out.println(items.value.get(0).getMoreSearchResultsUrl());


        List<Items> result = items.value;

        String[] productUrls = new String[result.get(0).getItem().size()];

        for (int i = 0; i < result.get(0).getItem().size(); ++i) {

            Item myItem = result.get(0).getItem().get(i);
            productUrls[i] = myItem.getDetailPageURL();
        }

        return productUrls;
    }

}
