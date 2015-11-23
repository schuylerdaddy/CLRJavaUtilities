import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by greghodgson on 9/27/15.
 */
public class OutPutConsumer extends OutputStream {
    StringBuilder buffer = new StringBuilder();

    @Override
    public void write(int b) throws IOException {

    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        if(len>0) {
            buffer.append(new String(b));
        }
    }

    public String getString(){
        return buffer.toString();
    }
}
