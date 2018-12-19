package com.nowcoder.model;

import java.util.Date;

public class Message{
    private int id;
    private int fromid;
    private int toid;
    private String content;
    private int conversationId;
    private Date createdDate;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the createdDate
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * @param createdDate the createdDate to set
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * @return the conversationId
     */
    public int getConversationId() {
        return conversationId;
    }

    /**
     * @param conversationId the conversationId to set
     */
    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the toid
     */
    public int getToid() {
        return toid;
    }

    /**
     * @param toid the toid to set
     */
    public void setToid(int toid) {
        this.toid = toid;
    }

    /**
     * @return the fromid
     */
    public int getFromid() {
        return fromid;
    }

    /**
     * @param fromid the fromid to set
     */
    public void setFromid(int fromid) {
        this.fromid = fromid;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

}
