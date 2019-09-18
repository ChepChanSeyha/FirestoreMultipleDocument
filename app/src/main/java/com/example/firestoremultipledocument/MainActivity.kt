package com.example.firestoremultipledocument

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var editTitle: EditText
    private lateinit var editDescription: EditText
    private lateinit var viewData: TextView

    // My first note is collection
    // NoteBook is Document
    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference = firebaseFirestore.collection("Notebook")
    private val documentReference = firebaseFirestore.document("Notebook/My First Note")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        editTitle = findViewById(R.id.editTitle)
        editDescription = findViewById(R.id.editDescription)
        viewData = findViewById(R.id.viewData)

        btnAdd.setOnClickListener {
            val title = editTitle.text.toString()
            val description = editDescription.text.toString()

            val note = Note("",title, description)

            collectionReference.add(note)
        }

        btnLoad.setOnClickListener {
            collectionReference.get().addOnSuccessListener {
                var data = ""
                for (queryDocumentSnapshot in it) {
                    val note = queryDocumentSnapshot.data
                    val title = note["title"]
                    val description = note["description"]
                    data += "Title: $title\nDescription: $description\n\n"
                }
                viewData.text = data
            }.addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        collectionReference.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null) {
                return@addSnapshotListener
            }

            var data = ""
            for (queryDocumentSnapshot in querySnapshot!!) {

                val note = queryDocumentSnapshot.data
                val title = note["title"]
                val description = note["description"]

                val documentId = queryDocumentSnapshot.id

                data += "Id: $documentId \nTitle: $title\nDescription: $description\n\n"
            }
            viewData.text = data
        }
    }
}
