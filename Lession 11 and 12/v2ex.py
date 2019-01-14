#!/usr/bin/env python
# -*- encoding: utf-8 -*-
# Created on 2016-08-17 11:11:46
# Project: v2ex

from pyspider.libs.base_handler import *
import re
import random
import datetime
# import MySQLdb
import mysql.connector

class Handler(BaseHandler):
    crawl_config = {
    }
    
    def __init__(self):
        self.db = mysql.connector.connect(host = "localhost", user = "root", password = "zxz9410950401", database = "wenda",auth_plugin = 'mysql_native_password')
    
    def add_question(self, title, content):
        try:
            cursor = self.db.cursor()
            sql = 'insert into question (title, content, user_id, created_date, comment_count) values ("%s","%s",%d, "%s", 0)' % (title, content, random.randint(1, 10),datetime.datetime.now())
            #print (sql)
            cursor.execute(sql)
            self.db.commit()
        except Exception, e:
            print e
            self.db.rollback()
        
    
    # 进入的首页
    @every(minutes=24 * 60)
    def on_start(self):
        self.crawl('http://v2ex.com', callback=self.index_page)

    # 第一层的论坛板块分类 
    @config(age=10 * 24 * 60 * 60)
    def index_page(self, response):
        for each in response.doc('a[href^="http://v2ex.com/?tab="]').items():
            self.crawl(each.attr.href, callback=self.tab_page)

    #次级的板块
    @config(age=10 * 24 * 60 * 60)
    def tab_page(self, response):
        for each in response.doc('a[href^="http://v2ex.com/go/"]').items():
            self.crawl(each.attr.href, callback=self.board_page)
    
    #具体的论坛与帖子（子版块）
    @config(priority=2)
    def board_page(self, response):
        for each in response.doc('a[href^="http://v2ex.com/t/"]').items():
            url = each.attr.href
            if url.find('#reply') > 0:
                url = url[0:url.find('#')]
            self.crawl(url, callback=self.detail_page)

        # 子版块的翻页，调用自己
        for each in response.doc('a.page_normal').items():
            self.crawl(each.attr.href, callback=self.board_page)

    # 在detail_page开始解析具体的帖子
    @config(priority=20)
    def detail_page(self, response):
        title = response.doc('h1').text()
        # 内容中的引号替换，否则引起数据库的冲突
        content = response.doc('div.topic_content').html().replace('"','\\"')
        # 插入数据库
        self.add_question(title, content)
        return {
            'url': response.url,
            'title': title,
            'content': content
        }
