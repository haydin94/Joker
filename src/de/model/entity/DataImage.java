package de.model.entity;

import java.io.File;

/**
 *
 * @author aydins
 */
public class DataImage {
    int image_id;
    int user_id;
    String date;
    String pathNorm;
    String pathThumb;
    boolean active;
    File img;

    public DataImage() {
    }

    public DataImage(int image_id, int user_id, String date, String pathNorm, String pathThumb, boolean active) {
        this.image_id = image_id;
        this.user_id = user_id;
        this.date = date;
        this.pathNorm = pathNorm;
        this.pathThumb = pathThumb;
        this.active = active;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPathNorm() {
        return pathNorm;
    }

    public void setPathNorm(String pathNorm) {
        this.pathNorm = pathNorm;
    }

    public String getPathThumb() {
        return pathThumb;
    }

    public void setPathThumb(String pathThumb) {
        this.pathThumb = pathThumb;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public File getImg() {
        return img;
    }

    public void setImg(File img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "DataImage{" + "image_id=" + image_id + ", user_id=" + user_id + ", date=" + date + ", pathNorm=" + pathNorm + ", pathThumb=" + pathThumb + ", active=" + active + ", img=" + img + '}';
    }
    
    
    
    
}
