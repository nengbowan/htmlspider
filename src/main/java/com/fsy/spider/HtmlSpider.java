package com.fsy.spider;


import com.fsy.spider.enums.HtmlSpiderOption;
import com.fsy.spider.util.FileUtil;
import com.github.axet.wget.WGet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fushiyong on 2018/3/7.
 */
public class HtmlSpider {

    //已经爬取的网页记录
    private HashMap<String,String> hasCrawedUrlMap = new HashMap() ;

    //当前工作空间, 用于存储文件主目录
    private String currentWorkDir = HtmlSpiderOption.SAVEDIR ;

    public void start0(){
        doCraw(HtmlSpiderOption.HOMEPAGE , HtmlSpiderOption.ISITERATOR);
    }

    /**
     * 封装wget 下载文件
     * @param waitCrawUrl
     * @param path
     */
    private void wget(String waitCrawUrl, String path) {

        if(!StringUtils.isEmpty(waitCrawUrl) && !StringUtils.isEmpty(path) ){


            try {
                if(isAbsolutePath(waitCrawUrl)){
                    String responseStr = WGet.getHtml(new URL(waitCrawUrl));

                    FileUtil.save(responseStr,  path );

                    recordCrwaedUrlMap(waitCrawUrl);
                }else{
                    // ../ ../../ /
                    String responseStr = WGet.getHtml(new URL(HtmlSpiderOption.HOMEPAGE + "/"+ waitCrawUrl));

                    FileUtil.save(responseStr,  path );

                    recordCrwaedUrlMap(waitCrawUrl);


                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

    }
    private boolean isAbsolutePath(String waitCrawUrl) {

        return waitCrawUrl.startsWith("http://") || waitCrawUrl.startsWith("https://");
    }

    private boolean isGoogleResource(String waitCrawUrl) {

        return waitCrawUrl.contains("google.com");
    }




    /**
     * 爬取一个网页css , js , html  以及 是否递归爬取
     * @param url
     */
    private void doCraw(String url , boolean isIterator) {

        if(isRepeatSpider(url)){
            return;
        }
        //css 文件
        List<String> cssUrl = getCSSUrl(url);

        //js脚本
        List<String> scriptUrl = getScriptUrl(url);



        downCurrentUrl(url);

        doCssUrl(cssUrl);

        doScriptUrl(scriptUrl);


        if(isIterator){
            //a标签
            List<String> aUrl = getAUrl(url);

            doAUrl(aUrl, isIterator);
        }






    }

    /**
     *
     * homepage = "http://www.baidu.com" url = "http://www.baidu.com/hello/1/hello2"  specialSaveLocation = "/tmp/download"
     * 根据请求网址 , 指定的存储路径 来确定本地存储的路径
     * @param url  当前请求url
     * @param specialSaveLocation 指定存储工作空间
     * @return
     */
    public String parseSaveLocation (String url , String specialSaveLocation){
        if(!isAbsolutePath(url)){

            // ../ ../../ /
            if(url.startsWith("/")){
                return specialSaveLocation + "/" + url+ "/" +"index.html" ;
            }else if(url.startsWith("../")){
                return specialSaveLocation + "/parent1/" + url + "/"+"index.html" ;
            }else if(url.startsWith("../../")){
                return specialSaveLocation + "/parent2/" + url + "/"+"index.html" ;
            }


        }else {
            if(url.equalsIgnoreCase(HtmlSpiderOption.HOMEPAGE)){
                return specialSaveLocation + "/" +"index.html" ;
            }
        }
        return null;
    }
    private void downCurrentUrl(String currentUrl) {


        if( isRepeatSpider(currentUrl)){
            return;
        }else{


            String responseHtml = null;
            try {
                responseHtml = Jsoup.parse( new URL(currentUrl), 1000).toString();

//            //替换当前爬取网站的域名为当前域名
//            responseHtml.replaceAll(homepage,"");
            } catch (IOException e) {
                e.printStackTrace();
            }


            String saveFilePath = null;


            saveFilePath = parseSaveLocation(currentUrl , HtmlSpiderOption.SAVEDIR);

            FileUtil.save(responseHtml , saveFilePath != null ?saveFilePath  : HtmlSpiderOption.SAVEDIR );

            recordCrwaedUrlMap(currentUrl);
        }

    }


    private void doAUrl(List<String> aUrl , boolean isIterator) {
        aUrl.stream().forEach(aUrlStr -> {
            doCraw(aUrlStr,isIterator );
        });

    }

    private void doScriptUrl(List<String> scriptUrl) {

        for(String scriptUrlStr : scriptUrl){
            if( isRepeatSpider(scriptUrlStr)){
                continue;
            }else{
                //已爬取的网页在以后不再继续爬取
                wget(scriptUrlStr,parseSaveLocationScript(scriptUrlStr));

            }
        }

    }

    private String parseSaveLocationScript(String scriptUrlStr) {

        if(!isAbsolutePath(scriptUrlStr)){

            // ../ ../../ /
            if(scriptUrlStr.startsWith("/")){
                return HtmlSpiderOption.SAVEDIR + "/" +  scriptUrlStr;
            }else if(scriptUrlStr.startsWith("../../")){
                scriptUrlStr = scriptUrlStr.replaceFirst("\\.\\./\\.\\./","");
                return HtmlSpiderOption.SAVEDIR + "/parentScript1/" + scriptUrlStr  ;
            }else if(scriptUrlStr.startsWith("../")){
                scriptUrlStr = scriptUrlStr.replaceFirst("\\.\\./","");
                return HtmlSpiderOption.SAVEDIR + "/parentScript2/" + scriptUrlStr  ;
            }


        }else{
            if(scriptUrlStr.startsWith(HtmlSpiderOption.HOMEPAGE)){
                return HtmlSpiderOption.SAVEDIR + "/"+ scriptUrlStr.split(HtmlSpiderOption.HOMEPAGE)[1]  ;
            }
        }
        return null;
    }

    private void doCssUrl(List<String> cssUrl) {


        for(String cssUrlStr : cssUrl){
            if( isRepeatSpider(cssUrlStr)){
                continue;
            }else{
                //已爬取的网页在以后不再继续爬取
                wget(cssUrlStr,parseSaveLocationCSS(cssUrlStr ));


            }
        }
    }

    private String parseSaveLocationCSS(String cssUrlStr) {

        if(!isAbsolutePath(cssUrlStr)){

            // ../ ../../ /
            if(cssUrlStr.startsWith("/")){
                return HtmlSpiderOption.SAVEDIR + "/" +  cssUrlStr;
            }else if(cssUrlStr.startsWith("../../")){

                cssUrlStr = cssUrlStr.replaceFirst("\\.\\./\\.\\./","");
                return HtmlSpiderOption.SAVEDIR + "/parentCss2/" + cssUrlStr  ;
            }else if(cssUrlStr.startsWith("../")){

                cssUrlStr = cssUrlStr.replaceFirst("\\.\\./","");

                return HtmlSpiderOption.SAVEDIR + "/parentCss1/" + cssUrlStr  ;
            }else{
                return HtmlSpiderOption.SAVEDIR  +"/"+  cssUrlStr  ;
            }


        }else{
            if(cssUrlStr.startsWith(HtmlSpiderOption.HOMEPAGE)){
                return HtmlSpiderOption.SAVEDIR + "/"+ cssUrlStr.split(HtmlSpiderOption.HOMEPAGE)[1]  ;
            }
        }
        return null;
    }

    private void recordCrwaedUrlMap(String cssUrlStr) {
        this.hasCrawedUrlMap.put(cssUrlStr,cssUrlStr);
    }

    private boolean isRepeatSpider(String cssUrlStr) {

        return this.hasCrawedUrlMap.get(cssUrlStr ) != null;
    }

    private List<String> getAUrl(String url) {

        //爬取
        Document doc = null;
        try {
            doc = Jsoup.parse( new URL(url), 1000);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        List<Node> allNodes = doc.childNodes();


        Element content = doc.getElementById("body");


        Elements links = content.getElementsByTag("a");

        List<String> aUrls = new ArrayList<>();

        for (Element link : links) {
            String linkHref = link.attr("href");
            String linkText = link.text();

            aUrls.add(linkHref);
        }
        return aUrls;
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

                    cssUrls.add(url);
                }

            }

        });

        return cssUrls;


    }


    private List<String> getScriptUrl(String urlStr ) {


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

                scriptUrls.add(url);

            }

        });

        return scriptUrls;

    }

}



