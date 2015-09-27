import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by greghodgson on 9/20/15.
 */
public class Pipe extends Thread {
    private InputStream in;
    private OutputStream out;

    public Pipe(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }

    public void run() {
        try {
            int n;
            byte[] buffer = new byte[4096];
            while ((n = in.read(buffer)) != -1) {
                out.write(buffer, 0, n);
                out.flush();
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
}
