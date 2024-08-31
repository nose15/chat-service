package org.studnia.requests;
import org.json.*;

import org.studnia.messages.DTOs.MessageDTO;

public class MessageRequest implements Request {
    private final JSONObject json;
    public MessageRequest(String content) {
        this.json = new JSONObject(content);
    }
    @Override
    public MessageDTO toDTO() {
        return new MessageDTO(json.getString("message"), json.getString("chat_room_id"));
    }
}
