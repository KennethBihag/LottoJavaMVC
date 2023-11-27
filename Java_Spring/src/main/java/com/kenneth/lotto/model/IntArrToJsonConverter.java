package com.kenneth.lotto.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class IntArrToJsonConverter implements AttributeConverter<int[], String> {
    @Override
    public String convertToDatabaseColumn(int[] attribute) {
        if (attribute == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for(int i=1; i<=attribute.length;++i){
            sb.append(attribute[i-1]);
            if(i<attribute.length)
                sb.append('-');
        }
        return sb.toString();
    }

    @Override
    public int[] convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return new int[0];
        }
        String[] tokens = dbData.split("-");
        int[] result = new int[tokens.length];
        for(int i=0; i<tokens.length;++i){
            result[i] = Integer.parseInt(tokens[i]);
        }
        return result;
    }

}