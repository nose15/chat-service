package org.studnia.endpoints;

import jakarta.websocket.DecodeException;
import jakarta.websocket.Decoder;
import org.json.JSONException;
import org.json.JSONObject;
import org.studnia.general.exceptions.MessageValidationException;
import org.studnia.messages.DTOs.MessageDTO;

public class MessageDecoder implements Decoder.Text<MessageDTO> {
    @Override
    public MessageDTO decode(String message) throws DecodeException {
        if (message == null || message.trim().isEmpty()) {
            throw new MessageValidationException("Message is empty");
        }
        JSONObject messageJSON;
        try {
            messageJSON = new JSONObject(message);
        } catch (JSONException e) {
            throw new MessageValidationException("Invalid message JSON format.");
        }

        String newMessage;
        String chatRoomId;
        try {
            newMessage = messageJSON.getString("message");
        } catch (JSONException e) {
            throw new MessageValidationException("Invalid 'message' property.");
        }

        try {
            chatRoomId = messageJSON.getString("chat_room_id");
        } catch (JSONException e) {
            throw new MessageValidationException("Invalid 'chat_room_id' property.");
        }

        return new MessageDTO(newMessage, chatRoomId);
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }
}
