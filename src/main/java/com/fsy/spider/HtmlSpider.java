package com.fsy.spider;


import com.fsy.spider.enums.HtmlSpiderOption;
import com.fsy.spider.util.FileUtil;
import com.github.axet.wget.WGet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fushiyong on 2018/3/7.
 */
public class HtmlSpider {

    private HtmlSpiderOption htmlSpiderOption ;


    private List<String> hasCrawedUrl ;

    private List<String> unCrawedjavascriptUrl ;

    //存储导出文件路径
    public static  String saveLocation="~";

    //主页
    private String homepage;

    //jsUrl
    private List<String> jsUrl;

    private List<String> cssUrl ;

    //包括主页, js文件地址, css文件地址
    private List<String> allUrls = new ArrayList<>();



    public HtmlSpider(String homepage ){





        this.homepage = homepage;

//        this.htmlSpiderOption = htmlSpiderOption;


        start0();

    }


    public HtmlSpider(String homepage ,  HtmlSpiderOption htmlSpiderOption){





       this.homepage = homepage;

       this.htmlSpiderOption = htmlSpiderOption;


        start0();

    }

    public void start0() {

        this.allUrls.add(homepage);

        this.allUrls.addAll(getScriptUrl(homepage));

        this.allUrls.addAll(getCSSUrl(homepage));

//        parseUrl();

        for(String waitCrawUrl : allUrls){
            //谷歌相关的不下载
            if(waitCrawUrl.contains("google")){
                continue;
            }
            String savePath = null;

            // 未处理a 标签
            if(waitCrawUrl.equals(homepage)){

                //首页
                savePath = saveLocation;

                String responseHtml = null;
                try {
                    responseHtml = Jsoup.parse( new URL(waitCrawUrl), 1000).toString();

                    //替换当前爬取网站的域名为当前域名
                    responseHtml.replaceAll(homepage,"");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                FileUtil.save(responseHtml , savePath+File.separator+"index.html");

            }else{

                //解决引用第三方js 比如 http://cdn.bootcss.com/jquery/1.11.1/jquery.min.js

                //首页相关JS , CSS
                if(waitCrawUrl.contains(homepage)){
                    String suffixUrl = waitCrawUrl.split(homepage)[1];

                    if(suffixUrl.indexOf("/") == 0){
                        suffixUrl = suffixUrl.substring(1);
                    }
                    savePath = saveLocation + File.separator + suffixUrl;

                    String responseHtml = null;
                    try {

                        //下载文件

                        responseHtml = WGet.getHtml( new URL(waitCrawUrl));

                        //替换当前爬取网站的域名为当前域名
                        responseHtml.replaceAll(homepage,"");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    FileUtil.save(responseHtml , savePath);

                }else{
                    savePath = saveLocation + File.separator + waitCrawUrl.substring(waitCrawUrl.lastIndexOf("/"));

                    String responseHtml = null;
                    try {

                        //下载文件

                        responseHtml = WGet.getHtml( new URL(waitCrawUrl));

                        //替换当前爬取网站的域名为当前域名
                        responseHtml.replaceAll(homepage,"");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    FileUtil.save(responseHtml , savePath);
                }





            }



        }





//        //不递归本页其他链接
//        if(this.htmlSpiderOption.isSingleOrRepeatPage()){
//            craw(url,false);
//        }else{
//            craw(url,true);
//        }
    }

    private void parseUrl() {
        Document document = Jsoup.parse( homepage, "1000");


    }

    //爬取方法

    /**
     *
     * @param url
     * @param isRecursive 是否递归本页爬取
     */
    private void craw(String url, boolean isRecursive) {

        if(isRecursive){

        }else{
            doCraw(url);
        }
    }

    private void doCraw(String url) {

        if(!hasCrawedUrl.contains(url)){
            //爬取
            Document doc = null;
            doc = Jsoup.parse( url, "1000");

            saveHomePage(doc);

//                saveScript(doc);
//
//                getCSSUrl(doc);


            List<Node> allNodes = doc.childNodes();


            Element content = doc.getElementById("content");


            Elements links = content.getElementsByTag("a");
            for (Element link : links) {
                String linkHref = link.attr("href");
                String linkText = link.text();
            }


        }
    }

    private List<String> getCSSUrl(String urlStr) {


        Document doc = null;
        try {
            doc = Jsoup.parse(  new URL(urlStr), 1000);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        List<String> cssUrls = new ArrayList<>();

        Elements cssEles = doc.getElementsByTag("link");

        cssEles.stream().forEach(element -> {

            if(element.tagName().equals("link") && element.hasAttr("href") ){

                if(element.attr("rel").equalsIgnoreCase("stylesheet")){
                    String url = element.attr("href");


                    if(url.startsWith("/")){
                        url = homepage+ url;
                    }


                    cssUrls.add(url);
                }

            }

        });

        return cssUrls;


    }

    private List<String> getScriptUrl(String urlStr) {


        Document doc = null;
        try {
            doc = Jsoup.parse( new URL(urlStr), 1000);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        List<String> scriptUrls = new ArrayList<>();

        Elements scriptEles = doc.getAllElements();

        scriptEles.forEach(element -> {

            //确定js文件url的标志
            if(element.tagName().equals("script") && element.hasAttr("src")){
                String url = element.attr("src");


                if(url.startsWith("/")){
                    url = homepage+ url;
                }

                scriptUrls.add(url);

            }



        });

        return scriptUrls;






    }

    private void saveHomePage(Document doc) {

        FileUtil.save(doc.toString(), saveLocation);
    }


}



