package com.example.demo.domain;

public class Todo {
	private Integer id;
	private String title;
	private Boolean done;

	public Integer getId(){return id;}
	public void setId(Integer id){this.id=id;}
	public String getTitle(){return title;}
	public void setTitle(String title){this.title=title;}
	public Boolean getDone(){return done;}
	public void setDone(Boolean done){this.done=done;}
}
