package de.dhbw.ka.se.fibo.utils.backend;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import de.dhbw.ka.se.fibo.models.CashflowType;

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
    @SerializedName("place")
    public int placeID;
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
                ", placeID='" + placeID + '\'' +
                ", accountID=" + accountID +
                '}';
    }

    public CashflowType getCashflowType() {
        return isIncome ? CashflowType.INCOME : CashflowType.EXPENSE;
    }
}
