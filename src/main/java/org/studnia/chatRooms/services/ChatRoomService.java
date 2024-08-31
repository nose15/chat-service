package org.studnia.chatRooms.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatRoomService {
    public ArrayList<String> getRoomsByUserId(String userId) {
        // fetch rooms from db
        return new ArrayList<String>(List.of("1", "2", "3", "4"));
    }
}
