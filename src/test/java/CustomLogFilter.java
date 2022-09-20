import io.qameta.allure.attachment.http.HttpRequestAttachment;
import io.qameta.allure.attachment.http.HttpResponseAttachment;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.internal.NameAndValue;
import io.restassured.internal.support.Prettifier;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

import static io.qameta.allure.attachment.http.HttpResponseAttachment.Builder.create;
import static java.util.Optional.ofNullable;

public class CustomLogFilter implements Filter {
        private String requestAttachmentName = "Request";
        private String responseAttachmentName = "Response";
    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext filterContext) {
            File reportfile = new File("report.txt");
            FileWriter myWriter=null ;
            try {
                    reportfile.createNewFile();
                    myWriter = new FileWriter("report.txt");
            } catch (IOException e) {
                    e.printStackTrace();
            }

            final Prettifier prettifier = new Prettifier();
            final String url = requestSpec.getURI();
            final HttpRequestAttachment.Builder requestAttachmentBuilder = HttpRequestAttachment.Builder.create(requestAttachmentName, url)
                    .setMethod(requestSpec.getMethod())
                    .setHeaders(toMapConverter(requestSpec.getHeaders()))
                    .setCookies(toMapConverter(requestSpec.getCookies()));

            if (Objects.nonNull(requestSpec.getBody())) {
                    requestAttachmentBuilder.setBody(prettifier.getPrettifiedBodyIfPossible(requestSpec));
            }

            final HttpRequestAttachment requestAttachment = requestAttachmentBuilder.build();
            System.out.println(requestAttachment.toString());
            try {
                    myWriter.append("Request Details:\n");
                    myWriter.append("URL:"+requestAttachment.getUrl()+"\n");
                    myWriter.append("Method:"+requestAttachment.getMethod()+"\n");
                    myWriter.append("Headers:"+requestAttachment.getHeaders().toString()+"\n");
                    myWriter.append("Cookies:"+requestAttachment.getCookies().toString()+"\n");
                    myWriter.append("Body:"+requestAttachment.getBody()+"\n");
                    myWriter.append("Curl request:"+requestAttachment.getCurl().replace("\n","")+"\n");
            } catch (IOException e) {
                    e.printStackTrace();
            }
            //This is to get the response details
            Response response=null;
            try {
                    response = filterContext.next(requestSpec, responseSpec);

                    final String attachmentName = ofNullable(responseAttachmentName)
                            .orElse(response.getStatusLine());

                    final HttpResponseAttachment responseAttachment = create(attachmentName)
                            .setResponseCode(response.getStatusCode())
                            .setHeaders(toMapConverter(response.getHeaders()))
                            .setBody(prettifier.getPrettifiedBodyIfPossible(response, response.getBody()))
                            .build();
                    System.out.println(responseAttachment.toString());
                    myWriter.append("Status code:"+Integer.toString(responseAttachment.getResponseCode())+"\n");
                    //myWriter.append("Response Headers:\n"+responseAttachment.getHeaders());
                    myWriter.append("Response:\n"+responseAttachment.getBody()+"\n");
            }
            catch (Exception e)
            {
                    try {
                            StringWriter sw = new StringWriter();
                            PrintWriter pw = new PrintWriter(sw);
                            e.printStackTrace(pw);
                            //myWriter.append("Response:\n"+e+"\n");
                            myWriter.append("Response:\n"+sw.toString());

                    } catch (IOException ex) {
                            ex.printStackTrace();
                    }
            }

            try {
                    myWriter.close();
            } catch (IOException e) {
                    e.printStackTrace();
            }
            Scanner scanner= null;
            try {
                    scanner = new Scanner(reportfile);
            } catch (FileNotFoundException e) {
                    e.printStackTrace();
            }
            while(scanner.hasNextLine()){
                    String str=scanner.nextLine();
                    System.out.println(str);
            }
            return response;
    }
        private static Map<String, String> toMapConverter(final Iterable<? extends NameAndValue> items) {
                final Map<String, String> result = new HashMap<>();
                items.forEach(h -> result.put(h.getName(), h.getValue()));
                return result;
        }
}
