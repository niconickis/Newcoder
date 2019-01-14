#!/usr/bin/python
# -*- encoding: utf-8 -*-
import mysql.connector
import datetime
import random
def main():
    try:
        cnx = mysql.connector.Connect(host = "localhost", user = "root", password = "zxz9410950401", database = "wenda",auth_plugin = 'mysql_native_password',)
        cursor = cnx.cursor()
        addQuestionSql ='insert into question (title, content, user_id, created_date, comment_count) values ("%s", "%s", %d, "%s", %d)' % ('自己插入的问题', 'balabala', random.randint(1, 10) , datetime.datetime.now(), 98)
        addCommentSql = 'insert into comment (content, entity_type, entity_id, user_id, created_date) values ("%s", %d, %d, %d, "%s")' %('自己插入的评论', 1, 8, random.randint(1, 10), datetime.datetime.now())
        cursor.execute(addQuestionSql)
        cursor.execute(addCommentSql)
        cnx.commit()
        qid = cursor.lastrowid
    except Exception as e :
        print e
        cnx.rollback()

if __name__ == "__main__":
    main()