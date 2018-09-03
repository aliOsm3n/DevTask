package com.example.aliothman.devtask;

public class Model_Repo {
    private  int id ;
    private  String repo_name ;
    private  String descreptoion ;
    private  String owner_name ;
    private  String url_repo ;
    private  String url_owner ;
    private  String fork;

    public int getId() {
        return id;
    }

    public String getRepo_name() {
        return repo_name;
    }

    public String getDescreptoion() {
        return descreptoion;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public String getUrl_repo() {
        return url_repo;
    }

    public String getUrl_owner() {
        return url_owner;
    }

    public String getFork() {
        return fork;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRepo_name(String repo_name) {
        this.repo_name = repo_name;
    }

    public Model_Repo(int id, String repo_name, String descreptoion, String owner_name, String url_repo, String url_owner, String fork) {
        this.id = id;
        this.repo_name = repo_name;
        this.descreptoion = descreptoion;
        this.owner_name = owner_name;
        this.url_repo = url_repo;
        this.url_owner = url_owner;
        this.fork = fork;
    }
}
