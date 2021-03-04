
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class SiteNodes extends RecursiveTask<Set<String>> {

    private String url;

    public static Set<String> urlList = new LinkedHashSet<>();

    public SiteNodes(String url) {
        this.url = url;
    }

    @Override
    protected synchronized Set<String> compute() {
        urlList.add(url);
        Set<SiteNodes> taskList = new LinkedHashSet<>();
        Set<String> nodes = getNodes(url);

        if (!nodes.isEmpty()) {
            for (String node : nodes) {
                SiteNodes task = new SiteNodes(node);
                taskList.add(task);
                task.fork();
                try {
                    Thread.sleep(120);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            for (SiteNodes task : taskList) {
                task.join();
            }
        }
        return urlList;
    }

    public static Set<String> getNodes(String node) {

        Set<String> siteNodes = new LinkedHashSet<>();
        try {
            Document html = Jsoup.connect(node).ignoreContentType(true).ignoreHttpErrors(true).get();
            Elements links = html.select("a");
            links.forEach(element -> {
                String link = element.attr("abs:href");
                if(link.endsWith("/")) link = link.substring(0, link.length() -1);
                if (!repeat(link) & !Main.isExternalLink(link)) {
                    siteNodes.add(link);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        return siteNodes;
    }

    protected static boolean repeat(String url) {
        boolean isContains;
        if (urlList.contains(url)) isContains = true;
        else isContains = false;

        return isContains;
    }

}
