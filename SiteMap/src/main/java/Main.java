
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ForkJoinPool;

public class Main {

    public static String siteName = "https://prostovar.ru";
    //String siteName = "https://prostovar.ru https://skillbox.ru https://hr-vector.com ";

    public static void main(String[] args) throws IOException {

        Set<String> res = new ForkJoinPool().invoke(new SiteNodes(siteName));
        List<String> sortRes = new ArrayList<>();
        sortRes.addAll(res);
        Collections.sort(sortRes);

        FileWriter siteMap = new FileWriter("sitemap.txt");

        sortRes.forEach(l -> {
            long count = l.chars().filter(ch -> ch == '/').count();
            for(int i =2; i< count; i++){
                l = "    " + l;
            }
            try {
                siteMap.write(l + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        siteMap.close();

    }

    public static boolean isExternalLink(String url) {
        Boolean isExternalLink;
        if(url.contains(siteName) && !url.contains(":443")) isExternalLink = false;
        else isExternalLink = true;
        return isExternalLink;
    }

}
