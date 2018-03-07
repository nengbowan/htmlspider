package com.fsy.spider.starter;

import com.fsy.spider.HtmlSpider;

import java.util.Scanner;

/**
 * Created by fushiyong on 2018/3/7.
 */
public class Starteer {
    public static void main(String [] args){

//        String url = "http://hack94sb.com/";

        ///Users/fushiyong/Desktop/huaidan


        System.out.println("please input what kind of website u want to craw~~~");


        Scanner sc = new Scanner(System.in);

        String url = sc.nextLine();

        System.out.println("please input location want  to save ~~~\n if you not specify , the default is user directory. ");

        HtmlSpider.saveLocation = sc.nextLine();

        new HtmlSpider(url).start0();
    }

}
