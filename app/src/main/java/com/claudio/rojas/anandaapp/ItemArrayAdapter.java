package com.claudio.rojas.anandaapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.claudio.rojas.anandaapp.request.AlarmaService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemArrayAdapter extends RecyclerView.Adapter<ItemArrayAdapter.ViewHolder> {

    //All methods in this adapter are required for a bare minimum recyclerview adapter
    private int listItemLayout;
    final ArrayList<Item> itemList;
    // Constructor of the class
    public ItemArrayAdapter(int layoutId, ArrayList<Item> itemList) {
        listItemLayout = layoutId;
        this.itemList = itemList;
    }

    // get the size of the list
    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }


    // specify the row layout file and click for each row
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(listItemLayout, parent, false);
        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    // load data in each row element
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition) {
        TextView item = holder.item;
        item.setText(itemList.get(listPosition).getName());
    }

    // Static inner class to initialize the views of rows
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView item;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            item = (TextView) itemView.findViewById(R.id.row_item);
        }
        @Override
        public void onClick(final View view) {
            Log.d("onclick", "onClick " + getLayoutPosition() + " " + item.getText());

            final Long idUsuario = Listado.superListaPrincipal.get(getLayoutPosition()).getId();
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

            builder
                    .setMessage("¿Seguro que desea eliminar esta alarma (" + idUsuario + ")?")
                    .setPositiveButton("Si",  new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                            AlarmaService alarmaService = new AlarmaService();
                            alarmaService.deleteAlarma(idUsuario).enqueue(new Callback<Void>() {

                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                                    alertDialog.setTitle("Alerta");
                                    alertDialog.setMessage("Eliminado con exito");
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cerrar",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    alertDialog.show();
                                    Listado.superListaPrincipal.remove(getLayoutPosition());
                                    Listado.itemArrayAdapter.notifyItemRemoved(getLayoutPosition());
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                                    alertDialog.setTitle("Alerta");
                                    alertDialog.setMessage("Error al eliminar, intentelo mas tarde");
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cerrar",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    alertDialog.show();
                                }
                            });
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                        }
                    })
                    .show();
        }
    }
}