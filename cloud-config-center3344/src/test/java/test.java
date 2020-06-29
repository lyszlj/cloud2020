import org.openjdk.jol.info.ClassLayout;

import java.io.UnsupportedEncodingException;

public class test {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String a = "";
        System.out.println( a.getBytes("GBK").length);
    }
}
