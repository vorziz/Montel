package com.montel.Montel.ui.data;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.adprogressbarlib.AdCircleProgress;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.montel.Montel.R;
import com.montel.Montel.model.Customer;
import com.montel.Montel.ui.inputdata.InputData;
import com.montel.Montel.utils.ListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DataFragment extends Fragment implements ListAdapter.CustomerAdapterListener {

    private DataViewModel dataViewModel;
    @BindView(R.id.rv_list_customer) RecyclerView recyclerView;

    //Deklarasi Variable untuk RecyclerView
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    ListAdapter listAdapter;

    //Deklarasi Variable Database Reference dan ArrayList dengan Parameter Class Model kita.
    private DatabaseReference reference;
    private List<Customer> dataCustomer;
    @BindView(R.id.spFilter) AppCompatSpinner appCompatSpinner;
    @BindView(R.id.searchView)
    SearchView searchView;

    Button btnSearch;
//    @BindView(R.id.pgb_progress) AdCircleProgress adCircleProgress;
    String selectSP;

    private FirebaseAuth auth;

    Context context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        dataViewModel =
                ViewModelProviders.of(this).get(DataViewModel.class);
        View root = inflater.inflate(R.layout.fragment_list, container, false);




        context = this.getContext();
        ButterKnife.bind(this,root);

        auth = FirebaseAuth.getInstance();
        MyRecyclerView();

        listAdapter = new ListAdapter(context,dataCustomer,this::onContactSelected);

        doSpinner();
        //GetData();

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                listAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                listAdapter.getFilter().filter(query);
                return false;
            }
        });





        return root;
    }


    private void doSpinner() {
        // Div Spinner implementing onItemSelectedListener


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.filter, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        appCompatSpinner.setAdapter(adapter);
        appCompatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                // do something upon option selection

                selectSP = parent.getItemAtPosition(position).toString();

                if (selectSP.equals("All")){
                    GetData(selectSP);
                }else if (selectSP.equals("Pascabayar")){
                    GetData(selectSP);

                }else if (selectSP.equals("Prabayar")){
                    GetData(selectSP);

                }else if (selectSP.equals("Non IDPEL")){
                    GetData(selectSP);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // can leave this empty
            }

        });
    }


    //Berisi baris kode untuk mengambil data dari Database dan menampilkannya kedalam Adapter
    private void GetData(String dataS){
        reference = FirebaseDatabase.getInstance().getReference("customers");

        if (dataS.equals("All")){


            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Inisialisasi ArrayList
                    dataCustomer = new ArrayList<>();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        //Mapping data pada DataSnapshot ke dalam objek mahasiswa
                        Customer customers = snapshot.getValue(Customer.class);

                        //Mengambil Primary Key, digunakan untuk proses Update dan Delete
                        customers.setKey(snapshot.getKey());
                        dataCustomer.add(customers);
                    }

                    //Inisialisasi Adapter dan data Mahasiswa dalam bentuk Array
                    listAdapter = new ListAdapter(context,dataCustomer, DataFragment.this::onContactSelected);

                    //Memasang Adapter pada RecyclerView
                    recyclerView.setAdapter(listAdapter);
                    listAdapter.notifyDataSetChanged();


                    //Toast.makeText(context,"Data Berhasil Dimuat", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(context,"Data Gagal Dimuat", Toast.LENGTH_LONG).show();
                    Log.e("MyListActivity", databaseError.getDetails()+" "+databaseError.getMessage());
                }


            });

        }else if (dataS.equals("Prabayar")){
            //reference = FirebaseDatabase.getInstance().getReference("customers");
            reference.orderByChild("type").equalTo("Prabayar").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Inisialisasi ArrayList
                    dataCustomer = new ArrayList<>();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        //Mapping data pada DataSnapshot ke dalam objek mahasiswa
                        Customer customers = snapshot.getValue(Customer.class);

                        //Mengambil Primary Key, digunakan untuk proses Update dan Delete
                        customers.setKey(snapshot.getKey());
                        dataCustomer.add(customers);
                    }

                    //Inisialisasi Adapter dan data Mahasiswa dalam bentuk Array
                    listAdapter= new ListAdapter(context,dataCustomer, DataFragment.this::onContactSelected);

                    //Memasang Adapter pada RecyclerView
                    recyclerView.setAdapter(listAdapter);
                    listAdapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //Toast.makeText(context,"Data Gagal Dimuat", Toast.LENGTH_LONG).show();
                    Log.e("MyListActivity", databaseError.getDetails()+" "+databaseError.getMessage());
                }


            });

        }else if (dataS.equals("Pascabayar")){

            reference.orderByChild("type").equalTo("Pascabayar").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Inisialisasi ArrayList
                    dataCustomer = new ArrayList<>();


                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        //Mapping data pada DataSnapshot ke dalam objek mahasiswa
                        Customer customers = snapshot.getValue(Customer.class);

                        //Mengambil Primary Key, digunakan untuk proses Update dan Delete
                        customers.setKey(snapshot.getKey());
                        dataCustomer.add(customers);
                    }

                    //Inisialisasi Adapter dan data Mahasiswa dalam bentuk Array
                    listAdapter= new ListAdapter(context,dataCustomer, DataFragment.this::onContactSelected);

                    //Memasang Adapter pada RecyclerView
                    recyclerView.setAdapter(listAdapter);
                    listAdapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //Toast.makeText(context,"Data Gagal Dimuat", Toast.LENGTH_LONG).show();
                    Log.e("MyListActivity", databaseError.getDetails()+" "+databaseError.getMessage());
                }


            });


        }else if (dataS.equals("Non IDPEL")){

            reference.orderByChild("type").equalTo("Non IDPEL").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Inisialisasi ArrayList
                    dataCustomer = new ArrayList<>();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        //Mapping data pada DataSnapshot ke dalam objek mahasiswa
                        Customer customers = snapshot.getValue(Customer.class);

                        //Mengambil Primary Key, digunakan untuk proses Update dan Delete
                        //customers.setKey(snapshot.getKey());
                        dataCustomer.add(customers);
                    }

                    //Inisialisasi Adapter dan data Mahasiswa dalam bentuk Array
                    listAdapter= new ListAdapter(context,dataCustomer, DataFragment.this::onContactSelected);

                    //Memasang Adapter pada RecyclerView
                    recyclerView.setAdapter(listAdapter);
                    listAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //Toast.makeText(context,"Data Gagal Dimuat", Toast.LENGTH_LONG).show();
                    Log.e("MyListActivity", databaseError.getDetails()+" "+databaseError.getMessage());
                }


            });


        }

    }

    //Methode yang berisi kumpulan baris kode untuk mengatur RecyclerView
    private void MyRecyclerView(){
        //Menggunakan Layout Manager, Dan Membuat List Secara Vertical
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //Membuat Underline pada Setiap Item Didalam List
        DividerItemDecoration itemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(context, R.drawable.line));
        recyclerView.addItemDecoration(itemDecoration);
    }


    @Override
    public void onContactSelected(Customer customer) {
        Toast.makeText(context, "Selected: " + customer.getName() + ", " + customer.getPhone(), Toast.LENGTH_SHORT).show();
    }
}