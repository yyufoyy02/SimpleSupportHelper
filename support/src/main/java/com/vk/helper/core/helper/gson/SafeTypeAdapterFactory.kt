package com.vk.helper.core.helper.gson

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

import java.io.IOException

class SafeTypeAdapterFactory : TypeAdapterFactory {

    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T> {

        val delegate = gson.getDelegateAdapter(this, type)

        return object : TypeAdapter<T>() {

            @Throws(IOException::class)
            override fun write(out: JsonWriter, value: T) {
                try {
                    delegate.write(out, value)
                } catch (e: IOException) {
                    delegate.write(out, null)
                }
            }

            @Throws(IOException::class)
            override fun read(`in`: JsonReader): T? {
                try {
                    return delegate.read(`in`)
                } catch (e: IOException) {
                    e.printStackTrace()
                    `in`.skipValue()
                    return null
                } catch (e: IllegalStateException) {
                    e.printStackTrace()
                    `in`.skipValue()
                    return null
                } catch (e: JsonSyntaxException) {
                    e.printStackTrace()
                    `in`.skipValue()
                    return null
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                    `in`.skipValue()
                    return null
                }
            }
        }
    }
}