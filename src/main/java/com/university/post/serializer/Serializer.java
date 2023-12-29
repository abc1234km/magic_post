package com.university.post.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.university.post.dto.DeliveryPointDTO;

public class Serializer extends JsonSerializer<Object>{

    @Override
    public void serialize(Object object, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        jsonGenerator.writeStartObject();
        if (object instanceof DeliveryPointDTO) {
            this.getJsonDeliveryPoint((DeliveryPointDTO) object, jsonGenerator);
        } 
        jsonGenerator.writeEndObject();
    }

    public void getJsonDeliveryPoint(DeliveryPointDTO deliveryPointDTO, JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeObjectField("id", deliveryPointDTO.getId());
        jsonGenerator.writeNumberField("pointId", deliveryPointDTO.getPointId());
        jsonGenerator.writeObjectField("name", deliveryPointDTO.getName());
        jsonGenerator.writeObjectField("phoneNo", deliveryPointDTO.getPhoneNo());
        jsonGenerator.writeObjectField("address", deliveryPointDTO.getAddress());
        jsonGenerator.writeObjectField("typePoint", deliveryPointDTO.getTypePoint());
    }
    
}
