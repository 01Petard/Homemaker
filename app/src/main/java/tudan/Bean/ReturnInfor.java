package tudan.Bean;

import android.util.SparseArray;

import tudan.Item.GoodsItem;


/**
 * Created by fengliang
 * on 2017/7/14.
 */

public class ReturnInfor {
    private int oid;
    private double totalPrice;
    private String title;
    private User user;
    private User courier;

    private SparseArray<GoodsItem> goodsItemSparseArray;
    private String address;
    private double l;
    private double d;
    private String delivery_time;
    private String serialNumber;
    private String content;
    private String logisticsStatus;
    private String orderStatus;
    public User getCourier() {
        return courier;
    }

    public void setCourier(User courier) {
        this.courier = courier;
    }


    public String getLogisticsStatus() {
        return logisticsStatus;
    }

    public void setLogisticsStatus(String logisticsStatus) {
        this.logisticsStatus = logisticsStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
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

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public ReturnInfor(){

    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SparseArray<GoodsItem> getGoodsItemSparseArray() {
        return goodsItemSparseArray;
    }

    public void setGoodsItemSparseArray(SparseArray<GoodsItem> goodsItemSparseArray) {
        this.goodsItemSparseArray = goodsItemSparseArray;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getL() {
        return l;
    }

    public void setL(double l) {
        this.l = l;
    }

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }
}
