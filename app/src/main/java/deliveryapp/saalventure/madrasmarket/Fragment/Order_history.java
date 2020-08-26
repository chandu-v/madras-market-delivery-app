package deliveryapp.saalventure.madrasmarket.Fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import deliveryapp.saalventure.madrasmarket.Adapter.Delivered_historyAdapter;
import deliveryapp.saalventure.madrasmarket.Adapter.Delivered_monthly_adapter;
import deliveryapp.saalventure.madrasmarket.Adapter.Delivered_weekly_adapter;
import deliveryapp.saalventure.madrasmarket.AppController;
import deliveryapp.saalventure.madrasmarket.Config.BaseURL;
import deliveryapp.saalventure.madrasmarket.Model.Daily_model;
import deliveryapp.saalventure.madrasmarket.Model.Monthly_model;
import deliveryapp.saalventure.madrasmarket.Model.My_order_model;
import deliveryapp.saalventure.madrasmarket.Model.Weekly_model;
import deliveryapp.saalventure.madrasmarket.R;
import deliveryapp.saalventure.madrasmarket.util.CustomVolleyJsonArrayRequest;
import deliveryapp.saalventure.madrasmarket.util.Session_management;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class Order_history extends Fragment {

    RecyclerView rc_today, rc_weekly, rc_monthlky;
    List<Weekly_model> weekly_modelist = new ArrayList<>();
    List<Monthly_model> monthly_modelList = new ArrayList<>();
    Delivered_weekly_adapter delivered_weekly_adapter;
    Session_management sessionManagement;
    String get_id;
    String currentdate;
    LinearLayout daily_layout, weekly_layout, monthly_layout;
    Delivered_monthly_adapter delivered_monthly_adapter;
    Delivered_historyAdapter delivered_dailyAdapter;
    private ProgressDialog progressDialog;
    private List<Daily_model> daily_modellist = new ArrayList<>();

    public Order_history() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);
        progressDialog = new ProgressDialog(container.getContext());
        progressDialog.setMessage("Please wait while we fetching your Deliverd Orders History..");
        progressDialog.setCancelable(false);
        rc_today = view.findViewById(R.id.rc_daily);

        rc_weekly = view.findViewById(R.id.rc_weekly);

        rc_monthlky = view.findViewById(R.id.rc_monthly);

        daily_layout = view.findViewById(R.id.daily_layout);
        monthly_layout = view.findViewById(R.id.monthly_layout);
        weekly_layout = view.findViewById(R.id.weekly_layout);

        sessionManagement = new Session_management(getActivity());
        get_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
        rc_today.setLayoutManager(new LinearLayoutManager(getActivity()));

        rc_weekly.setLayoutManager(new LinearLayoutManager(getActivity()));

        rc_monthlky.setLayoutManager(new LinearLayoutManager(getActivity()));

        getorder_history();

        // weekly_history();

        // monthly_history();

        return view;
    }

    private void getorder_history() {
        progressDialog.show();
        String tag_json_obj = "json_socity_req";
        HashMap<String, String> param = new HashMap<>();
        param.put("dboy_id", get_id);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                "https://madrasmarketplaceapi.azurewebsites.net/order_master/getAllOrdersByStatusId/2/"+get_id,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.length()==0) {
                                daily_layout.setVisibility(View.GONE);
//                                Toast.makeText(requireActivity(), "No Today's Order Found!", Toast.LENGTH_SHORT).show();
                            } else {
                                for(int i = 0 ; i < response.length() ; i++){
                                    JSONArray jsonObjects = response.getJSONArray(i);
                                    String saleid = jsonObjects.get(0).toString();
                                    String placedon = jsonObjects.get(11).toString().split("T")[0];
//                                    String timefrom = obj.getString("time_slot");
                                    String timefrom = jsonObjects.get(11).toString().split("T")[1].split("\\+")[0];
                                    //  String timeto=obj.getString("delivery_time_from");
                                    //String item = obj.getString("total_items");
                                    String ammount = jsonObjects.get(3).toString();
                                    String status = jsonObjects.get(2).toString();
                                    String user_address = jsonObjects.get(4).toString()+","+jsonObjects.get(5).toString()+","+jsonObjects.get(6).toString();
//                                    String store_Address = obj.getString("store_address");
                                    //  String house = obj.getString("house_no");
                                    String rename = jsonObjects.get(8).toString();
                                    String renumber = jsonObjects.get(7).toString();
                                    String store_name = "";
                                    String total_items = jsonObjects.get(12).toString()==null?"0":jsonObjects.get(12).toString();
//                                    String store_lat = obj.getString("store_lat");
//                                    String store_lng = obj.getString("store_lng");
//                                    String user_lat = obj.getString("user_lat");
//                                    String user_lng = obj.getString("user_lng");
//                                    String dboy_lat = obj.getString("dboy_lat");
//                                    String dboy_lng = obj.getString("dboy_lng");
                                    Daily_model my_order_model = new Daily_model();

//                                    My_order_model my_order_model = new My_order_model();
                                    // my_order_model.setSocityname(society);
                                    my_order_model.setHouse(user_address);
                                    my_order_model.setRecivername(rename);
                                    my_order_model.setRecivermobile(renumber);
                                    my_order_model.setDelivery_time_from(timefrom);
                                    my_order_model.setSale_id(saleid);
                                    my_order_model.setOn_date(placedon);
                                    my_order_model.setTotal_amount(ammount);
                                    my_order_model.setStatus(status);
                                    my_order_model.setStore_name(store_name);
                                    my_order_model.setTotal_items(total_items);
//                                    my_order_model.setLat(store_lat);
//                                    my_order_model.setLng(store_lng);
//                                    my_order_model.setUserLat(user_lat);
//                                    my_order_model.setUserLong(user_lng);
//                                    my_order_model.setDbLat(dboy_lat);
//                                    my_order_model.setDbuserLong(dboy_lng);
                                    //   my_order_model.setTotal_items(item);
                                    // my_order_model.setDelivery_time_to(timefrom);
                                    daily_modellist.add(my_order_model);

                                    delivered_dailyAdapter = new Delivered_historyAdapter(daily_modellist);

                                    rc_today.setAdapter(delivered_dailyAdapter);
                                    delivered_dailyAdapter.notifyDataSetChanged();
                                }


                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }finally {
                            progressDialog.dismiss();
//                            refresh_layout.setRefreshing(false);
                        }
                    }
                }, error -> {
            error.printStackTrace();
        });
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.getCache().clear();
        jsonArrayRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 2;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        requestQueue.add(jsonArrayRequest);
        Map<String, String> params = new HashMap<String, String>();

        params.put("dboy_id", get_id);
        // params.put("filter","1");

        CustomVolleyJsonArrayRequest jsonObjReq = new CustomVolleyJsonArrayRequest(Request.Method.POST,

                BaseURL.historyOrders, params, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d("fghjk", response.toString());
                try {

                    for (int i = 0; i < response.length(); i++) {

                        JSONObject obj = response.getJSONObject(i);

                        String saleid = obj.getString("cart_id");
                        String placedon = obj.getString("delivery_date");
                        String timefrom = obj.getString("time_slot");
                        // String timeto = obj.getString("delivery_time_to");
                        // String item = obj.getString("total_items");
                        String ammount = obj.getString("remaining_price");
                        String status = obj.getString("order_status");
                        String lat = obj.getString("user_lat");
                        String lng = obj.getString("user_lng");
                        String house = obj.getString("user_address");
                        String rename = obj.getString("user_name");
                        String renumber = obj.getString("user_phone");
                        String storename = obj.getString("store_name");
                        Daily_model my_order_model = new Daily_model();
                        my_order_model.setStore_name(storename);
                        my_order_model.setLat(lat);
                        my_order_model.setLng(lng);
                        my_order_model.setHouse(house);
                        my_order_model.setRecivername(rename);
                        my_order_model.setRecivermobile(renumber);
                        my_order_model.setDelivery_time_from(timefrom);
                        my_order_model.setSale_id(saleid);
                        my_order_model.setOn_date(placedon);
                        // my_order_model.setDelivery_time_to(timeto);
                        my_order_model.setTotal_amount(ammount);
                        my_order_model.setStatus(status);
                        // my_order_model.setTotal_items(item);

                        daily_modellist.add(my_order_model);

                        delivered_dailyAdapter = new Delivered_historyAdapter(daily_modellist);

                        rc_today.setAdapter(delivered_dailyAdapter);
                        delivered_dailyAdapter.notifyDataSetChanged();


                    }

                    if (daily_modellist.isEmpty()) {
                        daily_layout.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    progressDialog.dismiss();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("dboy_id", get_id);
                return param;
            }
        };
//        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//        requestQueue.getCache().clear();
//        requestQueue.add(jsonObjReq);

    }

    private void weekly_history() {

        String tag_json_obj = "json_socity_req";

        Map<String, String> params = new HashMap<String, String>();

        params.put("d_id", get_id);
        //  params.put("filter","2");

        CustomVolleyJsonArrayRequest jsonObjReq = new CustomVolleyJsonArrayRequest(Request.Method.POST,

                BaseURL.todayOrder, params, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                try {


                    for (int i = 0; i < response.length(); i++) {

                        JSONObject obj = response.getJSONObject(i);

                        String saleid = obj.getString("sale_id");

                        String placedon = obj.getString("on_date");

                        String timefrom = obj.getString("delivery_time_from");

                        String timeto = obj.getString("delivery_time_to");

                        String item = obj.getString("total_items");

                        String ammount = obj.getString("total_amount");

                        String status = obj.getString("status");


                        String lat = obj.getString("lat");

                        String lng = obj.getString("lng");

                        String house = obj.getString("house_no");

                        String rename = obj.getString("receiver_name");

                        String renumber = obj.getString("receiver_mobile");
                        String storename = obj.getString("store_name");


                        Weekly_model my_order_model = new Weekly_model();
                        my_order_model.setStore_name(storename);
                        my_order_model.setLat(lat);
                        my_order_model.setLng(lng);
                        my_order_model.setHouse(house);
                        my_order_model.setRecivername(rename);
                        my_order_model.setRecivermobile(renumber);
                        my_order_model.setDelivery_time_from(timefrom);
                        my_order_model.setSale_id(saleid);
                        my_order_model.setOn_date(placedon);
                        my_order_model.setDelivery_time_to(timeto);
                        my_order_model.setTotal_amount(ammount);
                        my_order_model.setStatus(status);
                        my_order_model.setTotal_items(item);

                        weekly_modelist.add(my_order_model);

                        delivered_weekly_adapter = new Delivered_weekly_adapter(weekly_modelist);

                        rc_weekly.setAdapter(delivered_weekly_adapter);

                        delivered_weekly_adapter.notifyDataSetChanged();


                    }
                    if (weekly_modelist.isEmpty()) {
                        weekly_layout.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    private void monthly_history() {

        String tag_json_obj = "json_socity_req";

        Map<String, String> params = new HashMap<String, String>();

        params.put("d_id", get_id);
        // params.put("filter","3");

        CustomVolleyJsonArrayRequest jsonObjReq = new CustomVolleyJsonArrayRequest(Request.Method.POST,

                BaseURL.todayOrder, params, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                try {


                    for (int i = 0; i < response.length(); i++) {

                        JSONObject obj = response.getJSONObject(i);

                        String saleid = obj.getString("sale_id");

                        String placedon = obj.getString("on_date");

                        String timefrom = obj.getString("delivery_time_from");

                        String timeto = obj.getString("delivery_time_to");

                        String item = obj.getString("total_items");

                        String ammount = obj.getString("total_amount");

                        String status = obj.getString("status");


                        String lat = obj.getString("lat");

                        String lng = obj.getString("lng");

                        String house = obj.getString("house_no");

                        String rename = obj.getString("receiver_name");

                        String renumber = obj.getString("receiver_mobile");

                        String storename = obj.getString("store_name");

                        Monthly_model my_order_model = new Monthly_model();
                        my_order_model.setStore_name(storename);
                        my_order_model.setLat(lat);
                        my_order_model.setLng(lng);
                        my_order_model.setHouse(house);
                        my_order_model.setRecivername(rename);
                        my_order_model.setRecivermobile(renumber);
                        my_order_model.setDelivery_time_from(timefrom);
                        my_order_model.setSale_id(saleid);
                        my_order_model.setOn_date(placedon);
                        my_order_model.setDelivery_time_to(timeto);
                        my_order_model.setTotal_amount(ammount);
                        my_order_model.setStatus(status);
                        my_order_model.setTotal_items(item);

                        monthly_modelList.add(my_order_model);

                        delivered_monthly_adapter = new Delivered_monthly_adapter(monthly_modelList);

                        rc_monthlky.setAdapter(delivered_monthly_adapter);

                        delivered_monthly_adapter.notifyDataSetChanged();

                    }
                    if (monthly_modelList.isEmpty()) {
                        monthly_layout.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }


}
