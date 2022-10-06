package net.softsociety.Team4GroupWare.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.softsociety.Team4GroupWare.domain.ChatRoom;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private Map<String, ChatRoom> chatRooms;

    @PostConstruct
    // 의존관게 주입완료되면 실행되는 코드
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    // 채팅방 불러오기
    public List<ChatRoom> findAllRoom() {
        // 채팅방 최근 생성 순으로 반환
        List<ChatRoom> result = new ArrayList<>(chatRooms.values());
        Collections.reverse(result);

        return result;
    }

    // 채팅방 하나 불러오기
    public ChatRoom findById(String roomId) {
        System.out.println("안녕1");
        return chatRooms.get(roomId);
    }

    // 채팅방 생성
    public ChatRoom createRoom(String name) {
        System.out.println("안녕2");
        ChatRoom chatRoom = ChatRoom.create(name);
        chatRooms.put(chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }
}
