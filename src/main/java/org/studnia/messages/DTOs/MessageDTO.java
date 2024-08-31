package org.studnia.messages.DTOs;

import org.studnia.general.DTOs.DTO;

final public class MessageDTO extends DTO {
    public String message;
    public String chatRoomId;
    public MessageDTO(String message, String chatRoomId) {
        this.message = message;
        this.chatRoomId = chatRoomId;
    }
}
