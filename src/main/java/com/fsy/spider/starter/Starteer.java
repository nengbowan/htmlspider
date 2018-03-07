package com.fsy.spider.starter;

import com.fsy.spider.HtmlSpider;

/**
 * Created by fushiyong on 2018/3/7.
 */
public class Starteer {
    public static void main(String [] args){

        String url = "http://www.yinwang.org/";

        HtmlSpider.saveLocation = "/Users/fushiyong/yinwang";

        //


//        System.out.println("please input what kind of website u want to craw~~~");
//
//
//        Scanner sc = new Scanner(System.in);
//
//        String url = sc.nextLine();
//
//        System.out.println("please input location want  to save ~~~\n if you not specify , the default is user directory. ");
//
//        HtmlSpider.saveLocation = sc.nextLine();

        new HtmlSpider(url).start0();
    }

}
