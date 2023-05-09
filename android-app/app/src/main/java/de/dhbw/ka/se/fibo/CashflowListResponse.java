package de.dhbw.ka.se.fibo;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CashflowListResponse {

    public int id;
    @SerializedName("is_income")
    public boolean isIncome;
    @SerializedName("overall_value")
    public BigDecimal overallValue;
    public LocalDateTime created;
    public LocalDateTime updated;
    @SerializedName("category")
    public int categoryID;
    public String place;
    @SerializedName("account")
    public int accountID;

    @Override
    public String toString() {
        return "CashflowListResponse{" +
                "id=" + id +
                ", isIncome=" + isIncome +
                ", overallValue=" + overallValue +
                ", created='" + created + '\'' +
                ", updated='" + updated + '\'' +
                ", categoryID=" + categoryID +
                ", place='" + place + '\'' +
                ", accountID=" + accountID +
                '}';
    }
}
