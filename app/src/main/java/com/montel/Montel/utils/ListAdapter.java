package com.montel.Montel.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate;
import com.google.gson.Gson;
import com.montel.Montel.R;
import com.montel.Montel.model.Customer;
import com.montel.Montel.ui.detailmarker.DetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>implements Filterable {
    //Deklarasi Variable
    private List<Customer> listCustomer;
    private List<Customer> customerListFiltered;
    private CustomerAdapterListener listener;
    private Context context;

    //ViewHolder Digunakan Untuk Menyimpan Referensi Dari View-View
    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView IDPEL, Nama, Alamat;
        private LinearLayout ListItem;
        private ImageView imageView;
        private RelativeLayout rrItem;

        ViewHolder(View itemView) {
            super(itemView);
            //Menginisialisasi View-View yang terpasang pada layout RecyclerView kita
            IDPEL = itemView.findViewById(R.id.tvIDPELC);
            Nama = itemView.findViewById(R.id.tvNamaC);
            Alamat = itemView.findViewById(R.id.tvAlamatC);
//            ListItem = itemView.findViewById(R.id.list_item);
            imageView = itemView.findViewById(R.id.imageViewC);
            rrItem = itemView.findViewById(R.id.rrItem);
        }
    }

    //Membuat Konstruktor, untuk menerima input dari Database
    public ListAdapter(Context context, List<Customer> listData, CustomerAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.listCustomer = listData;
        this.customerListFiltered = listData;
    }










    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Membuat View untuk Menyiapkan dan Memasang Layout yang Akan digunakan pada RecyclerView
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_customer, parent, false);
        return new ViewHolder(V);
    }





    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //Mengambil Nilai/Value yenag terdapat pada RecyclerView berdasarkan Posisi Tertentu

        final Customer customer= customerListFiltered.get(position);
        final String IDPEL = customer.getNoIDPL();
        final String NoMeter = customer.getNoMeter();
        final String Nama = customer.getName();
        final String Alamat = customer.getAddress();
        final String URL = customer.getImageURL1();
        final String TypeS = customer.getType();

        //Memasukan Nilai/Value kedalam View (TextView: NIM, Nama, Jurusan)


        if (TypeS != null & TypeS.matches("Pascabayar")){
            holder.IDPEL.setText("IDPEL: "+IDPEL);
        }else if (TypeS != null & TypeS.matches("Prabayar")){
            holder.IDPEL.setText("No Meter: "+NoMeter);

        }else if (TypeS != null & TypeS.matches("Non Register")){
            holder.IDPEL.setVisibility(View.GONE);
        }

        holder.Nama.setText("Nama: "+Nama);
        holder.Alamat.setText("Alamat: "+Alamat);
        Picasso.get().load(URL).into(holder.imageView);



        holder.rrItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DO ACTION
                Gson gson = new Gson();

                String jsonCustomer = gson.toJson(listCustomer.get(position));
                String key = listCustomer.get(position).getKey();

                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("key", key);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        //Menghitung Ukuran/Jumlah Data Yang Akan Ditampilkan Pada RecyclerView
        return customerListFiltered.size();
    }

    public void removeItem(int position) {
        listCustomer.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }



    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    customerListFiltered = listCustomer;
                } else {
                    ArrayList<Customer> filteredList = new ArrayList<>();
                    for (Customer row : listCustomer) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    customerListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = customerListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                customerListFiltered = (ArrayList<Customer>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface CustomerAdapterListener {
        void onContactSelected(Customer customer);
    }
}
