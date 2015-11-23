import java.io.InputStream;
import java.lang.String;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws Exception{

        try {
            DotNetServiceManager dnsm = new DotNetServiceManager();

            HashMap<String,String> map = new HashMap<>();
            map.put("temperature","1");
            System.out.println(dnsm.getWSDLServiceResultString("CelsiusToFarenheit",map));
            dnsm.waitForErrors();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
