package de.dhbw.ka.se.fibo.models;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

public class Cashflow {

  private CashflowType type;
  private BigDecimal overallValue;
  private Date timestamp;
  private String name;

  public Cashflow(CashflowType type, BigDecimal overallValue, Date timestamp, String name) {
    setType(type);
    setOverallValue(overallValue);
    setTimestamp(timestamp);
    setName(name);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public CashflowType getType() {
    return type;
  }

  public void setType(CashflowType type) {
    this.type = type;
  }

  public BigDecimal getOverallValue() {
    return overallValue;
  }

  public void setOverallValue(BigDecimal overallValue) {
    this.overallValue = overallValue.setScale(2, RoundingMode.HALF_UP);
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }
}
