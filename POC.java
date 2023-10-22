import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class xxeSAXReader {
    public static void main(String[] args) {
        ParseXMLUtil();
    }
    public static void ParseXMLUtil(){


        String xxe_payload1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<!DOCTYPE foo SYSTEM \"http://127.0.0.1:8888/xxe.dtd\">" +
                "<foo>&xxe;</foo>";
        String xxe_payload2 = "<!DOCTYPE name [<!ENTITY % catd09 SYSTEM \"http://127.0.0.1:8888/xxe.dtd\">%catd09;]>" +
                "<name ></name>";
        InputStream xmlFilePathStream = new ByteArrayInputStream(xxe_payload1.getBytes(StandardCharsets.UTF_8));

        try{
            if(xmlFilePathStream == null){
                throw new NullPointerException();
            }
            SAXReader reader = new SAXReader();


            //如果设置了下面一行，那么就无法调用外部通用实体，即payload1
            reader.setFeature("http://xml.org/sax/features/external-general-entities",false);

            //如果设置了下面一行，那么就无法调用外部参数实体，即payload2
            reader.setFeature("http://xml.org/sax/features/external-parameter-entities",false);


            //如果先限制外部参数实体，再限制外部通用实体是可以达到两个都限制的功能。
            //如果先限制外部通用实体，再限制外部参数实体就无法限制两个，只能限制外部参数。

            System.out.println("start");
            Document doc = reader.read(xmlFilePathStream);
            System.out.println(doc.getRootElement().getStringValue());

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
