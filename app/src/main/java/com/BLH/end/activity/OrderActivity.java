package com.BLH.end.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.BLH.end.MainActivity;
import com.BLH.end.R;
import com.BLH.end.adapter.AddressSelectAdapter;
import com.BLH.end.adapter.OrderAdapter;
import com.BLH.end.bean.AddressBean;
import com.BLH.end.bean.CartBean;
import com.BLH.end.db.AddressDao;
import com.BLH.end.db.OrderDao;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    private List<CartBean> cartList;
    private double totalPrice;
    private String shopName;
    private int shopId;
    private boolean isGlobal = false;
    private AddressBean selectedAddress = null;
    private EditText etAddress;
    private AddressDao addressDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        addressDao = new AddressDao(this);

        cartList = (List<CartBean>) getIntent().getSerializableExtra("cartList");
        shopId = getIntent().getIntExtra("shopId", -1);
        shopName = getIntent().getStringExtra("shopName");
        isGlobal = getIntent().getBooleanExtra("isGlobal", false);

        if (cartList == null || cartList.isEmpty()) {
            finish();
            return;
        }

        totalPrice = 0;
        for (CartBean bean : cartList) {
            totalPrice += bean.getPrice() * bean.getCount();
        }

        TextView tvTotal = findViewById(R.id.tv_order_total);
        tvTotal.setText("合计：¥" + totalPrice);

        etAddress = findViewById(R.id.et_address);
        TextView btnPay = findViewById(R.id.btn_pay);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_order);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new OrderAdapter(this, cartList));

        // 点击地址框弹出底部抽屉
        etAddress.setOnClickListener(v -> showAddressBottomSheet());

        btnPay.setOnClickListener(v -> {
            if (selectedAddress == null) {
                Toast.makeText(this, "请先选择收货地址", Toast.LENGTH_SHORT).show();
                return;
            }
            showPayDialog(selectedAddress.getDetail(), selectedAddress.getPhone());
        });
    }

    private void showAddressBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.dialog_select_address, null);
        bottomSheetDialog.setContentView(sheetView);

        RecyclerView rv = sheetView.findViewById(R.id.recycler_view_address);
        rv.setLayoutManager(new LinearLayoutManager(this));

        // 局部刷新方法
        Runnable refreshData = () -> {
            List<AddressBean> newList = addressDao.getAllAddress();
            AddressSelectAdapter adapter = new AddressSelectAdapter(this, newList, new AddressSelectAdapter.OnActionListener() {
                @Override
                public void onSelect(AddressBean bean) {
                    selectedAddress = bean;
                    etAddress.setText("收货人：" + bean.getName() + "  " + bean.getPhone() + "\n地址：" + bean.getDetail());
                    bottomSheetDialog.dismiss();
                }

                @Override
                public void onEdit(AddressBean bean) {
                    bottomSheetDialog.dismiss();
                    showEditAddressDialog(bean);
                }

                @Override
                public void onDelete(AddressBean bean) {
                    new AlertDialog.Builder(OrderActivity.this)
                            .setTitle("删除地址")
                            .setMessage("确定要删除这条地址吗？")
                            .setPositiveButton("确定", (dialog, which) -> {
                                addressDao.deleteAddress(bean.getId());
                                if (selectedAddress != null && selectedAddress.getId() == bean.getId()) {
                                    selectedAddress = null;
                                    etAddress.setText("");
                                }
                                bottomSheetDialog.dismiss();
                            })
                            .setNegativeButton("取消", null)
                            .show();
                }
            });
            rv.setAdapter(adapter);
        };

        refreshData.run();

        sheetView.findViewById(R.id.tv_add_new_address).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            showEditAddressDialog(null);
        });

        bottomSheetDialog.show();
    }

    private void showEditAddressDialog(AddressBean editBean) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);

        EditText etName = new EditText(this); etName.setHint("姓名"); layout.addView(etName);
        EditText etPhone = new EditText(this); etPhone.setHint("电话"); layout.addView(etPhone);
        EditText etDetail = new EditText(this); etDetail.setHint("详细地址"); layout.addView(etDetail);

        if (editBean != null) {
            etName.setText(editBean.getName());
            etPhone.setText(editBean.getPhone());
            etDetail.setText(editBean.getDetail());
        }

        new AlertDialog.Builder(this)
                .setTitle(editBean == null ? "新增地址" : "修改地址")
                .setView(layout)
                .setPositiveButton("保存", (dialog, which) -> {
                    String name = etName.getText().toString().trim();
                    String phone = etPhone.getText().toString().trim();
                    String detail = etDetail.getText().toString().trim();
                    if (name.isEmpty() || phone.isEmpty() || detail.isEmpty()) {
                        Toast.makeText(this, "请填完整", Toast.LENGTH_SHORT).show();
                    } else {
                        if (editBean == null) {
                            addressDao.addAddress(name, phone, detail);
                        } else {
                            addressDao.updateAddress(editBean.getId(), name, phone, detail);
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void showPayDialog(String address, String phone) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_pay, null);
        TextView tvAmount = dialogView.findViewById(R.id.tv_pay_amount);
        tvAmount.setText("￥" + totalPrice);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialog.show();

        TextView btnSuccess = dialogView.findViewById(R.id.btn_pay_success);
        btnSuccess.setOnClickListener(v -> {
            OrderDao orderDao = new OrderDao(this);
            if (isGlobal) {
                orderDao.submitOrder(0, "多店合并订单", cartList, address, phone);
            } else {
                orderDao.submitOrder(shopId, shopName, cartList, address, phone);
            }

            Toast.makeText(this, "支付成功，下单完成！", Toast.LENGTH_LONG).show();
            dialog.dismiss();

            Intent intent = new Intent(OrderActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}