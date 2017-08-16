package cn.edu.nju.wonderland.ucountserver.entity;

import javax.persistence.*;

/**
 * 账户信息（用户名，卡类型，账号）
 * Created by green-cherry on 2017/8/16.
 */
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String username;
    private String cardType;
    private String cardId;

    @Column(name = "username", nullable = false, length = 50)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @Column(name = "card_type", nullable = false, length = 45)
    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }


    @Column(name = "card_id", nullable = false, length = 45)
    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (username != null ? !username.equals(account.username) : account.username != null) return false;
        if (cardType != null ? !cardType.equals(account.cardType) : account.cardType != null) return false;
        if (cardId != null ? !cardId.equals(account.cardId) : account.cardId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (cardType != null ? cardType.hashCode() : 0);
        result = 31 * result + (cardId != null ? cardId.hashCode() : 0);
        return result;
    }
}
