import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.SortedMap;

/**
 * Created by greghodgson on 9/27/15.
 */
public class ErrorConsumer extends OutputStream {
    StringBuilder buffer = new StringBuilder();
    public boolean errorOccured = false;

    @Override
    public void write(int b) throws IOException {

    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        if(len>0) {
            errorOccured = true;
            buffer.append(new String(b));
        }
    }

    public boolean errorOccured(){
        return errorOccured;
    }

    public String getError(){
        return buffer.toString();
    }
}
