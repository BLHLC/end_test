package com.BLH.end.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.BLH.end.R;
import com.BLH.end.adapter.AddressAdapter;
import com.BLH.end.bean.AddressBean;
import com.BLH.end.db.AddressDao;
import java.util.List;

public class AddressListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AddressDao addressDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        addressDao = new AddressDao(this);

        recyclerView = findViewById(R.id.recycler_view_address);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadData();

        // 新增地址
        findViewById(R.id.tv_add_address).setOnClickListener(v -> showAddressDialog(null));
    }

    private void loadData() {
        List<AddressBean> list = addressDao.getAllAddress();

        AddressAdapter adapter = new AddressAdapter(this, list, new AddressAdapter.OnDeleteClickListener() {
            @Override
            public void onDelete(AddressBean bean) {
                new AlertDialog.Builder(AddressListActivity.this)
                        .setTitle("删除地址")
                        .setMessage("确定要删除这条地址吗？")
                        .setPositiveButton("确定", (dialog, which) -> {
                            addressDao.deleteAddress(bean.getId());
                            loadData();
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }

            @Override
            public void onEdit(AddressBean bean) {
                // 触发修改，把已有的数据传进去
                showAddressDialog(bean);
            }
        });

        recyclerView.setAdapter(adapter);
    }

    /**
     * 通用的地址弹窗方法
     * @param editBean 如果为 null 则是新增，如果不为 null 则是修改
     */
    private void showAddressDialog(AddressBean editBean) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 20);

        EditText etName = new EditText(this); etName.setHint("联系人姓名"); layout.addView(etName);
        EditText etPhone = new EditText(this); etPhone.setHint("联系电话"); layout.addView(etPhone);
        EditText etDetail = new EditText(this); etDetail.setHint("详细收货地址"); layout.addView(etDetail);

        // 如果是修改，把原来的数据填进去
        if (editBean != null) {
            etName.setText(editBean.getName());
            etPhone.setText(editBean.getPhone());
            etDetail.setText(editBean.getDetail());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(editBean == null ? "新增收货地址" : "修改收货地址");
        builder.setView(layout);

        builder.setPositiveButton("保存", (dialog, which) -> {
            String name = etName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String detail = etDetail.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty() || detail.isEmpty()) {
                Toast.makeText(this, "请填完整", Toast.LENGTH_SHORT).show();
            } else {
                if (editBean == null) {
                    // 新增
                    addressDao.addAddress(name, phone, detail);
                } else {
                    // 修改
                    addressDao.updateAddress(editBean.getId(), name, phone, detail);
                }
                loadData();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }
}