package com.example.notespassword;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
        import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private List<NoteModel> notes;
    private Context context;


    public NoteAdapter(List<NoteModel> notes, Context context) {
        this.notes = notes;
        this.context = context;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notes_layout, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        ImageView popupButton = holder.itemView.findViewById(R.id.notes_Button_dot);
        int colorCode = getRandomColor();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            holder.linear.setBackgroundColor(holder.itemView.getResources().getColor(colorCode,null));
        }
        NoteModel note = notes.get(position);
        holder.noteTitleTextView.setText(note.getTitle());
        holder.noteContentTextView.setText(note.getContent());

        String docId = String.valueOf(holder.itemView.getId());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), Notes_details_Activity.class);
                i.putExtra("title",note.getTitle());
                i.putExtra("content",note.getContent());
                i.putExtra("noteId",docId);
                v.getContext().startActivity(i);

            }
        });
        popupButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(),v);
                popupMenu.setGravity(Gravity.END);
                popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(@NonNull MenuItem item) {
                        Intent i = new Intent(v.getContext(), Edit_notes_Activity.class);
                        i.putExtra("title",note.getTitle());
                        i.putExtra("content",note.getContent());
                        i.putExtra("noteId",docId);
                        v.getContext().startActivity(i);
                        return false;
                    }
                });
                popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(@NonNull MenuItem item) {
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                        firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").whereEqualTo("title",note.getTitle()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful() && !task.getResult().isEmpty()) {
                                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);

                                    String documentId = documentSnapshot.getId();
                                    firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(documentId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(context, "Note deleted successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "some error occured", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else {
                                    Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        //FirebaseFirestore db = FirebaseFirestore.getInstance();
//                        db.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(docId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                Toast.makeText(context, "note deleted successfully", Toast.LENGTH_SHORT).show();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(context, "Failed to deleted", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                        DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(docId);
//                        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                Toast.makeText(context, "Note deleted successful" +" "+ firebaseUser.getUid()+ " "+docId, Toast.LENGTH_LONG).show();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
//                            }
//                        });

                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        public TextView noteTitleTextView;
        public TextView noteContentTextView;
        LinearLayout linear;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitleTextView = itemView.findViewById(R.id.note_title);
            noteContentTextView = itemView.findViewById(R.id.notes_content);
            linear = itemView.findViewById(R.id.note_layout);
        }
    }

    private int getRandomColor() {
        List<Integer> colorCode = new ArrayList<>();
        colorCode.add(R.color.yellow);
        colorCode.add(R.color.sky);
        colorCode.add(R.color.sky_dark);
        colorCode.add(R.color.red);
        colorCode.add(R.color.pink);
        colorCode.add(R.color.light_yellow);
        colorCode.add(R.color.purpul);
        colorCode.add(R.color.light_green);
        colorCode.add(R.color.blue);
        colorCode.add(R.color.lime);
        colorCode.add(R.color.purpul);
        colorCode.add(R.color.brown);
        colorCode.add(R.color.cyan);
        colorCode.add(R.color.gold);
        colorCode.add(R.color.indigo);
        colorCode.add(R.color.gray);
        colorCode.add(R.color.magenta);
        colorCode.add(R.color.maroon);
        colorCode.add(R.color.navy);
        colorCode.add(R.color.silver);
        colorCode.add(R.color.teal);
        Random random = new Random();
        int num = random.nextInt(colorCode.size());
        return colorCode.get(num);
    }
}
