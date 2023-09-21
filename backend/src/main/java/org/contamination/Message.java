package org.contamination;

import com.google.gson.JsonObject;

public record Message(String type, JsonObject content) {
}
