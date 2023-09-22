package com.kenneth.lotto.service;

import java.io.*;
import java.util.*;

import org.springframework.stereotype.Service;

import com.kenneth.lotto.model.*;
import com.kenneth.lotto.ServletInitializer;

@Service
public class ClientService implements LottoService {
    private static final Map<String, int[]> entries = new HashMap<>();
    public ClientService() {
    }

    private int[] toIntArray(String[] tokens) {
        if (tokens.length != Client.maxPicks)
            return null;
        int[] result = new int[Client.maxPicks];
        boolean randomize = false;
        int i = 0;
        for (; i < Client.maxPicks; ++i) {
            if (!tokens[i].equals("LP")) {
                try {
                    result[i] = Integer.parseInt(tokens[i]);
                } catch (NumberFormatException ex) {
                    return null;
                }
            } else {
                randomize = true;
                break;
            }
        }
        if (randomize)
            for (; i < Client.maxPicks; ++i) {
                Random random = new Random();
                result[i] = random.nextInt(45) + 1;
            }
        return result;
    }

    private char[] getCharsFromStream(InputStream inStream) throws IOException {
        InputStreamReader br = new InputStreamReader(inStream);
        char[] buffer = new char[fileSize];
        int offset = 0;
        do {
            int chars = br.read(buffer, offset, fileSize);
            boolean hasMore = chars != -1;
            if (hasMore) {
                char[] temp = Arrays.copyOf(buffer, buffer.length * 2);
                buffer = temp;
                offset += chars;
            } else break;
        } while (true);
        return buffer;
    }

    @Override
    public void validateInput(String csvInput) {
        String[] lines = csvInput.split(System.lineSeparator());
        for (int i = 0; i < lines.length; ++i) {
            String[] tokens = lines[i].split(",", -1);
            if (tokens.length < 7)
                if (!tokens[tokens.length - 1].equals("LP"))
                    continue;
            String name = tokens[0];
            if (entries.containsKey(name))
                continue;
            int[] values = toIntArray(Arrays.copyOfRange(
                    tokens, 1, tokens.length));
            if (values == null)
                continue;
            entries.put(name, values);
        }
    }

    @Override
    public boolean updateEntriesFromString(String csvInput) {
        boolean result = false;
        ServletInitializer.em.getTransaction().begin();
        validateInput(csvInput);
        Collection<String> toRemove = new ArrayList<>();
        Iterator<Map.Entry<String, int[]>> itr = entries.entrySet().iterator();
        while (itr.hasNext()) {
            var e = itr.next();
            try {
                Client client = new Client(e.getKey(), e.getValue());
                Client found = ServletInitializer.em.find(Client.class, client.getName());
                if (found == null) {
                    result = true;
                    ServletInitializer.em.persist(client);
                }
            } catch (IllegalArgumentException ignored) {
                itr.remove();
            }
        }
        ServletInitializer.em.getTransaction().commit();
        return result;
    }

    @Override
    public boolean updateEntriesFromFile(String fileUri) {
        char[] buffer = null;
        boolean result = false;
        try {
            InputStream fis = new FileInputStream(fileUri);
            buffer = getCharsFromStream(fis);
        } catch (IOException ignored) {
        } finally {
            if (buffer != null) {
                result = updateEntriesFromString(String.valueOf(buffer));
            }
        }
        return result;
    }
}