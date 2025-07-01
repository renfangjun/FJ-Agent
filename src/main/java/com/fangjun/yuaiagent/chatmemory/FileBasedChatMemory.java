package com.fangjun.yuaiagent.chatmemory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fangjun
 * @modify 2025-05-25 17:00:46
 * @motto Talk is cheap, show me the code!
 * @description <h1> </h1>
 */
public class FileBasedChatMemory implements ChatMemory {
    private final String BASE_DIR;

    private static final Kryo kryo = new Kryo();

    static {
        kryo.setRegistrationRequired(false);
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
    }

    public FileBasedChatMemory(String dir) {
        this.BASE_DIR = dir;
        File baseDir = new File(dir);
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
    }

    @Override
    public void add(String conversationId, Message message) {
        List<Message> conversationMessages = getOrCreateConversation(conversationId);
        conversationMessages.add(message);
        saveConversation(conversationId, conversationMessages);
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        List<Message> conversationMessages = getOrCreateConversation(conversationId);
        conversationMessages.addAll(messages);
        saveConversation(conversationId, conversationMessages);

    }

    @Override
    public List<Message> get(String conversationId, int lastN) {

        List<Message> messages = getOrCreateConversation(conversationId);
        List<Message> list = messages.stream().skip(Math.max(0, messages.size() - lastN)).toList();
        return list;
    }

    @Override
    public void clear(String conversationId) {

        File file = getConversationFile(conversationId);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 获取或创建会话消息的列表
     * @param conversationId
     * @return
     */
    private List<Message> getOrCreateConversation(String conversationId) {
        File file = getConversationFile(conversationId);
        List<Message> messages = new ArrayList<>();
        if (file.exists()) {
            try {
                Input input = new Input(new FileInputStream(file));
                messages = kryo.readObject(input, ArrayList.class);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return messages;
    }

    private void saveConversation(String conversationId, List<Message> messages) {
        File file = getConversationFile(conversationId);
        try (Output output = new Output(new FileOutputStream(file))) {
            kryo.writeObject(output, messages);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private File getConversationFile(String conversationId) {
        return new File(BASE_DIR + "/" + conversationId + ".kryo");
    }


}
