import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by greghodgson on 9/15/15.
 */
public class DotNetServiceManager {

    private Process process;
    Pipe stderr;
    ErrorConsumer errConsumer = new ErrorConsumer();

    public String get(String url, Map<String,String> headers) throws Exception{
        InputStream is = getOutPutStream(HttpMethod.GET,url,null,headers);
        OutPutConsumer out = new OutPutConsumer();
        Pipe stdout = new Pipe(is, out);
        stdout.start();
        stdout.join();
        return out.getString();
    }

    public String post(String url, String body, Map<String,String> headers) throws Exception{
        InputStream is = getOutPutStream(HttpMethod.POST,url,body,headers);
        OutPutConsumer out = new OutPutConsumer();
        Pipe stdout = new Pipe(is, out);
        stdout.start();
        stdout.join();
        return out.getString();
    }

    public InputStream getOutPutStream(HttpMethod method,String url, String body, Map<String,String> headers) throws Exception {
        if(headers == null){
            headers = new HashMap<>();
        }

        String [] args = getCommandListString(method.toString(), url,body, headers);
        process = Runtime.getRuntime().exec(args);

        stderr = new Pipe(process.getErrorStream(),errConsumer);
        stderr.start();
        return process.getInputStream();
    }

    private String[] getCommandListString(String command, String uri, String body, Map<String,String> params) throws Exception{
        ArrayList<String> args = new ArrayList<>();

        args.add("mono");
        args.add("CLIWebService.exe");
        if(command == null || command.equals("")){
            throw new Exception("MagicException: Domo encountered internal error with .NET request:Command cannot be null or empty");
        }
        args.add(command);

        if(body != null && !body.trim().equals("")){
            args.add("body:"+body);
        }

        if(uri != null && !uri.trim().equals("")){
            args.add("uri:"+uri);
        }

        if(params != null) {
            for (String key : params.keySet()) {
                args.add(key +":"+ params.get(key));
            }
        }

        String[] retval = new String[args.size()];
        int idx = 0;
        for(String arg : args){
            retval[idx++] = arg;
        }

        return retval;
    }

    public InputStream getWSDLServiceResultStream(String serviceName, Map<String,String> headers) throws Exception{
        if(headers == null){
            headers = new HashMap<>();
        }
        headers.put("service",serviceName);
        String [] args = getCommandListString("wsdl", null,null, headers);
        process = Runtime.getRuntime().exec(args);

        stderr = new Pipe(process.getErrorStream(),errConsumer);
        stderr.start();
        return process.getInputStream();
    }

    public String getWSDLServiceResultString(String serviceName, Map<String,String> args) throws Exception{
        InputStream is = getWSDLServiceResultStream(serviceName, args);
        OutPutConsumer out = new OutPutConsumer();
        Pipe stdout = new Pipe(is, out);
        stdout.start();
        stdout.join();

        return out.getString();
    }

    public void waitForErrors() throws Exception{
        if(process != null){
            stderr.join();
            if(errConsumer.errorOccured())
                throw new Exception(".NET error: "+errConsumer.getError());
        }

    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if(process != null){
            stderr.join();
            if(errConsumer.errorOccured())
                throw new Exception(".NET error: "+errConsumer.getError());
        }
    }
}
