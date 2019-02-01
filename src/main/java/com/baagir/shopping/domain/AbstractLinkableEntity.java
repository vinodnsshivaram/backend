package com.baagir.shopping.domain;

import com.baagir.shopping.utils.JsonDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.text.ParseException;
import java.util.Date;

public abstract class AbstractLinkableEntity {
    @JsonSerialize(using=JsonDateSerializer.class)
    private Date createdAt;
    @JsonSerialize(using=JsonDateSerializer.class)
    private Date updatedAt;

    public Date getCreatedAt() throws ParseException {
        return createdAt == null ? null : JsonDateSerializer.dateFormat.parse(JsonDateSerializer.dateFormat.format(createdAt));
    }

    public void setCreatedAt(Date createdAt) throws ParseException {
        this.createdAt = createdAt == null ? new Date() : JsonDateSerializer.dateFormat.parse(JsonDateSerializer.dateFormat.format(createdAt));
    }

    public Date getUpdatedAt() throws ParseException {
        return updatedAt == null ? null : JsonDateSerializer.dateFormat.parse(JsonDateSerializer.dateFormat.format(updatedAt));
    }

    public void setUpdatedAt(Date updatedAt) throws ParseException {
        this.updatedAt = updatedAt == null ? new Date() : JsonDateSerializer.dateFormat.parse(JsonDateSerializer.dateFormat.format(updatedAt));
    }

    public void cleanup() throws ParseException {
        setCreatedAt(null);
        setUpdatedAt(null);
    }
}
