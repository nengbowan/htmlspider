package test.com.fsy.spider;

import com.github.axet.wget.WGet;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlTests {
    public static void main(String[] args) {
        try {

            //不自己去转路径 由库解决底层问题
            URL url = new URL("http://www.baidu.com/dfa/dafadf/../../");


            System.out.println(WGet.getHtml(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
