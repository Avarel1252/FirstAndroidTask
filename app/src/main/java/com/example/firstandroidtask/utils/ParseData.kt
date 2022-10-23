package com.example.firstandroidtask.utils

class ParseData {
    fun parseUserName(extra: String): Pair<String, String> {
        val name = extra.substringBefore(".")
        val surname = extra.substringAfter(".").substringBefore("@")
        return Pair(name, surname)
    }
}