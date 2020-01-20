package oms.pomelo.itides.ui.daliy;

import com.google.gson.annotations.SerializedName;

/**
 * NAME: Sherry
 * DATE: 2019-07-27
 */
public class DailyInfo {

    private String pic_url;
    private String raw_pic;
    private String date;
    private String layout_template;
    private ContentBean content;
    private WatermarkBean watermark;
    private int like_count;
    private int updated_at;
    //---------错误信息---------
    private int code;
    private String msg;
    //-------------------------

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getRaw_pic() {
        return raw_pic;
    }

    public void setRaw_pic(String raw_pic) {
        this.raw_pic = raw_pic;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLayout_template() {
        return layout_template;
    }

    public void setLayout_template(String layout_template) {
        this.layout_template = layout_template;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public WatermarkBean getWatermark() {
        return watermark;
    }

    public void setWatermark(WatermarkBean watermark) {
        this.watermark = watermark;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public int getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(int updated_at) {
        this.updated_at = updated_at;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class ContentBean {

        private ContentDetailBean en;
        @SerializedName("zh-Hant")
        private ContentDetailBean zhHant;
        @SerializedName("zh-Hans")
        private ContentDetailBean zhHans;

        public ContentDetailBean getEn() {
            return en;
        }

        public void setEn(ContentDetailBean en) {
            this.en = en;
        }

        public ContentDetailBean getZhHant() {
            return zhHant;
        }

        public void setZhHant(ContentDetailBean zhHant) {
            this.zhHant = zhHant;
        }

        public ContentDetailBean getZhHans() {
            return zhHans;
        }

        public void setZhHans(ContentDetailBean zhHans) {
            this.zhHans = zhHans;
        }
    }

    public static class WatermarkBean {
        private String url;
        private int width;
        private int height;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }

    public static class ContentDetailBean {
        private TextBean author;
        private TextBean author_title;
        private TextBean quote;
        private TextBean festival;

        public TextBean getAuthor() {
            return author;
        }

        public void setAuthor(TextBean author) {
            this.author = author;
        }

        public TextBean getAuthor_title() {
            return author_title;
        }

        public void setAuthor_title(TextBean author_title) {
            this.author_title = author_title;
        }

        public TextBean getQuote() {
            return quote;
        }

        public void setQuote(TextBean quote) {
            this.quote = quote;
        }

        public TextBean getFestival() {
            return festival;
        }

        public void setFestival(TextBean festival) {
            this.festival = festival;
        }
    }

    public static class TextBean {
        private String text;
        private int font_size;
        private String font_name;
        private int line_spacing;
        private int kern;
        private String color;
        private String background_color;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getFont_size() {
            return font_size;
        }

        public void setFont_size(int font_size) {
            this.font_size = font_size;
        }

        public String getFont_name() {
            return font_name;
        }

        public void setFont_name(String font_name) {
            this.font_name = font_name;
        }

        public int getLine_spacing() {
            return line_spacing;
        }

        public void setLine_spacing(int line_spacing) {
            this.line_spacing = line_spacing;
        }

        public int getKern() {
            return kern;
        }

        public void setKern(int kern) {
            this.kern = kern;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getBackground_color() {
            return background_color;
        }

        public void setBackground_color(String background_color) {
            this.background_color = background_color;
        }
    }
}