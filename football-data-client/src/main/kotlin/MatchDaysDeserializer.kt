package com.secwager.refdata

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class MatchDaysDeserializer : JsonDeserializer<List<String>> {
    override fun deserialize(json: JsonElement, type: Type, context: JsonDeserializationContext): List<String> {
        return json.asJsonObject.getAsJsonObject("api")
                .getAsJsonArray("fixtures")
                .map { it.asString }
    }
}