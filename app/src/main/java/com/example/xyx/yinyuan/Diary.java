package com.example.xyx.yinyuan;


public class Diary {
    private Integer id;
    private String title;
    private String content;
    private String pubdate;

    public Diary(String title, String content, String pubdate) {
        super();
        this.title = title;
        this.content = content;
        this.pubdate = pubdate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

}

