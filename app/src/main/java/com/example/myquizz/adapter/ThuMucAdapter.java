package com.example.myquizz.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myquizz.R;
import com.example.myquizz.activity.ChiTietThuMucActivity;
import com.example.myquizz.model.ThuMuc;

import java.util.List;

public class ThuMucAdapter extends RecyclerView.Adapter<ThuMucAdapter.ThuMucViewHolder> {

    private Context context;
    private List<ThuMuc> danhSachThuMuc;

    public ThuMucAdapter(Context context, List<ThuMuc> danhSachThuMuc) {
        this.context = context;
        this.danhSachThuMuc = danhSachThuMuc;
    }

    public void capNhatDanhSach(List<ThuMuc> danhSachMoi) {
        this.danhSachThuMuc = danhSachMoi;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ThuMucViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_thu_muc, parent, false);
        return new ThuMucViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThuMucViewHolder holder, int position) {
        ThuMuc thuMuc = danhSachThuMuc.get(position);

        holder.txtTenThuMuc.setText(thuMuc.getTenThuMuc());
        holder.txtMoTaThuMuc.setText(thuMuc.getMoTaThuMuc());

        // Sự kiện khi bấm vào 1 Thư mục -> Mở ChiTietThuMucActivity và gửi THU_MUC_ID + THU_MUC_TEN sang
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChiTietThuMucActivity.class);
            intent.putExtra("THU_MUC_ID", thuMuc.getMaThuMuc());
            intent.putExtra("THU_MUC_TEN", thuMuc.getTenThuMuc());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return danhSachThuMuc != null ? danhSachThuMuc.size() : 0;
    }

    public static class ThuMucViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenThuMuc, txtMoTaThuMuc;

        public ThuMucViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenThuMuc = itemView.findViewById(R.id.txtTenThuMuc);
            txtMoTaThuMuc = itemView.findViewById(R.id.txtMoTaThuMuc);
        }
    }
}
