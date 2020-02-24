package jolyjdia.vk.api.objects.docs;

import com.google.gson.annotations.SerializedName;
import jolyjdia.vk.api.queries.EnumParam;

public enum DocAttachmentType implements EnumParam {
    @SerializedName("doc")
    DOC("doc"),

    @SerializedName("graffiti")
    GRAFFITI("graffiti"),

    @SerializedName("audio_message")
    AUDIO_MESSAGE("audio_message");

    private final String value;

    DocAttachmentType(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
