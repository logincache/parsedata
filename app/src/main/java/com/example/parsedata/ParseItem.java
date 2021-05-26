package com.example.parsedata;
public class  ParseItem {

    private String imgUrl;//картинка
    private String title;//Название обьявления
    private String detailUrl;//ссылка на обьявление
    private String price;//Цена
    //++ добавить дату публикации обьявления

    public ParseItem() {
    }

    public ParseItem(String imgUrl, String title,String price, String detailUrl) {
        this.imgUrl = imgUrl;
        this.title = title;
        this.price = price;
        this.detailUrl = detailUrl;
    }

    public String getImgUrl() {
        if (imgUrl.isEmpty()) {
          imgUrl = "https://www.vivicetona.it/wp-content/uploads/2017/05/noImg_2-1.jpg";//
        }else {
            return imgUrl;
        }

        return null;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {return price;}

    public void setPrice(String price) {this.price = price;}

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }
}