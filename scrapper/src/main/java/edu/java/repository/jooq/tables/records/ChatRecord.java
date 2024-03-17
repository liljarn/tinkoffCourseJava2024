/*
 * This file is generated by jOOQ.
 */

package edu.java.repository.jooq.tables.records;

import edu.java.repository.jooq.tables.Chat;
import java.beans.ConstructorProperties;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Row1;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class ChatRecord extends UpdatableRecordImpl<ChatRecord> implements Record1<Long> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>CHAT.CHAT_ID</code>.
     */
    public void setChatId(@NotNull Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>CHAT.CHAT_ID</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public Long getChatId() {
        return (Long) get(0);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record1 type implementation
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Row1<Long> fieldsRow() {
        return (Row1) super.fieldsRow();
    }

    @Override
    @NotNull
    public Row1<Long> valuesRow() {
        return (Row1) super.valuesRow();
    }

    @Override
    @NotNull
    public Field<Long> field1() {
        return Chat.CHAT.CHAT_ID;
    }

    @Override
    @NotNull
    public Long component1() {
        return getChatId();
    }

    @Override
    @NotNull
    public Long value1() {
        return getChatId();
    }

    @Override
    @NotNull
    public ChatRecord value1(@NotNull Long value) {
        setChatId(value);
        return this;
    }

    @Override
    @NotNull
    public ChatRecord values(@NotNull Long value1) {
        value1(value1);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ChatRecord
     */
    public ChatRecord() {
        super(Chat.CHAT);
    }

    /**
     * Create a detached, initialised ChatRecord
     */
    @ConstructorProperties({ "chatId" })
    public ChatRecord(@NotNull Long chatId) {
        super(Chat.CHAT);

        setChatId(chatId);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised ChatRecord
     */
    public ChatRecord(edu.java.repository.jooq.tables.pojos.Chat value) {
        super(Chat.CHAT);

        if (value != null) {
            setChatId(value.getChatId());
            resetChangedOnNotNull();
        }
    }
}
