package deliveryapp.saalventure.madrasmarket.util;

public interface TodayOrderClickListner {

    void callAction(String number);
    void orderDetailsClick(String viewType,int position);
    void orderConfirm(String viewType,int position);
}
