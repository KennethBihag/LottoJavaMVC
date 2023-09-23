package com.kenneth.lotto.service;

import java.io.*;
import java.util.*;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

import com.kenneth.lotto.model.*;
import static com.kenneth.lotto.repository.LottoRepo.*;

@Service
public class ClientService implements LottoService {
    private static final int fileSize = 1024;
    private static final Map<String, int[]> entries = new HashMap<>();
    public ClientService() {
    }

    private int[] toIntArray(String[] tokens) {
        if (tokens.length != LottoModel.maxPicks)
            return null;
        int[] result = new int[LottoModel.maxPicks];
        boolean randomize = false;
        int i = 0;
        for (; i < tokens.length; ++i) {
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
            randomize(result,i);
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

    public boolean updateEntriesFromString(String csvInput) {
        boolean result = false;
        em.getTransaction().begin();
        validateInput(csvInput);
        Collection<String> toRemove = new ArrayList<>();
        Iterator<Map.Entry<String, int[]>> itr = entries.entrySet().iterator();
        while (itr.hasNext()) {
            var e = itr.next();
            try {
                Client client = new Client(e.getKey(),e.getValue());
                String jpql = String.format(
                        "SELECT c FROM Client c WHERE c.name = '%s'",
                        e.getKey());
                Query query = em.createQuery(jpql, Client.class);
                Client found = null;
                try{
                    found = (Client)query.getSingleResult();
                }catch (NoResultException ignored){}
                if (found == null) {
                    result = true;
                    em.persist(client);
                }
            } catch (IllegalArgumentException ignored) {
                itr.remove();
            }
        }
        em.getTransaction().commit();
        return result;
    }

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
    @Override
    public List<Client> getLottoModels(){
        String jpql = "SELECT c FROM Client c";
        Query query = em.createQuery(jpql, Client.class);
        return (List<Client>)query.getResultList();
    }
}