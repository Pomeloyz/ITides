package oms.pomelo.itides.daliy;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * NAME: Sherry
 * DATE: 2019-07-27
 */
public class DailyInfo {

    /**
     * id : brwvcn
     * author : 《初代吸血鬼》
     * content : Don't make promises you can't keep.
     * assign_date : 2019-07-27
     * origin_img_urls : ["https://media-image1.baydn.com/soup_pub_image/ccdbwr/ae3cff74a9de7c3d5748f9ade1807a19.793ab21d70b5d87b8031cad3a945f726.png@!fhd_webp","https://media-image1.qiniu.baydn.com/soup_pub_image/ccdbwr/ae3cff74a9de7c3d5748f9ade1807a19.793ab21d70b5d87b8031cad3a945f726.png?imageView2/2/w/1080/format/webp"]
     * translation : 守不住的承诺就别说。
     */

    @SerializedName("id")
    private String id;
    @SerializedName("author")
    private String author;
    @SerializedName("content")
    private String content;
    @SerializedName("assign_date")
    private String assign_date;
    @SerializedName("share_url")
    private String share_url;
    @SerializedName("translation")
    private String translation;
    @SerializedName("origin_img_urls")
    private List<String> origin_img_urls;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAssign_date() {
        return assign_date;
    }

    public void setAssign_date(String assign_date) {
        this.assign_date = assign_date;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public List<String> getOrigin_img_urls() {
        return origin_img_urls;
    }

    public void setOrigin_img_urls(List<String> origin_img_urls) {
        this.origin_img_urls = origin_img_urls;
    }


}