package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by greghodgson on 9/15/15.
 */
public class DotNetServiceManager {

    private boolean errorOccurred = false;
    private boolean busy = false;
    private Process process;

    public InputStream getOutPutStream(HttpMethod method,String url, String body, Map<String,String> headers) throws Exception {
        if(headers == null){
            headers = new HashMap<>();
        }

        String [] args = getCommandListString(method.toString(), url,body, headers);
        process = Runtime.getRuntime().exec(args);

        return process.getInputStream();
    }

    private String[] getCommandListString(String command, String uri, String body, Map<String,String> params) throws Exception{
        ArrayList<String> args = new ArrayList<>();

        args.add("mono");
        args.add("CLIWebService.exe");
        if(command == null || command.equals("")){
            throw new Exception("MagicException:Domo encountered internal error with .NET request:Command cannot be null or empty");
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
                args.add("\"" + key + "\":\"" + params.get(key) + "\"");
            }
        }

        String[] retval = new String[args.size()];
        int idx = 0;
        for(String arg : args){
            retval[idx++] = arg;
        }

        return retval;
    }

    public InputStream getErrStream() throws Exception{
        if(process == null){
            throw new Exception("MagicException:Domo is ready but has encountered a temporary error:Process not started");
        }
        return process.getErrorStream();
    }
}
