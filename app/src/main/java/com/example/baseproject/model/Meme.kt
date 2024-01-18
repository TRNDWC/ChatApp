package com.example.baseproject.model

class Memes(
    var memes: List<Meme>
)

class Meme (
    var id: String,
    var name: String,
    var url: String,
    var width: Int,
    var height: Int,
    var box_count: Int
)
