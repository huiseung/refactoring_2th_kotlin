package chatper1.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

val gson = Gson()

inline fun <reified T> readJson(fileName: String): T{
    val resource = object {}.javaClass.getResource("/$fileName")
    val content = resource.readText()
    return gson.fromJson(content, object : TypeToken<T>() {}.type)
}
