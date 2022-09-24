数据库

```sql
question表添加一个字段 难度   level
question表添加一个字段 原题链接   link
contest表添加一个字段 比赛名称  contestName
新建表comment评论表
friendLink表添加一个字段  简介  introduce
新建表blog博客表
新建表blog_comment博客评论表
新建表blog_class博客分类表

question表添加索引
alter table question
    add fulltext ftk_question_name (question_name) with parser ngram;
alter table question
    add fulltext ftk_question_from (question_from) with parser ngram;
alter table question
    add fulltext ftk_author (author) with parser ngram;
    
    
message_board表添加索引
alter table message_board add fulltext ftk_message(message) with parser ngram;

friend_link表添加索引
alter table friend_link add fulltext ftk_link_name(link_name) with parser ngram;
```

创建blog文件夹

