package nigam.yomarket.getset;

import java.io.Serializable;

/**
 * Created by alokit nigam on 5/14/2017.
 */

public class HomeListGetSet  implements Serializable{
//{"s_no":"14","post_product":"Fruit","post_city":"Bhopal","post_profession":"Wholeseller",
// "post_quantity":"1","post_price":"etwet","post_description":"7iytui","post_image_1":"1366x768 (1).jpg",
// "post_image_2":"1407134463MasteringOpenCV.gif",
// "post_image_3":"1366x768 (1).jpg","post_image_4":"1366x768 (1).jpg","post_id":"104"}
    private String s_no,post_product,post_city,post_profession,post_quantity,post_price,post_description,post_image_1,post_image_2,
        post_image_3,post_image_4,post_id,posted_by_id,posted_by_name,posted_by_phone,mobile_no,comment,commentby,date,time;


    public String getComment(){
        return comment;
    }

    public void setComment(String comment){
        this.comment = comment;
    }

    public String getCommentby(){
        return commentby;
    }

    public void setCommentby(String commentby){
        this.commentby = commentby;
    }

    public String getMobile_no(){
        return mobile_no;
    }

    public void setMobile_no(String mobile_no){
        this.mobile_no = mobile_no;
    }

    public void setPosted_by_id(String posted_by_id) {
        this.posted_by_id = posted_by_id;
    }

    public String getPosted_by_phone() {
        return posted_by_phone;
    }

    public void setPosted_by_phone(String posted_by_phone) {
        this.posted_by_phone = posted_by_phone;
    }

    public String getPosted_by_id() {
        return posted_by_id;
    }

    public void setposted_by_id(String posted_by) {
        this.posted_by_id = posted_by;
    }

    public String getPosted_by_name() {
        return posted_by_name;
    }

    public void setPosted_by_name(String posted_by_name) {
        this.posted_by_name = posted_by_name;
    }

    public String getS_no() {
        return s_no;
    }

    public void setS_no(String s_no) {
        this.s_no = s_no;
    }

    public String getPost_product() {
        return post_product;
    }

    public void setPost_product(String post_product) {
        this.post_product = post_product;
    }

    public String getPost_city() {
        return post_city;
    }

    public void setPost_city(String post_city) {
        this.post_city = post_city;
    }

    public String getPost_profession() {
        return post_profession;
    }

    public void setPost_profession(String post_profession) {
        this.post_profession = post_profession;
    }

    public String getPost_quantity() {
        return post_quantity;
    }

    public void setPost_quantity(String post_quantity) {
        this.post_quantity = post_quantity;
    }

    public String getPost_price() {
        return post_price;
    }

    public void setPost_price(String post_price) {
        this.post_price = post_price;
    }

    public String getPost_description() {
        return post_description;
    }

    public void setPost_description(String post_description) {
        this.post_description = post_description;
    }

    public String getPost_image_1() {
        return post_image_1;
    }

    public void setPost_image_1(String post_image_1) {
        this.post_image_1 = post_image_1;
    }

    public String getPost_image_2() {
        return post_image_2;
    }

    public void setPost_image_2(String post_image_2) {
        this.post_image_2 = post_image_2;
    }

    public String getPost_image_3() {
        return post_image_3;
    }

    public void setPost_image_3(String post_image_3) {
        this.post_image_3 = post_image_3;
    }

    public String getPost_image_4() {
        return post_image_4;
    }

    public void setPost_image_4(String post_image_4) {
        this.post_image_4 = post_image_4;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
