package com.kenneth.lotto.service;

import java.io.*;
import java.util.*;
import java.util.AbstractMap.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kenneth.lotto.model.*;
import com.kenneth.lotto.repository.LottoRepo;

@Service
public class ClientService implements LottoService {
    private final Map<String,List<int[]>> entries = new HashMap<>();
    @Autowired
    private LottoRepo repos;
    public ClientService() {}

    @Override
    public List<Client> getAll(){
        return (List<Client>)repos.getAllObjects(Client.class);
    }

    public int updateEntriesFromFile(String fileUri) {
        StringBuilder sb = new StringBuilder();
        try (Reader r = new FileReader(fileUri)) {
            final int lineLength = 64;
            char[] buffer = new char[lineLength];
            int read;
            while( (read=r.read(buffer)) > 0){
                sb.append(
                        String.valueOf(buffer,0,read));
            }
        } catch (IOException iox) {
            System.err.println(iox.getMessage());
            return -1;
        }
        return updateEntriesFromString(sb.toString());
    }
    public int updateEntriesFromString(String csvInput) {
        parseCsv(csvInput);
        int result =  repos.createClients(entries);
        entries.clear();
        return result;
    }
    public int parseCsvTest(String csvInput){
        return parseCsv(csvInput);
    }
    private int parseCsv(String csvInput) {
        int result=0;
        String[] lines = csvInput.split(System.lineSeparator());
        for (int i = 0; i < lines.length; ++i) {
            Map.Entry<String,int[]> entry = getEntryFromString(lines[i]);
            if(entry != null){
                String newName = entry.getKey();
                if(entries.containsKey(newName))
                    entries.get(newName).add(entry.getValue());
                else{
                    List<int[]> newEntries = new LinkedList<>();
                    entries.put(newName, newEntries);
                    newEntries.add(entry.getValue());
                }
                result++;
            }
        }
        return result;
    }
    private Map.Entry<String,int[]> getEntryFromString(String entryStr){
        String[] tokens = entryStr.split(",",0);
        boolean willRandomize=false;
        if(tokens[tokens.length-1].equals("LP"))
            willRandomize=true;
        if (tokens.length < LottoModel.maxPicks+1 & !willRandomize) {
            return null;
        } else if(tokens.length > LottoModel.maxPicks+1)
            return null;
        String name = tokens[0];
        name = name.trim();
        if(name.isBlank())
            return null;
        int start = 0;
        int stop = LottoModel.maxPicks-1;
        if(willRandomize)
            stop = tokens.length-3;
        int[] array = new int[LottoModel.maxPicks];
        try {
            for (; start <= stop; ++start)
                array[start] = Integer.parseInt(tokens[start + 1]);
        }catch (NumberFormatException nfx){
            System.err.println(entryStr);
            System.err.println("Entry value is not a number");
            return null;
        }
        if(willRandomize)
            LottoModel.randomize(array, start);
        if(isRepeated(name,array))
            return null;
        return new SimpleEntry<>(name,array);
    }
    private boolean isRepeated(String name, int[] picks){
        for(var e : entries.entrySet()){
            for(var i : e.getValue()){
                if(e.getKey().equals(name) & LottoRepo.areSameArrays(i,picks))
                    return true;
            }
        }
        return false;
    }
}