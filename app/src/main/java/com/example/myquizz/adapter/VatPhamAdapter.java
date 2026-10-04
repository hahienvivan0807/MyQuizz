package com.example.myquizz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myquizz.R;
import com.example.myquizz.model.VatPham;

import java.util.List;

public class VatPhamAdapter extends RecyclerView.Adapter<VatPhamAdapter.VatPhamViewHolder> {

    private Context context;
    private List<VatPham> danhSachVatPham;

    public VatPhamAdapter(Context context, List<VatPham> danhSachVatPham) {
        this.context = context;
        this.danhSachVatPham = danhSachVatPham;
    }

    public void capNhatDanhSach(List<VatPham> danhSachMoi) {
        this.danhSachVatPham = danhSachMoi;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VatPhamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_vat_pham, parent, false);
        return new VatPhamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VatPhamViewHolder holder, int position) {
        VatPham vatPham = danhSachVatPham.get(position);

        holder.txtTieuDe.setText(vatPham.getTieuDe());
        holder.txtMoTa.setText(vatPham.getMoTa());

        // Đặt mặc định biểu tượng Quiz cho tất cả vật phẩm
        holder.imgIcon.setImageResource(R.drawable.ic_leaf);
        holder.imgIcon.setBackgroundResource(R.drawable.bg_icon_soft);
        holder.imgIcon.setColorFilter(ContextCompat.getColor(context, R.color.primary));
    }

    @Override
    public int getItemCount() {
        return danhSachVatPham != null ? danhSachVatPham.size() : 0;
    }

    public static class VatPhamViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIcon;
        TextView txtTieuDe, txtMoTa;

        public VatPhamViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.imgIconVatPham);
            txtTieuDe = itemView.findViewById(R.id.txtTieuDeVatPham);
            txtMoTa = itemView.findViewById(R.id.txtMoTaVatPham);
        }
    }
}
