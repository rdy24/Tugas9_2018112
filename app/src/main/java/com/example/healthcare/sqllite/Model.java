package com.example.healthcare.sqllite;

public class Model {
  private int id;
  private byte[]avatar;
  private String title;
  private String datePublish;

  public Model(int id, byte[] avatar, String title, String datePublish) {
    this.id = id;
    this.avatar = avatar;
    this.title = title;
    this.datePublish = datePublish;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public byte[] getAvatar() {
    return avatar;
  }

  public void setAvatar(byte[] avatar) {
    this.avatar = avatar;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDatePublish() {
    return datePublish;
  }

  public void setDatePublish(String datePublish) {
    this.datePublish = datePublish;
  }
}
