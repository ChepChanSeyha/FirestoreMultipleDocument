package com.example.firestoremultipledocument

import com.google.firebase.firestore.Exclude

data class Note(@Exclude val documentId: String, val title: String, val description: String)