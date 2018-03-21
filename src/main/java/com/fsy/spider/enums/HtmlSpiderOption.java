package com.fsy.spider.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by fushiyong on 2018/3/7.
 */
@Getter
@Setter
public class HtmlSpiderOption {



//    //爬取线程数目
//    private int threadNum ;
//
//    //是否纵深递进爬取页面
//    private boolean singleOrRepeatPage ;
//
//    //爬取白名单
//    private List<String> whiteUrlList ;
//
//    //爬取黑名单
//    private List<String> blackUrlList ;

    //是否递归爬取子网页
    public static boolean ISITERATOR = false ;

//    //爬取的主页
//    public static String HOMEPAGE  = "http://www.hack94sb.com/" ;
//
//    //保存文件路径
//    public static String SAVEDIR ="/tmp/huaidan";

    //爬取的主页
    public static String HOMEPAGE  = "http://www.hack94sb.com/" ;

    //保存文件路径
    public static String SAVEDIR ="/tmp/huaidan";



}
