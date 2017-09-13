package cn.edu.nju.wonderland.ucountserver.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 提醒（用户名，提醒类型，提醒时间）
 * Created by green-cherry on 2017/8/17.
 */
@Deprecated
@Entity
@Table(name = "remind", schema = "Ucount_data", catalog = "")
public class Remind {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    private String username;
    private String remindType;
    private Timestamp remindTime;

    @Id
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "username", nullable = true, length = 50)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "remind_type", nullable = true, length = 45)
    public String getRemindType() {
        return remindType;
    }

    public void setRemindType(String remindType) {
        this.remindType = remindType;
    }

    @Basic
    @Column(name = "remind_time", nullable = true)
    public Timestamp getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(Timestamp remindTime) {
        this.remindTime = remindTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Remind that = (Remind) o;

        if (id != that.id) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (remindType != null ? !remindType.equals(that.remindType) : that.remindType != null) return false;
        if (remindTime != null ? !remindTime.equals(that.remindTime) : that.remindTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (remindType != null ? remindType.hashCode() : 0);
        result = 31 * result + (remindTime != null ? remindTime.hashCode() : 0);
        return result;
    }
}
