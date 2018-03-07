package com.fsy.spider.enums;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by fushiyong on 2018/3/7.
 */
@Getter
@Setter
public class HtmlSpiderOption {


    //爬取线程数目
    private int threadNum ;

    //是否纵深递进爬取页面
    private boolean singleOrRepeatPage ;

    //爬取白名单
    private List<String> whiteUrlList ;

    //爬取黑名单
    private List<String> blackUrlList ;


    public HtmlSpiderOption(){
        this(20 , false);
    }
    public HtmlSpiderOption( int threadNum , boolean singleOrRepeatPage){
        this.threadNum = threadNum;
        this.singleOrRepeatPage = singleOrRepeatPage;
    }
}
