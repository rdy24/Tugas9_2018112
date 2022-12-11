package com.example.healthcare.sqllite;

import static com.example.healthcare.sqllite.DBmain.TABLENAME;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcare.R;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
  Context context;
  int singledata;
  ArrayList<Model> modelArrayList;
  SQLiteDatabase sqLiteDatabase;

  public MyAdapter(Context context, int singledata, ArrayList<Model> modelArrayList, SQLiteDatabase sqLiteDatabase) {
    this.context = context;
    this.singledata = singledata;
    this.modelArrayList = modelArrayList;
    this.sqLiteDatabase = sqLiteDatabase;
  }

  @NonNull
  @Override
  public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(context);
    View view = inflater.inflate(R.layout.single_data, null);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
    final Model model = modelArrayList.get(position);
    byte[] image = model.getAvatar();
    Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0,
            image.length);
    holder.avatar.setImageBitmap(bitmap);
    holder.txtTitle.setText(model.getTitle());
    holder.txtDatePublish.setText(model.getDatePublish());
    //flow menu
    holder.flowmenu.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        PopupMenu popupMenu = new PopupMenu(context,
                holder.flowmenu);
        popupMenu.inflate(R.menu.flow_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
          @Override
          public boolean onMenuItemClick(MenuItem item)
          {
            switch (item.getItemId()){
              case R.id.edit_menu:
                Bundle bundle = new Bundle();
                bundle.putInt("id",model.getId());
                bundle.putString("title",model.getTitle());
                bundle.putString("date_publish",model.getDatePublish());
                bundle.putByteArray("avatar",model.getAvatar());
                Intent intent = new Intent(context, TambahBerita.class);
                intent.putExtra("userdata",bundle);
                context.startActivity(intent);
                break;
              case R.id.delete_menu:
                DBmain dBmain = new DBmain(context);
                sqLiteDatabase = dBmain.getReadableDatabase();
                long recdelete = sqLiteDatabase.delete(TABLENAME,"id="+model.getId(),null);
                if (recdelete != -1){
                  Toast.makeText(context, "Data Deleted",Toast.LENGTH_SHORT).show();
                  modelArrayList.remove(position);
                  notifyDataSetChanged();
                }
                break;
              default:
                return false;
            }
            return false;
          }
        });
        //display menu
        popupMenu.show();
      }
    });
  }

  @Override
  public int getItemCount() {
    return modelArrayList.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    ImageView avatar;
    TextView txtTitle, txtDatePublish;
    ImageButton flowmenu;
    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      avatar = (ImageView)itemView.findViewById(R.id.viewavatar);
      txtTitle = (TextView) itemView.findViewById(R.id.txt_judul);
      txtDatePublish = (TextView) itemView.findViewById(R.id.txt_tanggal);
      flowmenu = (ImageButton) itemView.findViewById(R.id.flowmenu);
    }
  }
}
