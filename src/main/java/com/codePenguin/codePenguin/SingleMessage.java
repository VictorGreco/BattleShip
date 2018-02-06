package com.codePenguin.codePenguin;

import javax.persistence.Embeddable;
import java.util.Date;

@Embeddable
public class SingleMessage {

    private String writerName;
    private String messageBody;
    private Date creationDate;

    public SingleMessage(){}

    public SingleMessage(String writerName, String messageBody) {
        this.writerName = writerName;
        this.messageBody = messageBody;
        this.creationDate = new Date();
    }

// getter and setter
    public String getWriterId() {
        return writerName;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public Date getCreationDate() {
        return creationDate;
    }
}
